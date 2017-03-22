package com.cobrodigital.com.cobrodigital2.Modulos.Correo.Tareas_asincronicas;

import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Modulos.Correo.Enviar_correo;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

/**
 * Created by ariel on 25/01/17.
 */

public class Tarea_enviar_correo extends AsyncTask<Integer,Integer,Boolean>{
    protected Enviar_correo context;
    protected AsyncResponse callback;
    public Tarea_enviar_correo(Enviar_correo context, AsyncResponse callback){
        this.context=context;
        this.callback=callback;
    }
    public interface AsyncResponse {
        void processFinish(Boolean resultado);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        context.findViewById(R.id.progressbar_enviar_correo).setVisibility(View.VISIBLE);
        context.findViewById(R.id.vista_enviar_correo).setVisibility(View.GONE);
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        int nro_boleta=integers[0];
        return enviar_mail(nro_boleta);
    }
    private boolean enviar_mail(int nro_Boleta){
        String destinatario=((EditText)context.findViewById(R.id.destinatario)).getText().toString();
        String asunto="Nueva boleta enviada";
        String mensaje="Se ha generado una nueva boleta";//poner un mensaje mas bonito aqui;
        try {
            CobroDigital.webservice.webservice_enviar_correo.enviar_boleta_correo(destinatario,asunto,mensaje,nro_Boleta);
        }catch (javax.net.ssl.SSLHandshakeException ex){
            Gestor_de_mensajes_usuario.dialogo("No podemos procesar la solicitud, intente mas tarde",context);
        }
        catch (Exception e) {
            e.printStackTrace();
            callback.processFinish(false);
            return false;
        }
        callback.processFinish(true);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        context.findViewById(R.id.progressbar_enviar_correo).setVisibility(View.GONE);
        context.findViewById(R.id.vista_enviar_correo).setVisibility(View.VISIBLE);
        if (aBoolean){
            Gestor_de_mensajes_usuario.mensaje("Boleta enviada correctamente",context.getApplicationContext());
        }
        else{
            Gestor_de_mensajes_usuario.mensaje("Ha ocurrido un error al enviar el mail",context.getApplicationContext());
        }
    }
}
