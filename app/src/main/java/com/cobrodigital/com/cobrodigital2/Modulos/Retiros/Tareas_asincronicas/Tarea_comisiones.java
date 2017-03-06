package com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Tareas_asincronicas;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Comision;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ariel on 03/03/17.
 */

public class Tarea_comisiones extends AsyncTask<Double,Void,Vector<Comision>> {
    private Activity context;
//    private View view;
    public Tarea_comisiones(Activity context){
        this.context=context;
//        this.view=view;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        context.findViewById(R.id.progressbar_retiro_confirmacion).setVisibility(View.VISIBLE);
        context.findViewById(R.id.contenido_confirmacion_retiro).setVisibility(View.GONE);
    }
    @Override
    protected Vector<Comision> doInBackground(Double... doubles) {
        Double monto=doubles[0];
        Vector<Comision> comisiones=new Vector<Comision>();
        try {
            CobroDigital.webservice.webservice_comision.obtener_comision_retiros(monto);
            if (CobroDigital.webservice.obtener_resultado().equals("1")) {
                Object transicion[] = CobroDigital.webservice.obtener_datos().toArray();
                if (transicion.length > 0) {
                    for (Object dato: transicion) {
                        Comision comision=new Comision((String) dato);
                        comisiones.add(comision);
                    }
                } else {
                    Log.e("Tarea_bancos",CobroDigital.webservice.obtener_log().toString());
                    Gestor_de_mensajes_usuario.mensaje("Ha ocurrido un error al ",context.getApplicationContext());
                    return comisiones;
                }
            }
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comisiones;
    }
    protected void onPostExecute(Vector<Comision> comisiones){
        super.onPostExecute(comisiones);
        for (Comision comision: comisiones) {
            Double monto_pagador=Double.parseDouble(comision.getMonto_pagador());
            Double monto_marchand=Double.parseDouble(comision.getMonto_marchand());
            ((TextView)context.findViewById(R.id.Importe_a_acreditar)).setText("$"+String.valueOf(monto_pagador));
            ((TextView)context.findViewById(R.id.Comision)).setText("$"+String.valueOf((monto_marchand-monto_pagador)));
            ((TextView)context.findViewById(R.id.Importe)).setText("$"+String.valueOf(monto_marchand));
        }
        context.findViewById(R.id.contenido_confirmacion_retiro).setVisibility(View.VISIBLE);
        context.findViewById(R.id.progressbar_retiro_confirmacion).setVisibility(View.GONE);

    }



}
