package com.cobrodigital.com.cobrodigital2.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.os.IBinder;
import android.provider.MediaStore;

import org.junit.internal.requests.ClassRequest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class serviceBoot extends BroadcastReceiver {
    final String PAQUETE="com.cobrodigital.com.cobrodigital2.Services.";
    public serviceBoot() {
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //metodo que ejecuta la llamada del sistema
    @Override
    public void onReceive(Context context, Intent intent) {
        //File path=new File("");
        Vector<String> servicios = new Vector<String>();
        servicios.add(PAQUETE+"serviceTransacciones");
        //Añadir los demas Servicios
        System.out.println(context.getPackageCodePath());
        for (String servicio : servicios) {
            //Levanto dinamicamente todos los servicios disponibles excepto a si mismo
            Class<?> clase = null;
            try {
                clase = Class.forName(servicio);
                Intent intentService = new Intent(context, clase);
                context.startService(intentService);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
