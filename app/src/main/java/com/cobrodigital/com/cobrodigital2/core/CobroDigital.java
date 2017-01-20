package com.cobrodigital.com.cobrodigital2.core;

import android.content.Context;
import android.os.StrictMode;

import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Webservice.Webservice;

public class CobroDigital {
    public static Credencial credencial;
    public static Webservice webservice;
    private static Context context;
    public CobroDigital(Credencial credencial, Context context) throws Exception{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if(credencial!=null){
            this.credencial=credencial;
        }
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