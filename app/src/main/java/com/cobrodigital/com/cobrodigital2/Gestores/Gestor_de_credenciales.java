package com.cobrodigital.com.cobrodigital2.Gestores;

import android.content.Context;
import android.content.Intent;
import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.LectorQR;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * Created by ariel on 14/10/16.
 */

public class Gestor_de_credenciales {

    public static boolean esta_asociado(){
        if(CobroDigital.credencial!=null){
            System.out.println("esta asociado");
            return true;
        }
        System.out.println("no esta asociado");
        return false;
    }
    public static void asociar(Context context, Intent intent){
        String contents = intent.getStringExtra("SCAN_RESULT");
        LectorQR lectorQR=new LectorQR(context);
        try{
            lectorQR.leer(contents);
        }catch (JSONException e){
            Gestor_de_mensajes_usuario.mensaje(e.getMessage(),context);
        }
        try {
            CobroDigital.credencial.guardar(CobroDigital.credencial);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }
    public static Boolean re_asociar(Context context){
        try {
            CobroDigital.credencial = new Credencial(context).obtenerCredencial();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if(CobroDigital.credencial!=null)
            return true;
        return false;

    }
    public static boolean desasociar(Context context){
        try {
            if(CobroDigital.credencial.BorrarCredencial())
                return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
