package com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Tareas_asincronicas;

import android.os.AsyncTask;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Detalle_saldo;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Tarea_Detalle_saldo extends AsyncTask <Void,Void,HashMap<String,String>>{
    protected  Detalle_saldo context;
    public Tarea_Detalle_saldo(Detalle_saldo context){
        this.context=context;
    }

    @Override
    protected HashMap<String,String> doInBackground(Void... voids) {
        HashMap<String,String> Resultado=new HashMap<String,String>();
        try {
            CobroDigital.webservice.webservice_detalle_saldo.obtener_detalle_hoy();
            if (CobroDigital.webservice.obtener_resultado().equals("1")) {
                Object transicion[] = CobroDigital.webservice.webservice_detalle_saldo.obtener_datos().toArray();
                if (transicion.length > 0) {
                    JSONObject Json= new JSONObject((String) transicion[0]);
                    Resultado.put("saldo",Json.getString("saldo"));
                    Resultado.put("aun_no_liquidado",Json.getString("aun_no_liquidado"));
                    Resultado.put("is_daut",Json.getString("is_daut"));
                    Resultado.put("is_tc", Json.getString("is_tc"));
                    Resultado.put("saldo_disponible",Json.getString("saldo_disponible"));
                    Resultado.put("encaje",Json.getString("encaje"));
                }
            }
        }catch (JSONException e) {e.printStackTrace();}
        catch (Exception e){e.printStackTrace();}
        return Resultado;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> Resultado) {
        Double saldo = Double.parseDouble(Resultado.get("saldo"));
        Double saldo_disponible = Double.parseDouble(Resultado.get("saldo_disponible"));
        Double aun_no_liquidado = Double.parseDouble(Resultado.get("aun_no_liquidado"));
        Double encaje = Double.parseDouble(Resultado.get("encaje"));
        DecimalFormat df = new DecimalFormat("#.##");
        ((TextView)context.findViewById(R.id.disponible)).setText("$"+df.format(saldo_disponible));
        ((TextView)context.findViewById(R.id.enCuenta)).setText("$"+df.format(saldo));
        ((TextView)context.findViewById(R.id.liq)).setText("$"+df.format(aun_no_liquidado));
        ((TextView)context.findViewById(R.id.encaje)).setText("$"+df.format(encaje));
    }
}
