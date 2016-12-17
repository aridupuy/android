package com.cobrodigital.com.cobrodigital2.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Vector;

public class serviceTransacciones extends Service {
    private static final String TAG = "transacciones";
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
                    Gestor_de_credenciales.re_asociar(getApplicationContext());
                    CobroDigital cobroDigital = new CobroDigital(CobroDigital.credencial,getBaseContext());
                    cobroDigital.webservice.webservice_transacciones.consultar_transacciones(format.format(Fecha), format.format(Fecha), new LinkedHashMap());
                    Vector respuesta = cobroDigital.webservice.obtener_datos();
                    serviceTransacciones.total = 0;
                    if (respuesta != null) {
                        Object transicion[] = respuesta.toArray();
                        try {
                            JSONArray datos = new JSONArray((String) transicion[0]);
                            for (int i = 0; i < datos.length(); i++) {
                                Transaccion transaccion=new Transaccion();
                                transaccion=transaccion.leerTransaccion(getApplicationContext(),datos.getJSONObject(i));
                                if(!transaccion.getInfo().contains("Retiro"))
                                    serviceTransacciones.total +=transaccion.getNeto();
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.getCause();
                    e.getMessage();
                    e.getLocalizedMessage();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (serviceTransacciones.total != 0.0) {
                    long[] pattern = {500,500,};
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
                            getBaseContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setVibrate(pattern)
                            .setSound(alarmSound)
                            .setStyle(new NotificationCompat.InboxStyle())
                            .setContentTitle("transacciones")
                            .setContentText("Se han recaudado $" + serviceTransacciones.total + " el dia de hoy")
                            .setWhen(System.currentTimeMillis());
                    nManager.notify(12345, builder.build());
                    serviceTransacciones.total=0;
                    }
                }
        }).start();
        this.stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {

        System.out.println("destruido");
    }
}
