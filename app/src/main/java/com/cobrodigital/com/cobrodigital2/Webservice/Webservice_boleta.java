package com.cobrodigital.com.cobrodigital2.Webservice;

import java.util.Vector;

/**
 * Created by ariel on 25/10/16.
 */

public class Webservice_boleta extends Webservice {
    public static String generar_boleta(String identificador, String campo_a_buscar, String concepto, String fecha_1, String importe_1, String modelo, String fecha_2, String importe_2, String fecha_3, String importe_3) throws Exception {

        metodo_web_service = "generar_boleta";
        array_a_enviar.put("identificador", identificador);
        array_a_enviar.put("buscar", campo_a_buscar);
        String fechas[] = null;
        String importes[] = null;
        fechas[0] = fecha_1;
        importes[0] = importe_1;
        if (modelo != "") {
            array_a_enviar.put("modelo", modelo);
        }
        if (fecha_2 != null) {
            fechas[1] = fecha_2;
        }
        if (fecha_3 != null) {
            fechas[2] = fecha_3;
        }
        if (importe_2 != null) {
            importes[1] = importe_2;
        }
        if (importe_3 != null) {
            importes[2] = importe_3;
        }
        array_a_enviar.put("concepto", concepto);
        array_a_enviar.put("fechas_vencimiento", fechas);
        array_a_enviar.put("importes", importes);
        ejecutar();
        if (obtener_resultado() == "1") {
            String[] nro_boletas;
            Vector boletas = obtener_datos();
            nro_boletas = (String[]) boletas.toArray();
            return nro_boletas[0];
        }
        return null;
    }
    public static Vector cancelar_boleta(int nro_boleta) throws Exception {
        metodo_web_service = "cancelar_boleta";
        array_a_enviar.put("nro_boleta", nro_boleta);
        ejecutar();
        return obtener_log();
    }
    public static Object obtener_codigo_de_barras(int nro_boleta) throws Exception {
        metodo_web_service = "obtener_codigo_de_barras";
        array_a_enviar.put("nro_boleta", nro_boleta);
        ejecutar();
        return obtener_resultado();
    }
}
