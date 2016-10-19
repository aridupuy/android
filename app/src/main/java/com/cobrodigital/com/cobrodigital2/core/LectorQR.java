package com.cobrodigital.com.cobrodigital2.core;
import android.app.Activity;
import android.content.Context;
import com.cobrodigital.com.cobrodigital2.Model.Credencial;
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
