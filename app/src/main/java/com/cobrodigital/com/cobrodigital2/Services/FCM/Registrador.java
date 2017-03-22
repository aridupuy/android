package com.cobrodigital.com.cobrodigital2.Services.FCM;

import android.util.Log;

import com.cobrodigital.com.cobrodigital2.Services.FCM.Tareas_asincronicas.Tarea_registrar;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class Registrador extends FirebaseInstanceIdService implements Tarea_registrar.AsyncResponse {
    @Override
    public void onTokenRefresh() {
        //Obtener el token de registro
        Log.d("Registrador","Obteniendo el token");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Registrador",refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Tarea_registrar tarea_registrar= new Tarea_registrar(this,getApplicationContext());
        tarea_registrar.execute(token);

    }

    @Override
    public void processFinish(Boolean resultado) {
        Log.d("procesando","procesando");
        if (!resultado){
            CobroDigital.credencial=null;
        }
    }
}
