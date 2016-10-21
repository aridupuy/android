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
    public void leer(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        CobroDigital.credencial= new Credencial(context);
        CobroDigital.credencial.set_IdComercio(jsonObject.getString("IdComercio").toString());
        CobroDigital.credencial.set_sid(jsonObject.getString("sid"));
    }

}
