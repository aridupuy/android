package com.cobrodigital.com.cobrodigital2.Webservice;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;

/**
 * Created by ariel on 25/10/16.
 */

public class Webservice_transacciones extends Webservice{
    public static boolean consultar_transacciones(String fecha_desde, String fecha_hasta, LinkedHashMap filtros,int offset,int limit) throws MalformedURLException, IOException {
        try {
            metodo_web_service = "consultar_transacciones";
            array_a_enviar.put("desde", fecha_desde);
            array_a_enviar.put("hasta", fecha_hasta);
            if(offset!=0 && limit!=0){
                array_a_enviar.put("offset", offset);
                array_a_enviar.put("limit", limit);
            }
            if (!filtros.isEmpty())
                array_a_enviar.put("filtros", filtros);
            ejecutar();
            //quito los filtros para que la variable no se quede con basura cada vez
            if (!filtros.isEmpty())
                array_a_enviar.remove("filtros");
        } catch (Exception e) {
            System.out.println(e);
        }
        if (obtener_resultado().equals("1")) {
            return true;
        }
        return false;
    }
}
