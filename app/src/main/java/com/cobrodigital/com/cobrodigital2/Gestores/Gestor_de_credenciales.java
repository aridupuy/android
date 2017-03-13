package com.cobrodigital.com.cobrodigital2.Gestores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.cobrodigital.com.cobrodigital2.Factory.credencialFactory;
import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Token_google;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.LectorQR;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

//import com.cobrodigital.com.cobrodigital2.Services.Tareas_asincronicas.Tarea_registrarGCM;

/**
 * Created by ariel on 14/10/16.
 */

public class Gestor_de_credenciales {

    public static final boolean USAR_TOKEN=false;

    public static boolean esta_asociado(){
        if(CobroDigital.credencial!=null /*&& CobroDigital.token_id!=null*/){
            System.out.println("esta asociado");
            return true;
        }
        System.out.println("no esta asociado");
        return false;
    }
    public static void iniciar_asociacion(final Context context,final Intent intent,final  Activity activity){
        if(USAR_TOKEN) {
            if (CobroDigital.token_id != null) {
                asociar(context, intent, activity);
            }
        }
        else
            asociar(context, intent, activity);
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
        try {
            if(CobroDigital.credencial!=null){
                credencialFactory factory=new credencialFactory(context);
                factory.guardar(CobroDigital.credencial);
            }
            else
                return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean re_asociar(Context context) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, PackageManager.NameNotFoundException {
        try {
            Credencial credencial = new Credencial(context);
            CobroDigital.credencial=credencial.obtenerCredencial();
            if(USAR_TOKEN) {
                String tkn = Token_google.obtener_token(context);
                if (tkn != null)
                    CobroDigital.token_id = new Token_google(tkn, context);
                else
                    return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(CobroDigital.credencial!=null/* && CobroDigital.token_id!=null */){
            return true;
        }
        return false;

    }
    public static boolean desasociar(Context context){
        try {
            if(CobroDigital.credencial.BorrarCredencial()){
                CobroDigital.credencial=null;
                if(!CobroDigital.token_id.borrar_token())
                    return false;
                else
                    CobroDigital.token_id=null;
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
            return false;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}