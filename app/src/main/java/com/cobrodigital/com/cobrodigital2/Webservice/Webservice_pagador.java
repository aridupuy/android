package com.cobrodigital.com.cobrodigital2.Webservice;

import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import java.util.LinkedHashMap;

/**
 * Created by ariel on 25/10/16.
 */

public class Webservice_pagador extends Webservice{
    public static String consultar_estructura_pagadores() throws Exception {
        metodo_web_service = "consultar_estructura_pagadores";
        array_a_enviar.put("idComercio", CobroDigital.credencial.get_IdComercio());
        array_a_enviar.put("sid",CobroDigital.credencial.get_sid());
        ejecutar();
        if (obtener_resultado().equals("0"))
            return  "1";
        return "0";
    }
    public static String existe_pagador(String identificador, String dato_a_buscar) throws Exception {
        metodo_web_service = "existe_pagador";
        array_a_enviar.put("idComercio", CobroDigital.credencial.get_IdComercio());
        array_a_enviar.put("sid",CobroDigital.credencial.get_sid());
        array_a_enviar.put("identificador", identificador);
        array_a_enviar.put("buscar", dato_a_buscar);
        ejecutar();
        return obtener_resultado();
    }
    public static String crear_pagador(LinkedHashMap nuevo_pagador) throws Exception {
        metodo_web_service = "crear_pagador";
        array_a_enviar.put("idComercio", CobroDigital.credencial.get_IdComercio());
        array_a_enviar.put("sid",CobroDigital.credencial.get_sid());
        array_a_enviar.put("pagador", nuevo_pagador);
        ejecutar();
        return obtener_resultado();
    }

    public static String editar_pagador(String identificador, String campo_a_buscar, LinkedHashMap nuevo_pagador) throws Exception {
        metodo_web_service = "editar_pagador";
        array_a_enviar.put("idComercio", CobroDigital.credencial.get_IdComercio());
        array_a_enviar.put("sid",CobroDigital.credencial.get_sid());
        array_a_enviar.put("identificador", identificador);
        array_a_enviar.put("buscar", campo_a_buscar);
        array_a_enviar.put("pagador", nuevo_pagador);
        ejecutar();
        return obtener_resultado();
    }


}
