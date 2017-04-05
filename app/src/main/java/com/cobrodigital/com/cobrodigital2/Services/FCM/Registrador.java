package com.cobrodigital.com.cobrodigital2.Services.FCM;

import android.util.Log;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Services.FCM.Tareas_asincronicas.Tarea_registrar;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class Registrador extends FirebaseInstanceIdService implements Tarea_registrar.AsyncResponse {
    private final String TOPIC="COBRODIGITAL";
    @Override
    public void onTokenRefresh() {
        Log.d("Registrador","Obteniendo el token");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
        Log.d("Registrador",refreshedToken);

        //Obtener el token de registro
        Log.d("Registrador","Verificando conectividad.");
        try {
            if(!Gestor_de_credenciales.esta_asociado())
                if(!Gestor_de_credenciales.re_asociar(getBaseContext())){
                    return;
                }
            sendRegistrationToServer(refreshedToken);
        } catch (Exception e) {
//            e.printStackTrace();
            return;
        }
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
