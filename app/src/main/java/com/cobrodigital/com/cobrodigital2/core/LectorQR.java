package com.cobrodigital.com.cobrodigital2.core;
import android.app.Activity;
import android.content.Context;
import com.cobrodigital.com.cobrodigital2.Model.Credencial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class LectorQR extends Activity {
    Context context;
    public LectorQR(Context context){
        this.context=context;
    }
    public void leer(String content) throws JSONException {
        //JSONObject jsonObject = new JSONObject(content);
        //System.out.println(jsonObject.toString());
        //CobroDigital.credencial.set_IdComercio(jsonObject.getString("IdComercio").toString());
        //CobroDigital.credencial.set_sid(jsonObject.getString("sid"));
        JSONArray jsonArray = new JSONArray(content);
//        System.out.println(jsonObject.toString());
        JSONObject jsonObject=jsonArray.getJSONObject(0);
        System.out.println(jsonObject.toString());
        CobroDigital.credencial=new Credencial();
        CobroDigital.credencial.set_IdComercio(jsonObject.getString("idComercio"));
        jsonObject=jsonArray.getJSONObject(1);
        CobroDigital.credencial.set_sid(jsonObject.getString("sid"));
    }

}
