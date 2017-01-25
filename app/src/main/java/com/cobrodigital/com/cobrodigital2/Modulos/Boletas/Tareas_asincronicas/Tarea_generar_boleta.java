package com.cobrodigital.com.cobrodigital2.Modulos.Boletas.Tareas_asincronicas;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.View;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Boleta;
import com.cobrodigital.com.cobrodigital2.Modulos.Boletas.Boletas;
import com.cobrodigital.com.cobrodigital2.Modulos.Correo.Enviar_correo;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import java.util.Vector;

/**
 * Created by ariel on 25/01/17.
 */

public class Tarea_generar_boleta extends AsyncTask<String, Integer, Integer>{
    private int nro_boleta;
    public static final String NRO_BOLETA = "nro_boleta";
    private Boletas context;
    public Tarea_generar_boleta(Boletas context){
        this.context=context;
    }
    protected void generar_boleta(String identificador, String campo_a_buscar, String concepto, String fecha_1, String importe_1, String modelo, String fecha_2, String importe_2, String fecha_3, String importe_3){
        Looper.prepare();
        try {
            if((nro_boleta= CobroDigital.webservice.webservice_boleta.generar_boleta(identificador, campo_a_buscar, concepto, fecha_1, importe_1, modelo, fecha_2, importe_2, fecha_3, importe_3))!=-1) {
                Gestor_de_mensajes_usuario.mensaje("Boleta generada correctamente.",context.getApplicationContext());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Gestor_de_mensajes_usuario.mensaje(e.getMessage(),context);
        }

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        context.findViewById(R.id.progressbar_Boleta).setVisibility(View.VISIBLE);
        context.findViewById(R.id.vista_boletas).setVisibility(View.GONE);
    }

    @Override
    protected Integer doInBackground(String... params) {
        String identificador=params[0];
        String campo_a_buscar=params[1];
        String concepto=params[2];
        String fecha_1=params[3];
        String importe_1=params[4];
        String modelo=params[5];
        String fecha_2=params[6];
        String importe_2=params[7];
        String fecha_3=params[8];
        String importe_3=params[9];
        generar_boleta(identificador, campo_a_buscar, concepto, fecha_1, importe_1, modelo, fecha_2, importe_2, fecha_3, importe_3);
        return nro_boleta;
    }

    @Override
    protected void onPostExecute(Integer nro_boleta) {
        super.onPostExecute(nro_boleta);
        context.findViewById(R.id.progressbar_Boleta).setVisibility(View.GONE);
        Intent intent= new Intent(context,Enviar_correo.class);
        if(nro_boleta!=-1)
            intent.putExtra(NRO_BOLETA,nro_boleta);
        else{
            context.findViewById(R.id.vista_boletas).setVisibility(View.VISIBLE);
            Gestor_de_mensajes_usuario.mensaje(CobroDigital.webservice.obtener_log().toString());
            return;
        }
        context.startActivity(intent);
    }
}
