package com.cobrodigital.com.cobrodigital2.Webservice;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.google.gson.Gson;
import com.google.zxing.client.result.URIResultParser;
import com.google.zxing.client.result.VCardResultParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.util.List;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ariel on 25/10/16.
 */

public class Webservice_boleta extends Webservice {
    public static int generar_boleta(String identificador, String campo_a_buscar, String concepto, String fecha_1, String importe_1, String modelo, String fecha_2, String importe_2, String fecha_3, String importe_3) throws Exception {
        metodo_web_service = "generar_boleta";
        array_a_enviar.clear();
        array_a_enviar.put("identificador", identificador);
        array_a_enviar.put("buscar", campo_a_buscar);
        Vector<String> temp_fecha = new Vector<String>();
        Vector<String> temp_importe = new Vector<String>();
        temp_fecha.add(fecha_1);
        temp_importe.add(importe_1);
        int cantidad=1;
        if (modelo != "") {
            array_a_enviar.put("plantilla", modelo);
        }
        if (fecha_2 != null && importe_2 != null ) {
                temp_fecha.add(fecha_2);
                temp_importe.add(importe_2);
        }

        if (fecha_3 != null&& importe_3 != null) {
            temp_fecha.add(fecha_3);
            temp_importe.add(importe_3);
        }

        String fechas[]=new String[temp_fecha.size()];
        String importes[]=new String[temp_importe.size()];
        for (int i=0;i<temp_fecha.size();i++) {
            fechas[i]=temp_fecha.get(i);
        }
        for (int i=0;i<temp_importe.size();i++) {
            importes[i]=temp_importe.get(i);
        }
        array_a_enviar.put("concepto", concepto);
        array_a_enviar.put("fechas_vencimiento", fechas);
        array_a_enviar.put("importes", importes);
        ejecutar();
        if (obtener_resultado() == "1") {
            Object[] nro_boletas;
            Vector boletas = obtener_datos();
            nro_boletas = boletas.toArray();
            return Integer.parseInt(((String)nro_boletas[0]));
        }
        return -1;
    }
    public static Vector cancelar_boleta(int nro_boleta) throws Exception {
        metodo_web_service = "cancelar_boleta";
        array_a_enviar.put("nro_boleta", nro_boleta);
        ejecutar();
        return obtener_log();
    }
    public static Object obtener_codigo_de_barras(int nro_boleta) throws Exception {
        metodo_web_service = "obtener_codigo_de_barras";
        array_a_enviar.put("nro_boleta", nro_boleta);
        ejecutar();
        return obtener_resultado();
    }
    public static String obtener_boleta_html(int nro_boleta) throws JSONException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        String html = "";
        metodo_web_service = "obtener_boleta_html";
        array_a_enviar.clear();
        array_a_enviar.put("nro_boleta", nro_boleta);
        ejecutar();
        if (CobroDigital.webservice.obtener_resultado().equals("1")) {
            Object transicion[] = CobroDigital.webservice.obtener_datos().toArray();
            if (transicion.length > 0) {
                html = ((String) transicion[0]).toString();
            } else {
                System.out.println("No hay Boleta disponibles");
                return null;
            }
        }
        Log.d("Boleta",html);
        return html;
    }
}
