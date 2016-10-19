package com.cobrodigital.com.cobrodigital2.Gestores;

import android.content.Context;
import android.content.Intent;

import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.LectorQR;

import org.json.JSONException;

/**
 * Created by ariel on 14/10/16.
 */

public class Gestor_de_credenciales {

    public static boolean esta_asociado(){
        if(CobroDigital.credencial!=null)
            return true;
        return false;
    }
    public static Credencial asociar(Context context, Intent intent){
        String contents = intent.getStringExtra("SCAN_RESULT");
        LectorQR lectorQR=new LectorQR(context);
        try{
            CobroDigital.credencial=lectorQR.leer(contents);
        }catch (JSONException e){
            Gestor_de_mensajes_usuario.mensaje(e.getMessage(),context);
        }
        CobroDigital.credencial.set();
        return CobroDigital.credencial;
    }
    public static Credencial re_asociar(Context context){
        Credencial credencial= new Credencial(context);
        credencial.onCreate(credencial.getWritableDatabase());
        CobroDigital.credencial = credencial.obtenerCredencial();
        credencial.set(); //revisar que no se hagan updates al cuete
        return credencial;
    }
    public static void desasociar(Context context){
        CobroDigital.credencial.BorrarCredencial();
    }
}
