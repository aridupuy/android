package com.cobrodigital.com.cobrodigital2.Gestores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.cobrodigital.com.cobrodigital2.Factory.credencialFactory;
import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Modulos.Main.Main;
import com.cobrodigital.com.cobrodigital2.Services.Tareas_asincronicas.Tarea_registrarGCM;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.LectorQR;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ariel on 14/10/16.
 */

public class Gestor_de_credenciales {
    private static final String TAG = "Registrando";
    private static final Boolean REGISTRADO_GOOGLE_DEVELOPER = false;
    private  static boolean PRUEBA=false;
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    public static final String PROPERTY_USER = "user";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static GoogleCloudMessaging gcm;
    public static boolean esta_asociado(){
    //Probar guardar la info como sharedPreferences

        if(CobroDigital.credencial!=null){
            System.out.println("esta asociado");
            return true;
        }
        System.out.println("no esta asociado");
        return false;
    }
    public static void asociar(Context context, Intent intent,Activity activity){
        String contents = intent.getStringExtra("SCAN_RESULT");
        LectorQR lectorQR=new LectorQR(context);
        try{
            lectorQR.leer(contents);
        }catch (JSONException e){
            Gestor_de_mensajes_usuario.mensaje(e.getMessage(),context);
            e.printStackTrace();

            return;
        }
        try {
            if(REGISTRADO_GOOGLE_DEVELOPER)
                registrar(context,activity);
            credencialFactory factory=new credencialFactory(context);
            factory.guardar(CobroDigital.credencial);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
    public static Boolean re_asociar(Context context) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, PackageManager.NameNotFoundException {
        if(Main.emulador){
            CobroDigital.credencial=new Credencial();
            CobroDigital.credencial.set_IdComercio("FL662997");
            CobroDigital.credencial.set_sid("ABZ0ya68K791phuu76gQ5L662J6F2Y4j7zqE2Jxa3Mvd22TWNn4iip6L9yq");
            return  true;
        }
        try {
            Credencial credencial = new Credencial(context);
            CobroDigital.credencial=credencial.obtenerCredencial();
            if(REGISTRADO_GOOGLE_DEVELOPER){
                String regid=getRegistrationId(context);
                if(!regid.equals(""))
                    CobroDigital.regid=regid;
                else return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(CobroDigital.credencial!=null ){

            return true;
        }
        return false;

    }
    public static boolean desasociar(Context context){
        try {
            if(CobroDigital.credencial.BorrarCredencial()){
                CobroDigital.credencial=null;
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
            return false;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static void registrar(Context context,Activity activity) throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, PackageManager.NameNotFoundException, InvalidKeyException {
//        if(checkPlayServices(context,activity)){
            gcm = GoogleCloudMessaging.getInstance(context);
            String regid = getRegistrationId(context);
            if(regid.equals("")){
                Tarea_registrarGCM tarea = new Tarea_registrarGCM(context);
                try {
                    tarea.execute(CobroDigital.credencial.get_IdComercio());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
//        }

    }
    private static Boolean checkPlayServices(Context context, Activity activity){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i(TAG, "Debe tener instalado Google play services.");
                return false;
            }
            return false;
        }
        return true;
    }
    private static String getRegistrationId(Context context) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, PackageManager.NameNotFoundException {
        SharedPreferences prefs = context.getSharedPreferences(
                Main.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.length() == 0)
        {
            Log.d(TAG, "Registro GCM no encontrado.");
            return "";
        }

        String registeredUser =
                prefs.getString(PROPERTY_USER, "user");

        int registeredVersion =
                prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        long expirationTime =
                prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));

        Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                ", version=" + registeredVersion +
                ", expira=" + expirationDate + ")");

        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        int currentVersion =pInfo.versionCode;

        if (registeredVersion != currentVersion)
        {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return "";
        }
        else if (System.currentTimeMillis() > expirationTime)
        {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        }
        else if (!(CobroDigital.credencial.get_IdComercio()).equals(registeredUser))
        {
            Log.d(TAG, "Nuevo nombre de usuario.");
            return "";
        }

        return registrationId;

    }
}