package com.cobrodigital.com.cobrodigital2.core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;


public class BaseDeDatos extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DATOS = "CobroDigital.db";
    private static final int VERSION_ACTUAL = 1;
    private final Context contexto;
    private SQLiteDatabase sqLiteDatabase;
    public BaseDeDatos(Context contexto){
        super(contexto,NOMBRE_BASE_DATOS,null,VERSION_ACTUAL);
        this.contexto=contexto;
        this.sqLiteDatabase=getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql="CREATE TABLE IF NOT EXISTS Credencial( id_credencial INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "IdComercio TEXT NOT NULL, " +
                    "sid TEXT NOT NULL , " +
                    "UNIQUE( IdComercio,sid))";
        sqLiteDatabase.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST Credencial");
        onCreate(sqLiteDatabase);
    }
    public HashMap<String,String> obtener_credencial(){
        String SQL="SELECT idComercio,sid FROM Credencial";
        Cursor RecordSet=this.sqLiteDatabase.rawQuery(SQL,null);
        if(RecordSet.getCount()==1){
            HashMap<String,String> credencial=new HashMap<String, String>();
            credencial.put("IdComercio",(String) RecordSet.getString(0));
            credencial.put("sid",(String) RecordSet.getString(1));
            return credencial;
        }
        return null;
    }
}
