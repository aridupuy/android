package com.cobrodigital.com.cobrodigital2.Webservice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;

/**
 * Created by ariel on 12/01/17.
 */

public class Webservice_saldo extends Webservice {
    public static boolean consultar_saldo() throws MalformedURLException, IOException {
        try {
            metodo_web_service = "consultar_saldo";
            array_a_enviar.clear();
            ejecutar();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (obtener_resultado().equals("1")) {
            return true;
        }
        return false;
    }
}
