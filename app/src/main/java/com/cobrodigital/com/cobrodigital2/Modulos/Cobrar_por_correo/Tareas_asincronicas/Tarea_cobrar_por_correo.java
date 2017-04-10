package com.cobrodigital.com.cobrodigital2.Modulos.Cobrar_por_correo.Tareas_asincronicas;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.Webservice.Webservice;
import com.cobrodigital.com.cobrodigital2.Webservice.Webservice_cobrar_por_correo;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ariel on 05/04/17.
 */

public class Tarea_cobrar_por_correo extends AsyncTask<String,Intent,Boolean> {
    private AppCompatActivity context;
    private AsyncResponse callback;
    public interface AsyncResponse {
        void processFinish(Boolean resultado );
    }
    public Tarea_cobrar_por_correo(AppCompatActivity context,AsyncResponse callback){
        this.context = context;
        this.callback=callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((LinearLayout)context.findViewById(R.id.progressbar_Boleta)).setVisibility(View.VISIBLE);
        ((LinearLayout)context.findViewById(R.id.vista_cobxcorreo)).setVisibility(View.GONE);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String nombre=strings[0];
        String apellido=strings[1];
        String documento=strings[2];
        String correo=strings[3];
        String direccion=strings[4];
        String concepto=strings[5];
        String vencimiento=strings[6];
        String importe=strings[7];
        try {
            if(!Webservice_cobrar_por_correo.cobrar_por_correo(nombre , apellido , correo, documento , direccion, concepto, vencimiento, importe)){
                return false;
            }

        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return false;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        if(Webservice.obtener_resultado().equals("1")){
            return true;
        }
        else
            return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        callback.processFinish(aBoolean);
    }
}
