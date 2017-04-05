package com.cobrodigital.com.cobrodigital2.core;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final String CONFIG = "config";
    private static final String PLANTILLA_BOLETA_DEFAULT  = "init";
    public static final String POSICION_MODELO_BOLETA="plantilla";
    private static HashMap<String,String> config;
    public static HashMap<String,String> get_instance_config(HashMap<String,String> config, Context context) {
        //singleton
        if(config==null){
            SharedPreferences sh = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
            Map<String,?> mapa=sh.getAll();
            config=new HashMap<String,String>();
            for (Map.Entry<String, ?> entry : mapa.entrySet()){
                config.put(entry.getKey(),(String)entry.getValue());
            }
            if(config.size()==0)
                config=default_config();
            return config;
        }
        else
            return config;
    }
    public static boolean guardar_config(HashMap<String,String> config, Context context){
        SharedPreferences sh = context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();
        for (Map.Entry<String, String> entry : config.entrySet()){
            edit.putString((String) entry.getKey(),(String)entry.getValue());
        }
        if(!edit.commit())
            return false;
        return true;
    }
    private static HashMap<String,String>default_config(){
        HashMap<String,String>conf=new HashMap<String,String>();
        conf.put("plantilla",PLANTILLA_BOLETA_DEFAULT);
        return conf;
    }
    public static void reiniciar_config(Context context){
        config=default_config();
        guardar_config(config,context);
    }
}
