package com.cobrodigital.com.cobrodigital2.core;

import java.util.HashMap;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
/**
 * Created by Ariel on 31/08/16.
 */
public class LectorQR {
    private String sid="";
    private String IdComercio="";

    public  LectorQR(){

        sid="KA289659";
        IdComercio="V2nJUHv7110BI4v1FLxdeQrFlWUw08j5L3VAxZB3P9Dm0EJbsDW5vJsi960";
    }
    public String getIdComercio() {
        return IdComercio;
    }

    public String getSid() {
        return sid;
    }
    public HashMap<String,String> leer(){
        HashMap<String,String> credencial=new HashMap<String,String>();
        credencial.put("sid",this.getSid());
        credencial.put("IdComercio",this.getIdComercio());
        return credencial;
    }
}
