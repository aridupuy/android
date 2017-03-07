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
import java.math.BigDecimal;
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
    private Double monto;
    private Double disponible;
//    private View view;
    public Tarea_comisiones(Activity context){
        this.context=context;
//        this.view=view;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        context.findViewById(R.id.progressbar_retiro_confirmacion).setVisibility(View.VISIBLE);
        context.findViewById(R.id.mensaje_linear).setVisibility(View.GONE);
        context.findViewById(R.id.contenido_confirmacion_retiro).setVisibility(View.GONE);
    }
    @Override
    protected Vector<Comision> doInBackground(Double... doubles) {
        monto=doubles[0];
        disponible=doubles[1];
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
        if(comisiones.size()>0) {
            for (Comision comision : comisiones) {
                if (this.validar(comision, monto)) {
                    Double monto_pagador_double = Double.parseDouble(comision.getMonto_pagador());
                    Double monto_marchand_double = Double.parseDouble(comision.getMonto_marchand());
                    BigDecimal monto_pagador = new BigDecimal(monto_pagador_double);
                    BigDecimal monto_marchand = new BigDecimal(monto_marchand_double);
                    ((TextView) context.findViewById(R.id.Importe_a_acreditar)).setText("$" + monto_pagador.toString());
                    ((TextView) context.findViewById(R.id.Comision)).setText("$" + String.valueOf((monto_marchand_double- monto_pagador_double)));
                    ((TextView) context.findViewById(R.id.Importe)).setText("$" + monto_marchand.toString());
                } else {
                    context.findViewById(R.id.contenido_confirmacion_retiro).setVisibility(View.VISIBLE);
                    context.findViewById(R.id.mensaje_linear).setVisibility(View.VISIBLE);
                    context.findViewById(R.id.datos_retiro_confirmacion).setVisibility(View.GONE);
                    context.findViewById(R.id.botonera_error).setVisibility(View.VISIBLE);
                    context.findViewById(R.id.botonera_estandar).setVisibility(View.GONE);
                    context.findViewById(R.id.progressbar_retiro_confirmacion).setVisibility(View.GONE);
                    return;
                }
            }
        }
        else {
            ((TextView)context.findViewById(R.id.mensaje)).setText("El monto no puede ser menor o igual a la comision.");
            context.findViewById(R.id.contenido_confirmacion_retiro).setVisibility(View.VISIBLE);
            context.findViewById(R.id.mensaje_linear).setVisibility(View.VISIBLE);
            context.findViewById(R.id.botonera_error).setVisibility(View.VISIBLE);
            context.findViewById(R.id.botonera_estandar).setVisibility(View.GONE);
            context.findViewById(R.id.datos_retiro_confirmacion).setVisibility(View.GONE);
            context.findViewById(R.id.progressbar_retiro_confirmacion).setVisibility(View.GONE);
            return;
        }
        context.findViewById(R.id.contenido_confirmacion_retiro).setVisibility(View.VISIBLE);
        context.findViewById(R.id.progressbar_retiro_confirmacion).setVisibility(View.GONE);
        context.findViewById(R.id.botonera_error).setVisibility(View.GONE);
    }
    private Boolean validar(Comision comision,Double plata){
        if(monto<=0){
            ((TextView)context.findViewById(R.id.mensaje)).setText("El monto no puede ser menor o igual a cero.");
            return false;
        }
        if(monto<=(Double.parseDouble(comision.getMonto_marchand())-Double.parseDouble(comision.getMonto_pagador()))){
            ((TextView)context.findViewById(R.id.mensaje)).setText("El monto debe ser superior a la comision.");
            return false;
        }
        if(monto>=disponible){
            ((TextView)context.findViewById(R.id.mensaje)).setText("Saldo disponible insuficiente para realizar la operación.");
            return false;
        }
        return true;
    }
}

