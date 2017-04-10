package com.cobrodigital.com.cobrodigital2.Webservice;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Webservice_cobrar_por_correo extends Webservice {

    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";
    public static final String CORREO = "correo";
    public static final String DOCUMENTO = "documento";
    public static final String DIRECCION = "direccion";
    public static final String CONCEPTO = "concepto";
    public static final String VENCIMIENTO = "vencimiento";
    public static final String IMPORTE = "importe";
    public static final String METODO_WEB_SERVICE = "cobrar_por_correo";

    public static boolean cobrar_por_correo(String nombre , String apellido , String correo, String documento , String direccion, String concepto, String vencimiento, String importe) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        Webservice.array_a_enviar.clear();
        Webservice.array_a_enviar.put(NOMBRE,nombre);
        Webservice.array_a_enviar.put(APELLIDO,apellido);
        Webservice.array_a_enviar.put(CORREO,correo);
        Webservice.array_a_enviar.put(DOCUMENTO,documento);
        Webservice.array_a_enviar.put(DIRECCION,direccion);
        Webservice.array_a_enviar.put(CONCEPTO,concepto);
        Webservice.array_a_enviar.put(VENCIMIENTO,vencimiento);
        Webservice.array_a_enviar.put(IMPORTE,importe);
        Webservice.metodo_web_service= METODO_WEB_SERVICE;
        if(ejecutar())
            return true;
        return false;
    }
}
