package com.cobrodigital.com.cobrodigital2.Gestores;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cobrodigital.com.cobrodigital2.Gestores.Fragment.Dialogo_alerta;

/**
 * Created by ariel on 14/10/16.
 */

public class Gestor_de_mensajes_usuario extends Toast {
    public static Context context=null;
    public Gestor_de_mensajes_usuario(Context context) {
        super(context);
        this.context = context;
    }
    public static void mensaje(String mensaje, Context context){
        if(context!=null){
            Toast toast = Toast.makeText(context,mensaje,Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            Log.e("Gestor_de_mensajes","Error al mostrar el mensaje, el Activity se ha cerrado?");
        }
    }
    public static void mensaje(String mensaje){
            Toast toast = Toast.makeText(context,mensaje,Toast.LENGTH_LONG);
            toast.show();
    }
    public static void dialogo(String mensaje,Activity activity){
        FragmentManager fragmentManager = activity.getFragmentManager();
        Dialogo_alerta dialogo = new Dialogo_alerta(mensaje,"CobroDigital S.R.L.","ACEPTAR");
        dialogo.show(fragmentManager,"Aceptar");
    }
   }
