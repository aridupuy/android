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
 * Created by ariel on 03/03/17.
 */

public class Webservice_comision extends Webservice {

    public static final String MONTO = "monto";
    public static final String MEDIO_DE_PAGO = "medio_de_pago";
    public static final int ID_MP = 110;

    public static void obtener_comision_retiros(Double monto) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        array_a_enviar.clear();
        metodo_web_service="calculo_de_comision";
        array_a_enviar.put(MONTO,monto);
        array_a_enviar.put(MEDIO_DE_PAGO, ID_MP);
        ejecutar();
    }
}
