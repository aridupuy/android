package com.cobrodigital.com.cobrodigital2.Services.Tareas_asincronicas;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Modulos.Main.Main;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by ariel on 06/03/17.
 */

public class Tarea_registrarGCM extends AsyncTask<String,String,GoogleCloudMessaging> {
    private static final String TAG = "Registrando";
    private static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7; //expira en una semana
    private static final String SENDER_ID = "com.cobrodigital.app.android";
    private Context context;
    private GoogleCloudMessaging gcm;
    private String regid;
    public Tarea_registrarGCM(Context context){
        this.context=context;
    }
    @Override
    protected GoogleCloudMessaging doInBackground(String... strings) {
        String msg = "";
        try
        {
            if (gcm == null)
            {
                gcm = GoogleCloudMessaging.getInstance(context);
            }

            //Nos registramos en los servidores de GCM
            regid = gcm.register(SENDER_ID);

            Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
            if(!guardar_registro(regid))
                Log.d("Error", "No Registrado en GCM!");
            //Nos registramos en nuestro servidor
            boolean registrado = registroServidor(strings[0], regid);

            //Guardamos los datos del registro
            if(registrado)
            {
                setRegistrationId(context, strings[0], regid);
            }
        }
        catch (IOException ex)
        {
            Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
        }

        return gcm;
    }

    @Override
    protected void onPostExecute(GoogleCloudMessaging googleCloudMessaging) {
        super.onPostExecute(googleCloudMessaging);
        CobroDigital.GCM=gcm;
    }
    protected  boolean guardar_registro(String regid){
        SharedPreferences preferencias=context.getSharedPreferences(Main.class.getSimpleName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString(Gestor_de_credenciales.PROPERTY_REG_ID,regid);
        if(!editor.commit())
            return false;
        return true;
    }
    private boolean registroServidor(String string, String regid) {

        return true;
    }
    private void setRegistrationId(Context context, String user, String regId)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Main.class.getSimpleName(),
                Context.MODE_PRIVATE);
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }
        int appVersion =packageInfo.versionCode;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Gestor_de_credenciales.PROPERTY_USER, user);
        editor.putString(Gestor_de_credenciales.PROPERTY_REG_ID, regId);
        editor.putInt(Gestor_de_credenciales.PROPERTY_APP_VERSION, appVersion);
        editor.putLong(Gestor_de_credenciales.PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + EXPIRATION_TIME_MS);
        editor.commit();
    }
}
