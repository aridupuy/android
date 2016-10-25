package com.cobrodigital.com.cobrodigital2.core;

import android.os.StrictMode;
import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Personalizacion;
import com.cobrodigital.com.cobrodigital2.Webservice.Webservice;

public class CobroDigital {
    public static Credencial credencial;
    public static Personalizacion personalizacion;
    public static Webservice webservice;
    public CobroDigital(Credencial credencial) throws Exception{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if(credencial!=null){
            this.credencial=credencial;
        }
        if(personalizacion!=null)
            this.personalizacion=new Personalizacion();
    }
    public CobroDigital(String idComercio, String sid) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (idComercio == "") {
            throw new Exception("No definio idComercio");
        }
        if (sid == "") {
            throw new Exception("No definio sid");
        }
        this.credencial.set_IdComercio(idComercio);
        this.credencial.set_sid(sid);
    }


}