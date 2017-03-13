package com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Tareas_asincronicas;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

/**
 * Created by ariel on 14/02/17.
 */

public class Tarea_retiros extends AsyncTask<Object,Boolean,Boolean> {
    Context context;
    public interface AsyncResponse {
        void processFinish(Boolean resultado);
    }
    public AsyncResponse delegate;
    public Tarea_retiros(Context context,AsyncResponse delegate){
        this.context=context;
        this.delegate=delegate;
    }
    @Override
    protected Boolean doInBackground(Object[] objects) {
        String cuit,titular,nombre;
        Double plata;
        cuit=(String)objects[0];
        titular=(String)objects[1];
        plata=(double)objects[2];
        nombre=(String)objects[3];
        try {
            CobroDigital.webservice.webservice_retiro.realizar_peticion(cuit, titular, plata, nombre);
        }catch (javax.net.ssl.SSLHandshakeException ex){
            Gestor_de_mensajes_usuario.dialogo("No podemos procesar la solicitud, intente mas tarde", (Activity)context);
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(CobroDigital.webservice.obtener_resultado().equals("1")){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean resultado) {
        super.onPostExecute(resultado);
        delegate.processFinish(resultado);
    }
}
