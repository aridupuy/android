package com.cobrodigital.com.cobrodigital2.Gestores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Token_google;
import com.cobrodigital.com.cobrodigital2.Services.FCM.Registrador;
import com.cobrodigital.com.cobrodigital2.Services.FCM.Tareas_asincronicas.Tarea_registrar;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.Config;
import com.cobrodigital.com.cobrodigital2.core.LectorQR;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

//import com.cobrodigital.com.cobrodigital2.Services.Tareas_asincronicas.Tarea_registrarGCM;

/**
 * Created by ariel on 14/10/16.
 */

public class Gestor_de_credenciales {

    public static final boolean USAR_TOKEN=true;

    public static boolean esta_asociado(){
        try {
            if (!CobroDigital.credencial.is_null_IdComercio() && !CobroDigital.credencial.is_null_sid() && !CobroDigital.token_id.is_null_token()) {
                System.out.println("esta asociado");
                return true;
            }
        }catch (java.lang.NullPointerException ex){
            System.out.println("no esta asociado");
            return false;
        }
        System.out.println("no esta asociado");
        return false;
    }
    public static boolean iniciar_asociacion(final Context context,final Intent intent,final  Activity activity){
            Intent intento=new Intent(activity, Registrador.class);
            context.startService(intento);
            if(!asociar(context, intent, activity))
                return false;
        CobroDigital.config= Config.get_instance_config(CobroDigital.config,context);
        return true;
    }
    public static boolean asociar(Context context, Intent intent,Activity activity){
        String contents = intent.getStringExtra("SCAN_RESULT");
        LectorQR lectorQR=new LectorQR(context);
        try{
            lectorQR.leer(contents);
        }catch (JSONException e){
            Gestor_de_mensajes_usuario.mensaje(e.getMessage(),context);
            e.printStackTrace();
            return false;
        }
            if(CobroDigital.credencial!=null){
                if(USAR_TOKEN)
                    if(!asociar_google(context,activity)){
                        return false;
                    }
            }
            else
                return false;
        return true;
    }
    public static boolean re_asociar(Context context) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, PackageManager.NameNotFoundException {
        Credencial credencial = new Credencial(context);
        CobroDigital.credencial=credencial.obtenerCredencial();
        CobroDigital.token_id= new Token_google(Token_google.obtener_token(context),context);
        if (CobroDigital.credencial!=null && !CobroDigital.credencial.is_null_IdComercio() && !CobroDigital.credencial.is_null_sid() && CobroDigital.token_id!=null && !CobroDigital.token_id.is_null_token()) {
            CobroDigital.config= Config.get_instance_config(CobroDigital.config,context);
            return true;
        }
        return false;
    }
    public static boolean desasociar(Context context){
            if(CobroDigital.credencial!=null && CobroDigital.credencial.BorrarCredencial()){
                CobroDigital.credencial=null;
                if(CobroDigital.token_id!=null && !CobroDigital.token_id.borrar_token())
                    return false;
                else
                    CobroDigital.token_id=null;
                return true;
            }
        return false;
    }
    protected static boolean asociar_google(final Context context,Activity activity){
        CobroDigital.token_id =new Token_google(FirebaseInstanceId.getInstance().getToken(),context);
        final Boolean[] result = new Boolean[1];
        result[0]=null;
        //sacar
//        CobroDigital.token_id.setToken("asdasdadsasd");
        Tarea_registrar tarea_registrar = new Tarea_registrar(new Tarea_registrar.AsyncResponse() {
            @Override
            public void processFinish(Boolean resultado) {
                if(resultado){
                    CobroDigital.token_id.guardar_token();
                    result[0] =true;
                }
                else{
//                    Gestor_de_mensajes_usuario.mensaje("No se pudo registrar.",context);
                    result[0] =false;
                }
            }
        }, context,activity);
        tarea_registrar.execute(CobroDigital.token_id.getToken());
        return true;
    }

}