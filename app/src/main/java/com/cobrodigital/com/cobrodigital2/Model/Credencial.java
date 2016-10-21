package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import java.util.HashMap;
/**
 * Created by Ariel on 6/09/16.
 */
public class Credencial extends Model{

    final private String ID_Tabla="id_credencial";
    private int Id;
    private String sid;
    private String IdComercio;
    final private String unique="IdComercio";
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
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
        if(this.IdComercio==null || this.sid==null)
            return null;
        return this;
    }
    public boolean BorrarCredencial(){
        if(this.delete())
            return true;
        return false;
    }
    @Override
    public String getunique(){
        return unique;
    }
}
