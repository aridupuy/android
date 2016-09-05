package com.cobrodigital.com.cobrodigital2.core;

//import com.sun.jndi.toolkit.url.UrlUtil;

import android.os.StrictMode;

import org.json.JSONArray;
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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;

public class CobroDigital {
    public static HashMap<String, String> credencial;
    private static final String URL  = "https://www.cobrodigital.com:14365/ws3/";
    public static String sid="";
    public static String idComercio="";
    public LinkedHashMap resultado = new LinkedHashMap();
    protected String metodo_web_service = "";
    protected String method = "POST";
    protected LinkedHashMap<Object, Object> array_a_enviar = new LinkedHashMap();
//    protected String idComercio = null;
//    protected String sid = null;


    public CobroDigital(HashMap<String,String> credencial) throws Exception{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (idComercio == "" ) {
            idComercio=credencial.get("IdComercio");
        }
        if (sid == "") {
            sid=credencial.get("sid");
        }
    }
    public CobroDigital(String idComercio, String sid) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        if (idComercio == "") {
            throw new Exception("No definio idComercio");
        }
        if (sid == "") {
            throw new Exception("No definio sid");
        }
        this.idComercio = idComercio;
        this.sid = sid;
    }

    //Funciones que dan una interfaz al usuario///
    public String crear_pagador(LinkedHashMap nuevo_pagador) throws Exception {
        this.metodo_web_service = "crear_pagador";
        this.array_a_enviar.put("pagador", nuevo_pagador);
        System.out.println(this.array_a_enviar.toString());
        this.ejecutar();
        return this.obtener_resultado();
    }

    public String editar_pagador(String identificador, String campo_a_buscar, LinkedHashMap nuevo_pagador) throws Exception {
        this.metodo_web_service = "editar_pagador";
        this.array_a_enviar.put("identificador", identificador);
        this.array_a_enviar.put("buscar", campo_a_buscar);
        this.array_a_enviar.put("pagador", nuevo_pagador);
        this.ejecutar();
        return this.obtener_resultado();
    }

    public String existe_pagador(String identificador, String dato_a_buscar) throws Exception {
        this.metodo_web_service = "existe_pagador";
        this.array_a_enviar.put("identificador", identificador);
        this.array_a_enviar.put("buscar", dato_a_buscar);
        this.ejecutar();
        return this.obtener_resultado();
    }

    public String generar_boleta(String identificador, String campo_a_buscar, String concepto, String fecha_1, String importe_1, String modelo, String fecha_2, String importe_2, String fecha_3, String importe_3) throws Exception {

        this.metodo_web_service = "generar_boleta";
        this.array_a_enviar.put("identificador", identificador);
        this.array_a_enviar.put("buscar", campo_a_buscar);
        String fechas[] = null;
        String importes[] = null;
        fechas[0] = fecha_1;
        importes[0] = importe_1;
        if (modelo != "") {
            this.array_a_enviar.put("modelo", modelo);
        }
        if (fecha_2 != null) {
            fechas[1] = fecha_2;
        }
        if (fecha_3 != null) {
            fechas[2] = fecha_3;
        }
        if (importe_2 != null) {
            importes[1] = importe_2;
        }
        if (importe_3 != null) {
            importes[2] = importe_3;
        }
        this.array_a_enviar.put("concepto", concepto);
        this.array_a_enviar.put("fechas_vencimiento", fechas);
        this.array_a_enviar.put("importes", importes);
        this.ejecutar();
        if (this.obtener_resultado() == "1") {
            String[] nro_boletas;
            Vector boletas = this.obtener_datos();
            nro_boletas = (String[]) boletas.toArray();
            return nro_boletas[0];
        }
        return null;
    }

    public boolean consultar_transacciones(String fecha_desde, String fecha_hasta, LinkedHashMap filtros) throws MalformedURLException, IOException {
        try {
            metodo_web_service = "consultar_transacciones";
            System.out.println(array_a_enviar.toString());
            array_a_enviar.put("desde", fecha_desde);
            array_a_enviar.put("hasta", fecha_hasta);
            if (!filtros.isEmpty())
                array_a_enviar.put("filtros", filtros);
            ejecutar();
        } catch (Exception e) {
//            System.out.println("hola error");
            System.out.println(e.getLocalizedMessage());
        }
        if (obtener_resultado() == "1") {
            return true;
        }
        return false;
    }

    public Vector cancelar_boleta(int nro_boleta) throws Exception {
        this.metodo_web_service = "cancelar_boleta";
        this.array_a_enviar.put("nro_boleta", nro_boleta);
        this.ejecutar();
        return this.obtener_log();
    }

    public Object obtener_codigo_de_barras(int nro_boleta) throws Exception {
        this.metodo_web_service = "obtener_codigo_de_barras";
        this.array_a_enviar.put("nro_boleta", nro_boleta);
        this.ejecutar();
        return this.obtener_resultado();
    }

    //Fin de funciones/////
    private void ejecutar(String metodo_webservice, LinkedHashMap array) throws UnsupportedEncodingException, MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException, Exception {
        System.out.println("ejecutar con parametros");
        this.array_a_enviar.put("idComercio", this.idComercio);
        this.array_a_enviar.put("sid", this.sid);
        if (metodo_webservice == null) {
            metodo_webservice = this.metodo_web_service;
        }
        Map<String, Object> array_a_enviar;
        if (array == null) {
            array_a_enviar = (LinkedHashMap) this.array_a_enviar;
        } else {
            array_a_enviar = (LinkedHashMap) array;
        }
        array_a_enviar.put("metodo_webservice", metodo_webservice);
        enviar_https(URL, array_a_enviar);
    }

    private void enviar_https(String httpsurl, Map<?, ?> array_a_enviar) throws IOException, Exception {
        URL myurl = new URL(httpsurl);
        HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        String url_parameters = http_build_query(array_a_enviar);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(url_parameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + httpsurl);
        System.out.println("Post parameters : " + url_parameters);
        System.out.println("Response Code : " + responseCode);
        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
        String response = "";
        while ((inputLine = in.readLine()) != null) {
            response += inputLine;
        }
        System.out.println(response);
        JSONObject jo = new JSONObject(response);
        JSONArray logs = jo.getJSONArray("log");
        Vector log = new Vector();
        Vector dato = new Vector();
        if (jo.has("datos")) {
            JSONArray datos = jo.getJSONArray("datos");

            for (int i = 0; i < datos.length(); i++) {
                dato.add(i, (String) datos.get(i));
            }
        }
        if (jo.has("log")) {
            System.out.println("tamaño" + logs.length());
            for (int j = 0; j < logs.length(); j++) {
                String logstring = logs.getString(j);
                log.add(j, (logstring));
                System.out.println(log.toString());
            }
            System.out.println("loooooog");
        }
        this.resultado.put("ejecucion_correcta", jo.get("ejecucion_correcta"));
        this.resultado.put("log", log);
        this.resultado.put("datos", dato);
        in.close();
    }

    public void ejecutar() throws Exception {
        ejecutar(metodo_web_service, (LinkedHashMap) array_a_enviar);
    }

    private String http_build_query(Map<?, ?> data) throws Exception {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            if (entry.getValue().getClass().getName() == "java.util.LinkedHashMap") {
                LinkedHashMap array = (LinkedHashMap) entry.getValue();
                for (Iterator it = array.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<?, ?> row = (Map.Entry<?, ?>) it.next();

                    queryString.append(String.format("%s=%s", entry.getKey() + "%5B" + row.getKey() + "%5D", urlEncodeUTF8(row.getValue().toString()) + "&"));
                }
            } else {
                queryString.append(String.format("%s=%s",
                        urlEncodeUTF8(entry.getKey().toString()),
                        urlEncodeUTF8(entry.getValue().toString())
                ));
            }
        }
        return queryString.toString();
    }

    private String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public Vector obtener_datos() {
        String ejecucion_correcta = (String) this.resultado.get("ejecucion_correcta");
        if (ejecucion_correcta == null)
            ejecucion_correcta = "0";
        if (Integer.parseInt(ejecucion_correcta) == 1) {
            return (Vector) this.resultado.get("datos");
        }
        return null;
    }

    public String obtener_resultado() {
        return (String) this.resultado.get("ejecucion_correcta");
    }

    public Vector obtener_log() {
        return (Vector) this.resultado.get("log");
    }
}