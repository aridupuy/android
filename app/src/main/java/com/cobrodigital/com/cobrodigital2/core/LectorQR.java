package com.cobrodigital.com.cobrodigital2.core;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.util.JsonReader;
import android.widget.Toast;

import java.util.HashMap;

import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class LectorQR extends Activity {
    Context context;
    public LectorQR(Context context){
        this.context=context;
    }
    public Credencial leer(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        Credencial credencial = new Credencial(context);
        credencial.set_IdComercio(jsonObject.getString("IdComercio").toString());
        credencial.set_sid(jsonObject.getString("sid"));
        return credencial;
    }

}
