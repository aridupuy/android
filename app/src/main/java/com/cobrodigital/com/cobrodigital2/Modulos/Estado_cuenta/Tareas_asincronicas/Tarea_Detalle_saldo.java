package com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Tareas_asincronicas;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Fragment.Detalle_saldo_fragment;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

public class Tarea_Detalle_saldo extends AsyncTask <Void,Void,HashMap<String,String>>{
public static final String SALDO = "saldo";
    protected Fragment context;
    protected View view;
    public Tarea_Detalle_saldo(Detalle_saldo_fragment context,View view){
        this.context=context;
        this.view=view;

    }
    @Override
    protected void onPreExecute() {
        this.view.findViewById(R.id.progressbar_fragment_detalle_saldo).setVisibility(View.VISIBLE);
        this.view.findViewById(R.id.contenido_detalle_saldo).setVisibility(View.GONE);
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
                return Resultado;
            }
        }catch (javax.net.ssl.SSLHandshakeException ex){
            Gestor_de_mensajes_usuario.dialogo("No podemos procesar la solicitud, intente mas tarde",context.getActivity());
        }
        catch (JSONException e) {e.printStackTrace();}
        catch (Exception e){e.printStackTrace();}
        return null;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> Resultado) {
        if(Resultado!=null){
            Double saldo = Double.parseDouble(Resultado.get("saldo"));
            Double saldo_disponible = Double.parseDouble((Resultado.get("saldo_disponible")!=null)?Resultado.get("saldo_disponible"):"0");
            Double aun_no_liquidado = Double.parseDouble((Resultado.get("aun_no_liquidado") !=null)?Resultado.get("aun_no_liquidado") :"0");
            Double encaje = Double.parseDouble((Resultado.get("encaje") !=null)?Resultado.get("encaje") :"0");
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat("###,###,###,###.##", otherSymbols);
            df.setDecimalSeparatorAlwaysShown(true);
//            df.setDecimalSeparatorAlwaysShown(true);
            df.setMinimumFractionDigits(2);
            ((TextView)this.view.findViewById(R.id.disponible)).setText("$"+df.format(saldo_disponible));
            ((TextView)this.view.findViewById(R.id.enCuenta)).setText("$"+df.format(saldo));
            ((TextView)this.view.findViewById(R.id.liq)).setText("$"+df.format(aun_no_liquidado));
            ((TextView)this.view.findViewById(R.id.encaje)).setText("$"+df.format(encaje));
            this.view.findViewById(R.id.progressbar_fragment_detalle_saldo).setVisibility(View.GONE);
            this.view.findViewById(R.id.contenido_detalle_saldo).setVisibility(View.VISIBLE);
        }
        else {
            Gestor_de_mensajes_usuario.dialogo("Ha ocurrido un error de comunicación, intente mas tarde.",context.getActivity());
//            context.getActivity().finish();
        }
    }

}
