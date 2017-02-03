package com.cobrodigital.com.cobrodigital2.Webservice;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ariel on 02/02/17.
 */

public class Webservice_detalle_saldo extends Webservice {
    public static void obtener_detalle_hoy() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        array_a_enviar.clear();
        metodo_web_service="detalle_saldo";
        ejecutar();
    }

}
