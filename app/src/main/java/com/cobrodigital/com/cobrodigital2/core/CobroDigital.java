package com.cobrodigital.com.cobrodigital2.core;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Token_google;
import com.cobrodigital.com.cobrodigital2.Modulos.Tools.Tools;
import com.cobrodigital.com.cobrodigital2.Webservice.Webservice;

public class CobroDigital {
    public static Credencial credencial;
    public static Token_google token_id;
    public static Webservice webservice;
    private static Context context;
    public CobroDigital(Credencial credencial, Context context) throws Exception{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if(credencial!=null){
            this.credencial=credencial;
        }
        if(Tools.isNetDisponible(context))
            Gestor_de_mensajes_usuario.dialogo("Por favor verifique su conectividad.",(Activity)context);
        if(Tools.isOnlineNet())
            Gestor_de_mensajes_usuario.dialogo("Por favor verifique su conectividad.",(Activity)context);
        this.context=context;
    }
    public CobroDigital(String idComercio, String sid, Context context) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (idComercio == "") {
            throw new Exception("No definio idComercio");
        }
        if (sid == "") {
            throw new Exception("No definio sid");
        }
        this.context=context;
        this.credencial.set_IdComercio(idComercio);
        this.credencial.set_sid(sid);
    }



}