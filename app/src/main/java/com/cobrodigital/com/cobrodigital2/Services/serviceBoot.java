package com.cobrodigital.com.cobrodigital2.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class serviceBoot extends BroadcastReceiver {
    final String PAQUETE="com.cobrodigital.com.cobrodigital2.Services.";
    private boolean Status=false;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    final Context context;
    public serviceBoot(Context context) {
        this.context=context;
    }
    public void onBind(Intent intent) {
        if (!Status) {
            Status = true;
            startTimer();
        }
    }

    //metodo que ejecuta la llamada del sistema
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Status){
            Status=true;
            startTimer();
        }

    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //File path=new File("");
                        Vector<String> servicios = new Vector<String>();
                        servicios.add(PAQUETE+"serviceTransacciones");
                        //Añadir los demas Servicios
                        for (String servicio : servicios) {
                            //Levanto dinamicamente todos los servicios disponibles excepto a si mismo
                            Class<?> clase = null;
                            try {
                                clase = Class.forName(servicio);
                                Intent intentService = new Intent(context, clase);
                                context.startService(intentService);
                                servicios.remove(servicio);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };
    }

    public void startTimer() {
        //set a new Timer
            timer = new Timer();
            //initialize the TimerTask's job
            initializeTimerTask();
            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer.schedule(timerTask, 5000, 3600000); //

    }
}
