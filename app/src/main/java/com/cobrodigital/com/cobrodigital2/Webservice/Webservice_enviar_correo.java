package com.cobrodigital.com.cobrodigital2.Webservice;

import java.util.HashMap;

/**
 * Created by ariel on 23/01/17.
 */

public class Webservice_enviar_correo extends Webservice {
    public static boolean enviar_boleta_correo(String destinatario, String asunto,String mensaje,int nro_boleta) throws Exception {
        metodo_web_service = "enviar_boleta_correo";
        HashMap map =new HashMap();
        map.put("destinatario",destinatario);
        map.put("asunto",asunto);
        map.put("mensaje",mensaje);
        map.put("nro_boleta",nro_boleta);
        array_a_enviar.clear();
        array_a_enviar.put("datos",map);
        ejecutar();
        if (obtener_resultado().equals("1")) {
            return true;
        }
        return false;
    }
}
