package com.cobrodigital.com.cobrodigital2.Gestores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cobrodigital.com.cobrodigital2.Modulos.Boletas.Boletas;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Estado_cuenta;
import com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Retiros;
import com.cobrodigital.com.cobrodigital2.Pagador;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Transacciones;
import com.cobrodigital.com.cobrodigital2.Modulos.Tools.Configuracion;

/**
 * Created by ariel on 16/01/17.
 */

public class Gestor_de_navegacion {
    Context context;
    public Gestor_de_navegacion(Context context){
        this.context=context;
    }
    public boolean navegar (int id){
        // Handle navigation view item clicks here.
        if (id == R.id.nav_loggin) {
            if (!Gestor_de_credenciales.esta_asociado()) {
                this.escanear(context);
            }
            else{
                Gestor_de_mensajes_usuario.mensaje("Usted ya esta asociado a una cuenta CobroDigital.",context);
            }
        }
        else if (id == R.id.nav_transacciones) {
            this.OnClickListarTransacciones(context);
        }
        else if (id == R.id.nav_estado) {
            this.estado_cuenta(context);
        }
        else if (id == R.id.nav_tools) {
            this.Configurar(context);
        }
        else if (id == R.id.nav_compartir) {

        }
        else if(id==R.id.nav_boleta){
            this.generar_boleta(context);
        }
        else if(id==R.id.nav_retiros){
            this.retiros(context);
        }


        return true;
    }

    public static void OnClickListarTransacciones(Context context) {
        context.startActivity(new Intent(context, Transacciones.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public static void escanear(Context context) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        ((Activity)context).startActivityForResult(intent, 0);
    }
    public static void Configurar(Context context) {
        Intent configuracion = new Intent(context, Configuracion.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(configuracion);
    }
    public void OnClickPagadorNew(View view) {
        (context).startActivity(new Intent(context, com.cobrodigital.com.cobrodigital2.Model.Pagador.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public static void generar_boleta(Context context){

        (context).startActivity(new Intent(context,Boletas.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public static void estado_cuenta(Context context){
        (context).startActivity(new Intent(context,Estado_cuenta.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public static void retiros(Context context){
        (context).startActivity(new Intent(context,Retiros.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
