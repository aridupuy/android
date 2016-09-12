package com.cobrodigital.com.cobrodigital2.Model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.cobrodigital.com.cobrodigital2.MainActivity;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Ariel on 6/09/16.
 */
public class Credencial extends Model{

    private String ID_Tabla="id_credencial";
    private int Id;
    private String sid;
    private String IdComercio;
    private String contexto;
    public Credencial(Context contexto) {
        super(contexto);
    }
    public int getId(){
        return Id;
    }
    public String getid_tabla(){
        return this.ID_Tabla;
    }
    public void setId(int Id){
        this.Id=Id;
    }
    public void set_IdComercio(String IdComercio){
        this.IdComercio=IdComercio;
    }
    public String get_IdComercio(){
        return IdComercio;
    }
    public void set_sid(String sid){
        this.sid=sid;
    }
    public String get_sid(){
        return sid;
    }
    public static Credencial newInstance(Context context) {
        Bundle args = new Bundle();
        Credencial fragment = new Credencial(context);
        return fragment;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        super.onUpgrade(sqLiteDatabase, i, i1);
    }
    public Credencial obtenerCredencial(){
        Cursor recordset=this.select(new HashMap<String, String>());
        recordset.moveToFirst();
        while(recordset.isAfterLast()==false){
            this.Id=recordset.getInt(recordset.getColumnIndex("id_credencial"));
            this.IdComercio=recordset.getString(recordset.getColumnIndex("IdComercio"));
            this.sid=recordset.getString(recordset.getColumnIndex("sid"));
            recordset.moveToNext();
        }
        System.out.println(Id);
        System.out.println(IdComercio);
        System.out.println(sid);
        if(IdComercio==null || sid==null)
            return null;
        return this;
    }
    public void BorrarCredencial(){
        this.delete();
    }
}
