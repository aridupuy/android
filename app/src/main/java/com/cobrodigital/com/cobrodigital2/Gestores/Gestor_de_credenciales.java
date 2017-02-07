package com.cobrodigital.com.cobrodigital2.Gestores;

import android.content.Context;
import android.content.Intent;

import com.cobrodigital.com.cobrodigital2.Factory.credencialFactory;
import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Pagador;
import com.cobrodigital.com.cobrodigital2.Modulos.Main.Main;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.LectorQR;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * Created by ariel on 14/10/16.
 */

public class Gestor_de_credenciales {
    private  static boolean PRUEBA=false;
    public static boolean esta_asociado(){
    //Probar guardar la info como sharedPreferences

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
            e.printStackTrace();

            return;
        }
        try {
            credencialFactory factory=new credencialFactory(context);
            factory.guardar(CobroDigital.credencial);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Boolean re_asociar(Context context){
        if(Main.emulador){
            CobroDigital.credencial=new Credencial();
            CobroDigital.credencial.set_IdComercio("FL662997");
            CobroDigital.credencial.set_sid("ABZ0ya68K791phuu76gQ5L662J6F2Y4j7zqE2Jxa3Mvd22TWNn4iip6L9yq");
            return  true;
        }
        try {
            Credencial credencial = new Credencial(context);
            CobroDigital.credencial=credencial.obtenerCredencial();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(CobroDigital.credencial!=null)
            return true;
        return false;

    }
    public static boolean desasociar(Context context){
        try {
            if(CobroDigital.credencial.BorrarCredencial()){
                CobroDigital.credencial=null;
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