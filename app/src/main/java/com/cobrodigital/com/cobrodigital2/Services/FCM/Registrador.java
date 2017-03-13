package com.cobrodigital.com.cobrodigital2.Services.FCM;

import android.content.Context;
import android.util.Log;

import com.cobrodigital.com.cobrodigital2.Model.Token_google;
import com.cobrodigital.com.cobrodigital2.Services.FCM.Tareas_asincronicas.Tarea_registrar;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class Registrador extends FirebaseInstanceIdService implements Tarea_registrar.AsyncResponse {
    Context context;
    public Registrador(Context context) {
        this.context=context;
    }
    public Registrador() {
    }
    @Override
    public void onTokenRefresh() {
        //Obtener el token de registro
        Log.d("Registrador","Obteniendo el token");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Registrador",refreshedToken);
        Token_google token_google=new Token_google(refreshedToken,context);
        token_google.guardar_token();
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        Tarea_registrar tarea_registrar= new Tarea_registrar(this,getApplicationContext());
        tarea_registrar.execute(token);

    }

    @Override
    public void processFinish(Boolean resultado) {
        if (!resultado){
            CobroDigital.credencial=null;
        }
    }
}
