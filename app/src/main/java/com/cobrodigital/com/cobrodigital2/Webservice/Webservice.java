package com.cobrodigital.com.cobrodigital2.Webservice;

import android.util.Log;

import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.field.types.StringType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ariel on 25/10/16.
 */

public abstract class Webservice {
    public static Webservice_boleta webservice_boleta;
    public static Webservice_pagador webservice_pagador;
    public static Webservice_transacciones webservice_transacciones;
    public static Webservice_saldo webservice_saldo;
    public static Webservice_enviar_correo webservice_enviar_correo;

    private static final String URL  = "https://www.cobrodigital.com:14365/ws3/";
    protected static final String method = "POST";
    protected static String metodo_web_service = "";
    protected static LinkedHashMap<Object, Object> array_a_enviar = new LinkedHashMap();
    protected static LinkedHashMap resultado = new LinkedHashMap();

    private static void ejecutar(String metodo_webservice, LinkedHashMap array) throws IOException, KeyManagementException, NoSuchAlgorithmException, JSONException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        array_a_enviar.put("idComercio", CobroDigital.credencial.get_IdComercio());
        array_a_enviar.put("sid", CobroDigital.credencial.get_sid());
        if (metodo_webservice == null) {
            metodo_webservice = metodo_web_service;
        }
        Map<String, Object> array_a_procesar;
        if (array == null) {
            array_a_procesar = (LinkedHashMap) array_a_enviar;
        } else {
            array_a_procesar= (LinkedHashMap) array;
        }
        array_a_procesar.put("metodo_webservice", metodo_webservice);
        enviar_https(URL, array_a_procesar);
    }

    private static void enviar_https(String httpsurl, Map<?, ?> array_a_enviar) throws IOException, JSONException {
        URL myurl = new URL(httpsurl);
        HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-type", "application/json");
        JSONObject json=new JSONObject(array_a_enviar);
        String url_parameters=json.toString();
        System.out.println(url_parameters);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(url_parameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
        String response = "";
        while ((inputLine = in.readLine()) != null) {
            response += inputLine;
        }
        Log.d("datos recibidos",response);
        JSONObject jo = new JSONObject(response);
        Vector log = new Vector();
        Vector dato = new Vector();
        if (jo.has("datos")) {
            JSONArray datos = jo.getJSONArray("datos");

            for (int i = 0; i < datos.length(); i++) {
                dato.add(i, (String) datos.get(i));
            }
        }
        if (jo.has("log")) {
            JSONArray logs=jo.getJSONArray("log");
            for (int j = 0; j < logs.length(); j++) {
                String logstring = logs.getString(j);
                log.add(j, (logstring));
            }
        }
        resultado.clear();
        resultado.put("ejecucion_correcta", jo.get("ejecucion_correcta"));
        resultado.put("log", log);
        resultado.put("datos", dato);
        in.close();
    }

    public static void ejecutar() throws NoSuchAlgorithmException, IOException, KeyManagementException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, JSONException {
        ejecutar(metodo_web_service, (LinkedHashMap) array_a_enviar);
    }

    private static String http_build_query(Map<?, ?> data) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            if(entry.getValue() instanceof String []) {
                for (String valor : (String[]) entry.getValue()) {
                    queryString.append(String.format("%s=%s", entry.getKey() + "%5B" + valor + "%5D", urlEncodeUTF8(valor) + "&"));
                }
            }
            else if (entry.getValue().getClass().getName() == "java.util.LinkedHashMap") {
                LinkedHashMap array = (LinkedHashMap) entry.getValue();
                for (Iterator it = array.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<?, ?> row = (Map.Entry<?, ?>) it.next();
                    queryString.append(String.format("%s=%s", entry.getKey() + "%5B" + row.getKey() + "%5D", urlEncodeUTF8(row.getValue().toString()) + "&"));
                }
            }
            else {
                queryString.append(String.format("%s=%s",
                        urlEncodeUTF8(entry.getKey().toString()),
                        urlEncodeUTF8(entry.getValue().toString())
                ));
            }
        }
        return queryString.toString();
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    public static Vector obtener_datos() {
        return (Vector) resultado.get("datos");
    }
    public static String obtener_resultado() {
        try {
            if (resultado.size() > 0 && (((String) resultado.get("ejecucion_correcta")).equals("1")))
                return "1";
            return "0";
        }catch (ClassCastException e){
             try {
             if (resultado.size() > 0 && ((Boolean) (resultado.get("ejecucion_correcta"))))
                 return "1";
             return "0";
         }catch (ClassCastException ex){
             if (resultado.size() > 0)
                 return "1";
             return "0";
         }
        }
    }
    public static Vector obtener_log() {
        return (Vector) resultado.get("log");
    }
}
