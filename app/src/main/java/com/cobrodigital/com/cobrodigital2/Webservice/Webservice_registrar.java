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
 * Created by ariel on 07/03/17.
 */

public class Webservice_registrar extends Webservice {
    public static void registrar(String token) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="registrar_device";
        array_a_enviar.clear();
        array_a_enviar.put("token",token);
        ejecutar();
    }
}
