package com.cobrodigital.com.cobrodigital2.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by Ariel on 6/09/16.
 */
public abstract class Model extends SQLiteOpenHelper{
    private final String PREFIJO_GETTER="get_";
    private final String PREFIJO_SETTER="set_";
    private static final String NOMBRE_BASE_DATOS = "CobroDigital.db";
    private static final int VERSION_ACTUAL = 1;
    public  String ID_Tabla;
    private final Context contexto;
    private SQLiteDatabase WriterSqLiteDatabase;
    private SQLiteDatabase ReaderSqLiteDatabase;
    private SQLiteDatabase sqLiteDatabase;
    public Model(Context contexto){
        super(contexto,NOMBRE_BASE_DATOS,null,VERSION_ACTUAL);
        this.contexto=contexto;
    }
    private Vector<Method> get_metodos(){
        Method[] metodos=this.getClass().getDeclaredMethods();
        Vector<Method> columnas=new Vector<Method>();
        for (Method method: metodos) {
            if(method.getName().substring(0,4).equals(PREFIJO_GETTER)){
                System.out.println(method.getName());
                columnas.add(method);
            }
        }
        return columnas;
    }
    public abstract int getId();
    public abstract String getid_tabla();
    public void set(){
        Vector<Method> metodos=this.get_metodos();
        if(this.getId()!=0) {
            try {
                this.insert(metodos);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else{
            try {
                this.update(metodos);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    public Cursor get(int id){
        Vector<Method> metodos=this.get_metodos();
        Cursor recordSet=this.select(new String[]{Integer.toString(id)});
        return recordSet;
    }
    public Cursor select(String[] param){
        Vector<Method> Metodos= this.get_metodos();
        String [] Columnas=new String[Metodos.size()];
        int i=0;
        for (Method metodo: Metodos) {
            Columnas[i]=metodo.getName().replace(PREFIJO_GETTER,"").replace(PREFIJO_SETTER,"");
            i++;
        }

        Cursor recordset=getReadableDatabase().query(this.getClass().getSimpleName(),Columnas,"",param,null,null,null);
        return recordset;
    }
    private void insert(Vector<Method> Columnas) throws InvocationTargetException, IllegalAccessException {
        String Tabla = this.getClass().getName();
        ContentValues Values= new ContentValues();
        for (Method columna: Columnas) {
            if(columna.getName().substring(0,3)==PREFIJO_GETTER)
                if(columna.invoke(columna.getName().toString(), new Object[] {}).toString()!=null)
                    Values.put(columna.getName().replace(PREFIJO_GETTER,"").replace(PREFIJO_SETTER,""), (String) columna.invoke(columna.getName().toString(), new Object[] {}));
        }
        getWritableDatabase().insert(Tabla,null,Values);
    }
    private void update(Vector<Method> Columnas) throws InvocationTargetException, IllegalAccessException {

        String Tabla = this.getClass().getName();
        ContentValues Values= new ContentValues();
        for (Method columna: Columnas) {
            if(columna.getName().substring(0,3)==PREFIJO_GETTER)
                if(columna.invoke(columna.getName().toString(), new Object[] {}).toString()!=null)
                    Values.put(columna.getName().replace(PREFIJO_GETTER,"").replace(PREFIJO_SETTER,""), (String) columna.invoke(columna.getName().toString(), new Object[] {}));
        }
        String [] whereARGS;
        whereARGS = new String[] {Integer.toString(this.getId())};
        getWritableDatabase().update(Tabla,Values,this.ID_Tabla+"=?",whereARGS);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Vector<Method> metodos=get_metodos();
        String campos=""+getid_tabla()+"INTEGER PRIMARY KEY ";
        System.out.println(metodos.size());
        for(int i =0 ; i<metodos.size();i++){
            campos+= ", "+metodos.get(i).getName().replace(PREFIJO_GETTER,"").replace(PREFIJO_SETTER,"")+" TEXT NOT NULL";
        }
        String sql="CREATE TABLE IF NOT EXISTS "+this.getClass().getSimpleName()+"("+campos+") ";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){

    }

}
