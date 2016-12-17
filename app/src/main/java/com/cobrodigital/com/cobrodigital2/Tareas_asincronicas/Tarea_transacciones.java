package com.cobrodigital.com.cobrodigital2.Tareas_asincronicas;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Adapters.Lista_transaccion_adapter;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.Transacciones;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.fragment.Transacciones_fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

/**
 * Created by ariel on 14/12/16.
 */
public class Tarea_transacciones extends AsyncTask<String, Integer, Vector<Transaccion>> {
    private long tiempo_estimado_de_espera=5000;
    private String saldo_total="";
    static Fragment context;
    static View view;
    static Bundle bundle;
    static ListView lista;
    String cantidad_transacciones_a_mostrar="5";

    public Tarea_transacciones(View view,Bundle Bundle,Fragment context){
        this.view=view;
        this.bundle=Bundle;
        this.context=context;
    }
    private HashMap<String, String> variables = new HashMap<String, String>();
    @Override
    protected void onPreExecute() {
        context.getFragmentManager().executePendingTransactions();

        lista= (ListView) view.findViewById(R.id.lista);
//        lista.removeAllViewsInLayout();
        view.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        view.findViewById(R.id.textView5).setVisibility(View.GONE);
        view.findViewById(R.id.transascompletas).setVisibility(View.GONE);
        view.findViewById(R.id.Saldo).setVisibility(View.GONE);
    }

    @Override
    protected Vector<Transaccion> doInBackground(String... input) {
        String hasta,desde;
        Vector<Transaccion> transacciones=new Vector<Transaccion>();
        if(input.length>0){
            desde = input[0];
            hasta = input[1];
            cantidad_transacciones_a_mostrar =input[2];
        }

        try {
            System.out.println("Busco en el ws");
            if (!Gestor_de_credenciales.esta_asociado())
                return null;
            LinkedHashMap filtros = new LinkedHashMap();
            if (variables.size() > 0) {
                desde = (String) variables.get("desde");
                hasta = (String) variables.get("hasta");
                variables.remove("desde");
                variables.remove("hasta");
            } else {
                Date Fecha = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                desde = format.format(Fecha);
                hasta = format.format(Fecha);
            }
            CobroDigital.webservice.webservice_transacciones.consultar_transacciones(desde, hasta, filtros);
            if (CobroDigital.webservice.obtener_resultado().equals("1")) {
                Object transicion[] = CobroDigital.webservice.obtener_datos().toArray();
                if (transicion.length > 0) {
                    JSONArray datos = new JSONArray((String) transicion[0]);
                    if(Integer.parseInt(cantidad_transacciones_a_mostrar)==0){
                        cantidad_transacciones_a_mostrar=""+datos.length();
                    }
                    for (int i = 0; (i<Integer.parseInt(cantidad_transacciones_a_mostrar )); i++) {
                        Transaccion transaccion = new Transaccion();
                        transaccion = transaccion.leerTransaccion(context.getContext(), datos.getJSONObject(i));
                        if (transaccion != null){
                            transacciones.add(transaccion);
                            saldo_total=transaccion.getSaldo_acumulado();
                        }
                    }

                } else {
                    System.out.println("No hay datos disponibles");
//                            Gestor_de_mensajes_usuario.mensaje("No hay datos disponibles.",Transacciones.context);
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
        return transacciones;
    }
    protected void onPostExecute(Vector<Transaccion> VectorTransaccion) {
        super.onPostExecute(VectorTransaccion);
        view.findViewById(R.id.textView5).setVisibility(View.VISIBLE);
        TextView saldo= (TextView) view.findViewById(R.id.Saldo);
        saldo.setVisibility(View.VISIBLE);
        Transaccion transaccion;
        System.out.println(VectorTransaccion.size());
        lista.setAdapter(new Lista_transaccion_adapter(context.getContext(),R.layout.item_transacciones,VectorTransaccion));
        saldo.setText("$"+saldo_total);
//        for (int i = 0; (i < Integer.parseInt(cantidad_transacciones_a_mostrar) || Integer.parseInt(cantidad_transacciones_a_mostrar) == 0) && i < VectorTransaccion.size(); i++) {

//            transaccion = (Transaccion) VectorTransaccion.get(i);
//            TableRow row = new TableRow(context.getContext());
//            LayoutInflater inflador = context.getLayoutInflater(bundle);
//            View ejemplo_fila = inflador.inflate(R.layout.test, row);
//            ///primera parte
//            TextView fecha = (TextView) ejemplo_fila.findViewById(R.id.fecha);
//            fecha.setText(transaccion.getFecha());
//            TextView neto = (TextView) ejemplo_fila.findViewById(R.id.neto);
//            neto.setText("$" + String.valueOf(transaccion.getNeto()));
//            ///segunda parte
//            TextView bol = (TextView) ejemplo_fila.findViewById(R.id.bol);
//            bol.setTextSize(9);
//            bol.setText(transaccion.getNro_boleta());
//            TextView nombre = (TextView) ejemplo_fila.findViewById(R.id.nombre);
//            nombre.setText(transaccion.getNombre());
//            TextView concepto = (TextView) ejemplo_fila.findViewById(R.id.concepto);
//            concepto.setText(transaccion.getNombre());
//            TextView info = (TextView) ejemplo_fila.findViewById(R.id.info);
//            info.setText(transaccion.getInfo());
//            saldo = transaccion.getSaldo_acumulado();
//            tabla_layout.addView(row);
//            TextView Saldo_vista = (TextView) view.findViewById(R.id.Saldo);
//            Saldo_vista.setText("$" + saldo);
//        }
        view.findViewById(R.id.progressbar).setVisibility(View.GONE);
        if (Integer.parseInt(cantidad_transacciones_a_mostrar) > 0);
//            view.findViewById(R.id.transascompletas).setVisibility(View.VISIBLE);

    }
}
