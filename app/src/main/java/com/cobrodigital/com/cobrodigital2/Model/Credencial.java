package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_cifrado;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Ariel on 6/09/16.
 */
public class Credencial {
    public static final String CREDENCIAL = "credencial";
    public static final String ID_COMERCIO = "IdComercio";
    public static final String SID = "sid";
    private byte[] sid;
    private byte[] IdComercio;
    protected Context context;
    public Credencial(String IdComercio,String sid,Context context) {
        set_IdComercio(IdComercio);
        set_sid(sid);
    }
    public Credencial(){}
    public Credencial(Context context){
        this.context=context;
    }
    public boolean is_null_IdComercio(){
        if(IdComercio==null)
            return true;
        return false;
    }
    public boolean is_null_sid(){
        if(sid==null)
            return true;
        return false;
    }
    public void set_IdComercio(String IdComercio){
        Gestor_de_cifrado cifrado=new Gestor_de_cifrado();
        try {
            this.IdComercio=cifrado.cifra(IdComercio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String get_IdComercio() throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        Gestor_de_cifrado cifrado=new Gestor_de_cifrado();
        return cifrado.descifra(this.IdComercio);
    }
    public void set_sid(String sid){
        Gestor_de_cifrado cifrado=new Gestor_de_cifrado();
        try {
            this.sid=cifrado.cifra(sid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String get_sid() throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        Gestor_de_cifrado cifrado=new Gestor_de_cifrado();
        return cifrado.descifra(sid);
    }
    public boolean guardar_credencial() {

            SharedPreferences sh = context.getSharedPreferences(CREDENCIAL, context.MODE_PRIVATE);
        try {
            if(sh.edit().putString(ID_COMERCIO, get_IdComercio()).putString(SID, get_sid()).commit())
                return true;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Credencial obtenerCredencial() {
        SharedPreferences sh = context.getSharedPreferences(CREDENCIAL,context.MODE_PRIVATE);
        String id_comercio=sh.getString(ID_COMERCIO,"");
        if(!id_comercio.equals("")){
            set_IdComercio(id_comercio);
        }
        String sid = sh.getString(SID,"");
        if(!sid.equals("")){
            set_sid(sid);
        }
        return this;
    }
    public boolean BorrarCredencial() {
        SharedPreferences sh = context.getSharedPreferences(CREDENCIAL,context.MODE_PRIVATE);
        if(sh.edit().clear().commit()){
            this.sid=null;
            this.IdComercio=null;
            return true;
        }
        return false;
    }

}
