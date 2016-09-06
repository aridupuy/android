package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ariel on 6/09/16.
 */
public class Credencial extends Model{
    private String ID_Tabla="id_credencial";
    private int Id;
    private String sid;
    private String IdComercio;
    public Credencial(Context contexto) {
        super(contexto);
    }
    public int getId(){
        return Id;
    }
    public String getid_tabla(){
        return ID_Tabla;
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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        super.onUpgrade(sqLiteDatabase, i, i1);
    }
    public Credencial obtenerCredencial(){
        Cursor recordset=this.select(new String[]{});
        if(recordset.getCount()!=1)
            return null;
        recordset.moveToFirst();
        do{
            this.Id=recordset.getInt(0);
            this.IdComercio=recordset.getString(2);
            this.sid=recordset.getString(3);
        }while (recordset.moveToNext());
        return this;
    }
}
