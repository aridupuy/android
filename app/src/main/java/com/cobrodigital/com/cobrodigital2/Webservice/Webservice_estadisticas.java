package com.cobrodigital.com.cobrodigital2.Webservice;


import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Webservice_estadisticas extends Webservice {
    public static void obtener_estadisticas_hoy_ingresos() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="estadisticas_tenencias";
        Date fecha_desde=new Date();
        Date fecha_hasta=new Date();
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        array_a_enviar.clear();
        array_a_enviar.put("sentido_transaccion","ingreso");
        array_a_enviar.put("fecha_hasta",format.format(fecha_hasta));
        array_a_enviar.put("fecha_desde",format.format(fecha_desde));
        ejecutar();
    }
    public static void obtener_estadisticas_semana_ingresos() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="estadisticas_tenencias";
        Date fecha_hasta=new Date();
        Calendar fecha_desde= GregorianCalendar.getInstance();
        fecha_desde.add(Calendar.DATE,-7);
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        array_a_enviar.clear();
        array_a_enviar.put("sentido_transaccion","ingreso");
        array_a_enviar.put("fecha_hasta",format.format(fecha_hasta.getTime()));
        array_a_enviar.put("fecha_desde",format.format(fecha_desde));
        ejecutar();
    }
    public static void obtener_estadisticas_mes_ingresos() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="estadisticas_tenencias";
        Date fecha_hasta=new Date();
        Calendar fecha_desde= Calendar.getInstance();
        fecha_desde.add(Calendar.MONTH,-1);
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        array_a_enviar.clear();
        array_a_enviar.put("sentido_transaccion","ingreso");
        array_a_enviar.put("fecha_hasta",format.format(fecha_hasta.getTime()));
        array_a_enviar.put("fecha_desde",format.format(fecha_desde.getTime()));
        ejecutar();
    }
    public static void obtener_estadisticas_hoy_egresos() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="estadisticas_tenencias";
        Date fecha_desde=new Date();
        Date fecha_hasta=new Date();
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        array_a_enviar.clear();
        array_a_enviar.put("sentido_transaccion","egreso");
        array_a_enviar.put("fecha_hasta",format.format(fecha_hasta));
        array_a_enviar.put("fecha_desde",format.format(fecha_desde));
        ejecutar();
    }
    public static void obtener_estadisticas_semana_egresos() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="estadisticas_tenencias";
        Date fecha_hasta=new Date();
        Calendar fecha_desde= GregorianCalendar.getInstance();
        fecha_desde.add(Calendar.DAY_OF_MONTH,-7);
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        array_a_enviar.clear();
        array_a_enviar.put("sentido_transaccion","egreso");
        array_a_enviar.put("fecha_hasta",format.format(fecha_hasta));
        array_a_enviar.put("fecha_desde",format.format(fecha_desde.getTime()));
        ejecutar();
    }
    public static void obtener_estadisticas_mes_egresos() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="estadisticas_tenencias";
        Date fecha_hasta=new Date();
        Calendar fecha_desde= GregorianCalendar.getInstance();
        fecha_desde.add(Calendar.DAY_OF_MONTH,-30);
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        array_a_enviar.clear();
        array_a_enviar.put("sentido_transaccion","egreso");
        array_a_enviar.put("fecha_hasta",format.format(fecha_hasta.getTime()));
        array_a_enviar.put("fecha_desde",format.format(fecha_desde));
        ejecutar();
    }
}
