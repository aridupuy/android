package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ariel on 08/03/17.
 */

public class Token_google  {
    public static final String TOKEN = "Token";
    public static final String CLAVE_PREFERENCIA = "Token_google";
    protected String token;
    protected Context context;
    public Token_google(String token, Context context){
        this.setToken(token);
        this.setContext(context);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public boolean guardar_token(){
        SharedPreferences sh = context.getSharedPreferences(CLAVE_PREFERENCIA,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        if(!editor.putString(TOKEN,this.getToken()).commit())
            return false;
        return true;
    }
    public static  String  obtener_token(Context context){
        SharedPreferences sh = context.getSharedPreferences(CLAVE_PREFERENCIA,Context.MODE_PRIVATE);
        if(sh.contains(TOKEN))
            return sh.getString(TOKEN,"");
        else
            return null;
    }
    public boolean borrar_token(){
        SharedPreferences sh = context.getSharedPreferences(CLAVE_PREFERENCIA,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        if(!editor.remove(TOKEN).commit())
            return false;
        return true;
    }
}
