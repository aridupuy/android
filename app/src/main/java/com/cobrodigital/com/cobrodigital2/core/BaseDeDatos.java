package com.cobrodigital.com.cobrodigital2.core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Objects;


public class BaseDeDatos extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DATOS = "CobroDigital.db";
    private static final int VERSION_ACTUAL = 1;
    private final Context contexto;
    private SQLiteDatabase WriterSqLiteDatabase;
    private SQLiteDatabase ReaderSqLiteDatabase;
    private SQLiteDatabase sqLiteDatabase;
    public BaseDeDatos(Context contexto){
        super(contexto,NOMBRE_BASE_DATOS,null,VERSION_ACTUAL);
        this.contexto=contexto;
        this.WriterSqLiteDatabase=getWritableDatabase();
        this.sqLiteDatabase=getWritableDatabase();
        this.ReaderSqLiteDatabase=getReadableDatabase();
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
        System.out.println("Obtengo credencial");
        String SQL="SELECT idComercio,sid FROM Credencial";

        Cursor RecordSet=this.ReaderSqLiteDatabase.rawQuery(SQL,null);
        System.out.println(RecordSet.getCount());
        if(RecordSet.getCount()==1){
            RecordSet.moveToFirst();
            String IdComercio = RecordSet.getString(RecordSet.getColumnIndex("IdComercio"));
            String sid = RecordSet.getString(RecordSet.getColumnIndex("sid"));
            HashMap<String,String> credencial=new HashMap<String, String>();
            credencial.put("IdComercio",IdComercio);
            credencial.put("sid",sid);
            return credencial;
        }
        System.out.println("sin credenciales");
        return null;
    }
    public boolean setCredencial(HashMap credencial){
        String sql="INSERT INTO Credencial (IdComercio,sid) VALUES (?,?)";
        Object[] args={credencial.get("IdComercio"),credencial.get("sid")};
        this.sqLiteDatabase.execSQL(sql,args);
        return true;
    }
}
