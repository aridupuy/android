package com.cobrodigital.com.cobrodigital2.Webservice;

import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

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

    private static final String TIPO = "Dispositivo Android";

    public static void registrar(String token, String id_dispositivo) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, KeyManagementException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        metodo_web_service="registrar";
        array_a_enviar.clear();
        array_a_enviar.put("idComercio", CobroDigital.credencial.get_IdComercio());
        array_a_enviar.put("sid",CobroDigital.credencial.get_sid());
        array_a_enviar.put("token",token);
        array_a_enviar.put("tipo",TIPO);
        array_a_enviar.put("identificador_dispositivo",id_dispositivo);
        ejecutar();
    }
}
