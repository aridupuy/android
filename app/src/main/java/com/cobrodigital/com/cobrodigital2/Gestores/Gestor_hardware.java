package com.cobrodigital.com.cobrodigital2.Gestores;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ariel on 16/02/17.
 */

public class Gestor_hardware extends Activity{

    public static HashMap<String,Double> obtener_resolucion_pantalla(Activity activity) {
        HashMap<String,Double> resolucion=new HashMap<String,Double>();
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics= activity.getResources().getDisplayMetrics();

        Point size = new Point();
        display.getSize(size);
        resolucion.put("ancho",Double.parseDouble(size.x+""));
        resolucion.put("alto",Double.parseDouble(size.y+""));
//        Double dencidad=Math.sqrt(Math.pow(size.x,2)+Math.pow(size.y,2));
//        resolucion.put("Dencity",dencidad); //en pixels
        resolucion.put("Dencity",(metrics.density*160d)); //en dp

        return resolucion;
    }
    public static Double calcular_tamaño_fuente(HashMap<String,Double> resolucion,int tamaño_fuente){
        Double alto,ancho,densidad;
        alto=resolucion.get("ancho");
        ancho=resolucion.get("alto");
        densidad=resolucion.get("Dencity");
        Log.d("mensaje","alto: "+alto+" ancho: "+ancho+" dencidad: "+densidad);
//        Double tamaño =  ancho/(densidad/160);
//        return tamaño;
        Double fuente=(int) tamaño_fuente * (densidad/160d);
        return fuente;
    }
}
