package com.cobrodigital.com.cobrodigital2.Services;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.BaseDeDatos;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.SimpleFormatter;

public class serviceTransacciones extends Service {
    private static final String TAG = "Transacciones";
    public static int total = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {



            @Override
            public void run() {
                try {
                    BaseDeDatos baseDeDatos = new BaseDeDatos(getApplicationContext());
                    HashMap<String, String> credencial = baseDeDatos.obtener_credencial();
                    CobroDigital cobroDigital = new CobroDigital(credencial);
                    Date Fecha = new Date();
                    String fecha_a_enviar = Fecha.getYear() + "" + Fecha.getMonth() + "" + Fecha.getDay();
                    cobroDigital.consultar_transacciones(fecha_a_enviar, fecha_a_enviar, new LinkedHashMap());
                    Vector respuesta = cobroDigital.obtener_datos();

                    if (respuesta != null) {
                        Object transicion[] = respuesta.toArray();
                        try {
                            JSONArray datos = new JSONArray((String) transicion[0]);
                            for (int i = 0; i < datos.length(); i++) {
                                JSONObject dato = datos.getJSONObject(i);
                                serviceTransacciones.total += Integer.parseInt(dato.get("Neto").toString());
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    getBaseContext())
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Transacciones")
                    .setContentText("Se Han recaudado $"+ serviceTransacciones.total+" el dia de hoy")
                    .setWhen(System.currentTimeMillis());
            nManager.notify(12345, builder.build());
        Log.d(TAG, "sleep finished");
    }
}
