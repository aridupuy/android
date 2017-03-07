package com.cobrodigital.com.cobrodigital2.Webservice;


import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ariel on 06/03/17.
 */

public class Webservice_retiro extends Webservice {
    public static void realizar_peticion(String cuit,String titular,Double plata,String nombre) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        array_a_enviar.clear();
        array_a_enviar.put("cuit",cuit);
        array_a_enviar.put("titular",titular);
        array_a_enviar.put("plata",plata);
        array_a_enviar.put("nombre",nombre);
        metodo_web_service="peticion_retiro";
        ejecutar();
    }
}
