package com.cobrodigital.com.cobrodigital2.core;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.util.JsonReader;
import android.widget.Toast;

import java.util.HashMap;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class LectorQR extends Activity {
    private String sid="";
    private String IdComercio="";
    public String getIdComercio() {
        return IdComercio;
    }
    public String getSid() {
        return sid;
    }
    public HashMap<String,String> leer(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        this.IdComercio = jsonObject.getString("IdComercio").toString();
        this.sid = jsonObject.getString("sid");
        HashMap<String,String> credencial=new HashMap<String,String>();
        credencial.put("IdComercio",IdComercio);
        credencial.put("sid",sid);
        return credencial;
    }

}
