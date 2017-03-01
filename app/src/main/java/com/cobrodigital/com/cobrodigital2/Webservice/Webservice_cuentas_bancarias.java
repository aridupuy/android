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
 * Created by ariel on 21/02/17.
 */

public class Webservice_cuentas_bancarias extends Webservice {

    public static void obtener_cuentas_bancarias() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="cuentas_bancarias";
        ejecutar();
    }

}
