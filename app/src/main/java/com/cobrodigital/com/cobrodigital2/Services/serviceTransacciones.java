package com.cobrodigital.com.cobrodigital2.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimerTask;
import java.util.Vector;

public class serviceTransacciones extends Service {
    private static final String TAG = "Transacciones";
    public static float total;
    public Thread service;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            public Date Fecha = new Date();
            public SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            @Override
            public void run() {
                try {
                    Credencial credencial= new Credencial(getApplicationContext());
                    CobroDigital cobroDigital = new CobroDigital(credencial.obtenerCredencial());
                    cobroDigital.consultar_transacciones(format.format(Fecha), format.format(Fecha), new LinkedHashMap());
                    Vector respuesta = cobroDigital.obtener_datos();
                    serviceTransacciones.total = 0;
                    if (respuesta != null) {
                        Object transicion[] = respuesta.toArray();
                        try {
                            JSONArray datos = new JSONArray((String) transicion[0]);
                            System.out.println(datos.length());
                            for (int i = 0; i < datos.length(); i++) {
                                Transaccion transaccion=new Transaccion(getApplicationContext());
                                transaccion=transaccion.leerTransaccion(datos.getJSONObject(i));
                                System.out.println(transaccion);
                                serviceTransacciones.total +=transaccion.get_Neto();
                                transaccion.set();
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
                if (serviceTransacciones.total > 0.0) {
                    long[] pattern = {500,500,};
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
                            getBaseContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setVibrate(pattern)
                            .setSound(alarmSound)
                            .setStyle(new NotificationCompat.InboxStyle())
                            .setContentTitle("Transacciones")
                            .setContentText("Se han recaudado $" + serviceTransacciones.total + " el dia de hoy")
                            .setWhen(System.currentTimeMillis());
                    nManager.notify(12345, builder.build());
                    serviceTransacciones.total=0;
                    }
                System.out.println("Transacciones es 0");
                }
        }).start();
        System.out.println("termine");
        //this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        System.out.println("destruido");
    }
}
