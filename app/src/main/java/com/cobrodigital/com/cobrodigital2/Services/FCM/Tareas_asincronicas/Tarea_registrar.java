package com.cobrodigital.com.cobrodigital2.Services.FCM.Tareas_asincronicas;

import android.content.Context;
import android.os.AsyncTask;

import com.cobrodigital.com.cobrodigital2.Model.Token_google;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ariel on 07/03/17.
 */

public class Tarea_registrar extends AsyncTask<String, Void, Void> {
    public interface AsyncResponse {
        void processFinish(Boolean resultado);
    }
    Tarea_registrar.AsyncResponse delegate;
    Context context;
    public Tarea_registrar(Tarea_registrar.AsyncResponse delegate,Context context){
        this.delegate=delegate;
        this.context=context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String token=strings[0];
        try {
            CobroDigital.webservice.webservice_registrar.registrar(token);
            if(CobroDigital.webservice.obtener_resultado().equals("1")){
                delegate.processFinish(true);
                CobroDigital.token_id=new Token_google(token,context);
                CobroDigital.token_id.guardar_token();
            }
            else{
                delegate.processFinish(false);
                return null;
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
        return null;
    }

}
