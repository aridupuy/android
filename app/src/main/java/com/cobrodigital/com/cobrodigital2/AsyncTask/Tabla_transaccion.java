package com.cobrodigital.com.cobrodigital2.AsyncTask;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.AsyncTaskCompat;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.Transacciones;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Created by ariel on 25/10/16.
 */

public class Tabla_transaccion extends AsyncTaskLoader implements Loader.OnLoadCompleteListener<Vector<Transaccion>>{
    Vector<Transaccion> transacciones;
    public Tabla_transaccion(Context context,Vector<Transaccion> transacciones) {
        super(context);
        this.transacciones=transacciones;
    }
    @Override
    public Object loadInBackground() {
        String hasta = Transacciones.fecha_hasta;
        String desde = Transacciones.fecha_desde;
        HashMap<String, String> variables = null;
        try {
            variables = Transacciones.obtener_filtros();
        } catch (ParseException e) {
            e.printStackTrace();
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
                    for (int i = 0; i < datos.length(); i++) {
                        Transaccion transaccion = new Transaccion();
                        transaccion = transaccion.leerTransaccion(Transacciones.context, datos.getJSONObject(i));
                        if (transaccion != null)
                            this.transacciones.add(transaccion);
                    }
                } else {
                    System.out.println("No hay datos disponibles");
                    Gestor_de_mensajes_usuario.mensaje("No hay datos disponibles.", Transacciones.context);
                }
            } else {
                System.out.println("Comunicacion fallida!");
                Gestor_de_mensajes_usuario.mensaje("Comunicacion fallida!", Transacciones.context);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            e.getMessage();
            e.getLocalizedMessage();
        }
        return this.transacciones;
    }
    @Override
    public void onLoadComplete(Loader<Vector<Transaccion>> loader, Vector<Transaccion> data) {

    }
}
