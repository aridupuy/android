package com.cobrodigital.com.cobrodigital2.Tareas_asincronicas;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Adapters.Lista_transaccion_adapter;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.Transacciones;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Created by ariel on 14/12/16.
 */
public class Tarea_transacciones extends AsyncTask<String, Integer, Vector<Transaccion>> {
    private long tiempo_estimado_de_espera=5000;
    private String saldo_total="";
    private int pagina=0;
    static Fragment context;
    static View view;
    static Bundle bundle;
    static ListView lista;
    static View footer;
    static int offset;
    static int limit;
    private static boolean cargando=false;
    String cantidad_transacciones_a_mostrar;

    public Tarea_transacciones(View view,View footerView,Bundle Bundle,Fragment context){
        this.view=view;
        this.footer=footerView;
        this.bundle=Bundle;
        this.context=context;
    }
    private HashMap<String, String> variables = new HashMap<String, String>();



    @Override
    protected void onPreExecute() {
        cargando=context.getActivity().getSharedPreferences("Ocupado",0).getBoolean("ocupado",false);
        lista= (ListView) view.findViewById(R.id.lista);
        footer.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        lista.addFooterView(footer);
        view.findViewById(R.id.textView5).setVisibility(View.GONE);
        view.findViewById(R.id.Saldo).setVisibility(View.GONE);
        view.findViewById(R.id.tr_toolbar).setVisibility(View.GONE);

    }

    @Override
    protected Vector<Transaccion> doInBackground(String... input) {
        if(!cargando) {
            ////////////////////////Semaforo///////////////////////////////
            SharedPreferences preferencias=context.getActivity().getSharedPreferences("Transacciones",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferencias.edit();
            editor.putBoolean("ocupado",true);
            editor.commit();
            /////////////////////////////////////////////////////////////
            String hasta = "";
            String desde = "";
            String tipo = "";
            Vector<Transaccion> transacciones = new Vector<Transaccion>();
            if (input.length > 0) {
                desde = input[0];
                hasta = input[1];
                tipo = input[2];
                cantidad_transacciones_a_mostrar = input[3];
            }
            if(bundle!=null){
                offset=bundle.getInt("Offset",0);
                limit=bundle.getInt("Limit",Integer.parseInt(cantidad_transacciones_a_mostrar));
            }
            try {
                System.out.println("Busco en el ws");
                if (!Gestor_de_credenciales.esta_asociado())
                    return null;
                LinkedHashMap filtros = new LinkedHashMap();
                if (!tipo.equals(""))
                    filtros.put("tipo", tipo);
                tipo = "";
                Date Fecha = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                if (desde == "")
                    desde = format.format(Fecha);
                if (hasta == "")
                    hasta = format.format(Fecha);
                //ver que hacer con filtros
                CobroDigital.webservice.webservice_transacciones.consultar_transacciones(desde, hasta, filtros,offset,limit);
                if (CobroDigital.webservice.obtener_resultado().equals("1")) {
                    Object transicion[] = CobroDigital.webservice.obtener_datos().toArray();
                    if (transicion.length > 0) {
                        JSONArray datos = new JSONArray((String) transicion[0]);
                        if (Integer.parseInt(cantidad_transacciones_a_mostrar) == 0) {
                            cantidad_transacciones_a_mostrar = "" + datos.length();
                        }
                        for (int i = 0; (i < Integer.parseInt(cantidad_transacciones_a_mostrar)); i++) {
                            Transaccion transaccion = new Transaccion();
                            transaccion = transaccion.leerTransaccion(context.getContext(), datos.getJSONObject(i));
                            if (transaccion != null) {
                                transacciones.add(transaccion);
                                saldo_total = transaccion.getSaldo_acumulado();
                            }
                        }

                    } else {
                        System.out.println("No hay datos disponibles");
                        return null;
                    }
                } else {
                    try {
                        Thread.currentThread().sleep(tiempo_estimado_de_espera);
                        this.doInBackground(input);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Comunicacion fallida!");
                        Gestor_de_mensajes_usuario.mensaje("Comunicacion fallida!", context.getContext());
                        return null;
                    }
                }

            } catch (SQLException e) {
                System.out.println("No hace falta agregar");
            } catch (JSONException e) {
                System.out.println("Error en la lectura de datos.");
            } catch (MalformedURLException e) {
                System.out.println("Error en el envio de datos.");
            } catch (IOException e) {
                System.out.println("Error de entrada salida.");
            }
            ////////////////////////Semaforo///////////////////////////////
            editor.remove("ocupado");
            editor.commit();
            /////////////////////////////////////////////////////////////
            return transacciones;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void onPostExecute(final Vector<Transaccion> VectorTransaccion) {
        super.onPostExecute(VectorTransaccion);
        lista.removeFooterView(footer);
        view.findViewById(R.id.loading_principal_transacciones).setVisibility(View.GONE);
        if(VectorTransaccion!=null && VectorTransaccion.size()>0) {
            view.findViewById(R.id.textView5).setVisibility(View.VISIBLE);
            TextView saldo = (TextView) view.findViewById(R.id.Saldo);
            saldo.setVisibility(View.VISIBLE);
            Transaccion transaccion;
            HeaderViewListAdapter listadapter=(HeaderViewListAdapter)lista.getAdapter();
            if(listadapter!=null){
                Lista_transaccion_adapter adapter= ((Lista_transaccion_adapter) ((ArrayAdapter) listadapter.getWrappedAdapter()));
                if (adapter!=null)
                adapter.addItems(VectorTransaccion);
            }
            else
                lista.setAdapter(new Lista_transaccion_adapter(context.getContext(), R.layout.item_transacciones, VectorTransaccion));
            lista.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }
                @Override
                public void onScroll(AbsListView absListView, int primer_item_visible, int total_items_visibles, int total_items) {
                    boolean lastItem = primer_item_visible + total_items_visibles == total_items && lista.getChildAt(total_items_visibles -1) != null && lista.getChildAt(total_items_visibles-1).getBottom() <= lista.getHeight();
                    SharedPreferences preferences=context.getActivity().getSharedPreferences("Transacciones",Context.MODE_PRIVATE);
                    Tarea_transacciones.cargando=preferences.getBoolean("ocupado",false);
                    if(lastItem && !Tarea_transacciones.cargando){
                        String desde="";
                        String hasta="";
                        String tipo="";
                        Bundle variables = null;
                        if(bundle!=null && bundle.containsKey(Transacciones.FILTROS))
                            variables=bundle.getBundle(Transacciones.FILTROS);
                        if(bundle!=null && bundle.containsKey(Transacciones.TIPO))
                            tipo=variables.getString(Transacciones.TIPO);
                        if(bundle!=null && bundle.containsKey("desde"))
                            desde=variables.getString("desde","");
                        if(bundle!=null && bundle.containsKey("hasta"))
                            hasta=variables.getString("hasta","");
                        bundle.putInt("Offset",offset+Integer.parseInt(cantidad_transacciones_a_mostrar));
                        bundle.putInt("Limit",limit+Integer.parseInt(cantidad_transacciones_a_mostrar));
                        Tarea_transacciones tr= new Tarea_transacciones(view,footer,bundle,context);
                        tr.execute(desde,hasta,tipo,cantidad_transacciones_a_mostrar);
                        Tarea_transacciones.cargando=false;
                    }
                }
            });
            saldo.setText("$" + saldo_total);
            view.findViewById(R.id.tr_toolbar).setVisibility(View.VISIBLE);
        }
        else{
            //mejorar este caso
            Gestor_de_mensajes_usuario.mensaje("No Existen transacciones que mostrar",context.getContext());
            TextView saldo = (TextView) view.findViewById(R.id.Saldo);
            saldo.setVisibility(View.VISIBLE);
            saldo.setText("No Existen transacciones que mostrar");
            view.findViewById(R.id.tr_toolbar).setVisibility(View.VISIBLE);
        }
    }

}
