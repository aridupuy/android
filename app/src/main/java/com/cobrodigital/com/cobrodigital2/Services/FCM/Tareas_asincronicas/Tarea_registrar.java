package com.cobrodigital.com.cobrodigital2.Services.FCM.Tareas_asincronicas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cobrodigital.com.cobrodigital2.Model.Token_google;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Estado_cuenta;
import com.cobrodigital.com.cobrodigital2.Modulos.Main.Main;
import com.cobrodigital.com.cobrodigital2.R;
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
    Main activity;
    public Tarea_registrar(Tarea_registrar.AsyncResponse delegate, Context context, Activity activity){
        this.delegate=delegate;
        this.context=context;
        this.activity=(Main)activity;
    }
    public Tarea_registrar(Tarea_registrar.AsyncResponse delegate, Context context){
        this.delegate=delegate;
        this.context=context;
        this.activity=null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(this.activity!=null){
            this.activity.findViewById(R.id.progressBar_main).setVisibility(View.VISIBLE);
            this.activity.findViewById(R.id.texto_registro).setVisibility(View.VISIBLE);
            this.activity.findViewById(R.id.boton_registrar).setVisibility(View.GONE);
        }
    }
    @Override
    protected Void doInBackground(String... strings) {
        String token=strings[0];
        String id_dispositivo= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            CobroDigital.webservice.webservice_registrar.registrar(token,id_dispositivo);
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



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(this.activity!=null){
            this.activity.setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) this.activity.findViewById(R.id.toolbar);
            DrawerLayout drawer = (DrawerLayout) this.activity.findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this.activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) this.activity.findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this.activity);
            this.activity.startActivity(new Intent(this.activity, Estado_cuenta.class));
        }
    }
}
