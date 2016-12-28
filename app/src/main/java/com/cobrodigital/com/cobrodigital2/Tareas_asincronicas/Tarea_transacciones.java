package com.cobrodigital.com.cobrodigital2.Tareas_asincronicas;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    String cantidad_transacciones_a_mostrar;

    public Tarea_transacciones(View view,Bundle Bundle,Fragment context){
        this.view=view;
        this.bundle=Bundle;
        this.context=context;
    }
    private HashMap<String, String> variables = new HashMap<String, String>();
    @Override
    protected void onPreExecute() {
//        context.getFragmentManager().executePendingTransactions();
        lista= (ListView) view.findViewById(R.id.lista);
        view.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        view.findViewById(R.id.textView5).setVisibility(View.GONE);
        view.findViewById(R.id.Saldo).setVisibility(View.GONE);
        view.findViewById(R.id.tr_toolbar).setVisibility(View.GONE);
    }

    @Override
    protected Vector<Transaccion> doInBackground(String... input) {
        String hasta="";
        String desde="";
        String tipo="";
        Vector<Transaccion> transacciones=new Vector<Transaccion>();
        if(input.length>0){
            desde = input[0];
            hasta = input[1];
            tipo= input[2];
            cantidad_transacciones_a_mostrar =input[3];

        }
        try {
            System.out.println("Busco en el ws");
            if (!Gestor_de_credenciales.esta_asociado())
                return null;
                LinkedHashMap filtros = new LinkedHashMap();
                if(tipo!="")
                    filtros.put("tipo",tipo);
                Date Fecha = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            if (desde=="")
                desde = format.format(Fecha);
            if (hasta=="")
                hasta = format.format(Fecha);
            //ver que hacer con filtros
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
    @TargetApi(Build.VERSION_CODES.M)
    protected void onPostExecute(Vector<Transaccion> VectorTransaccion) {
        super.onPostExecute(VectorTransaccion);
        view.findViewById(R.id.textView5).setVisibility(View.VISIBLE);
        TextView saldo= (TextView) view.findViewById(R.id.Saldo);
        saldo.setVisibility(View.VISIBLE);
        Transaccion transaccion;
        lista.setAdapter(new Lista_transaccion_adapter(context.getContext(),R.layout.item_transacciones,VectorTransaccion));
        saldo.setText("$"+saldo_total);
        view.findViewById(R.id.progressbar).setVisibility(View.GONE);
        view.findViewById(R.id.tr_toolbar).setVisibility(View.VISIBLE);
    }
}
