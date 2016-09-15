package com.cobrodigital.com.cobrodigital2.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
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
    public final Context contexto;
    private SQLiteDatabase WriterSqLiteDatabase;
    private SQLiteDatabase ReaderSqLiteDatabase;
    private SQLiteDatabase sqLiteDatabase;
    public Model(Context contexto){
        super(contexto,NOMBRE_BASE_DATOS,null,VERSION_ACTUAL);
        this.contexto=contexto;
        Vector<String> campos=get_campos();
        String columnas=""+this.getid_tabla()+" INTEGER PRIMARY KEY ";
        for (String campo: campos) {
            columnas+=","+campo+" String not null ";
        }
        String sql="CREATE TABLE IF NOT EXISTS "+this.getClass().getSimpleName()+"("+columnas+") ";
        System.out.println(sql);
        getWritableDatabase().execSQL(sql);
    }
    private Vector<String> get_campos(){
        Field [] Campos=this.getClass().getDeclaredFields();
        Vector<String> columnas=new Vector<String>();
        for (Field campo: Campos) {
            if (campo.getName()!="Id" && campo.getName()!="ID_Tabla" &&  campo.getName()!="contexto")
            columnas.add(campo.getName());

        }
        return columnas;
    }
    private Vector<String> get_values(){
        Method [] metodos=this.getClass().getDeclaredMethods();
        Vector<String> Values=new Vector<String>();
        for (Method metodo: metodos) {
            System.out.println(metodo.getName());
            if (metodo.getName().startsWith("get_") && !metodo.getName().endsWith("Id")&&!metodo.getName().endsWith("ID_Tabla") && !metodo.getName().endsWith("Class")) {
                try {
                    if(metodo.invoke(this, (Object[]) null).getClass().isInstance("String"))
                        Values.add((String) metodo.invoke(this, (Object[]) null));
                    else
                        Values.add(String.valueOf(metodo.invoke(this, (Object[]) null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(Values);
        return Values;
    }
    public abstract int getId();
    public abstract String getid_tabla();
    public void set(){
        Vector<String> campos=this.get_campos();
        System.out.println("estoy en set id es :"+this.getId());
        System.out.println(campos);
        if(this.getId()==0) {
            System.out.println("entro en insert");
            try {
                this.insert(campos);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("entro en update");
            try {
                this.update(campos);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    public Cursor get(int id){
        Vector<String> Campos=this.get_campos();
        HashMap<String,String> variables=new HashMap<String, String>();
        variables.put(getid_tabla(),Integer.toString(id));
        Cursor recordSet=this.select(variables);
        return recordSet;
    }
    public Cursor select(HashMap<String,String> Variables){
        String Tabla=this.getClass().getSimpleName();
        String sql="SELECT * FROM "+Tabla;

        String[] array=new String[Variables.size()];
        int i = 0;
        if(Variables.size()>0){
            String where=" WHERE ";
            if(Variables.containsKey("desde") && !Variables.containsKey("hasta"))
                return null;
            if(Variables.containsKey("hasta") && !Variables.containsKey("desde"))
                return null;
            if(Variables.containsKey("desde") && Variables.containsKey("hasta")){
                where+="date(fecha) between date(?) AND date(?)";
                array[i]=(String)Variables.get("desde");
                array[i+1]=(String)Variables.get("hasta");
                Variables.remove("desde");
                Variables.remove("hasta");
            }

            for (HashMap.Entry<String,String> variable :Variables.entrySet()){
                where+=" AND "+variable.getKey()+"=?";
                array[i]=variable.getValue();
                    i++;
            }
            sql+=where;
        }
        System.out.println(sql);
        Cursor RecordSet=getReadableDatabase().rawQuery(sql,array);
        return RecordSet;
    }
    private void insert(Vector<String> campos) throws InvocationTargetException, IllegalAccessException {
        String Tabla = this.getClass().getSimpleName();
        Vector<String> Valores=get_values();
        String sql="INSERT INTO "+Tabla+" (";
        for (String campo: campos) {
            sql+=campo+",";
        }
        System.out.println("sql:"+sql);
        sql=sql.substring(0,sql.length()-1);
        sql+=") VALUES (";
        System.out.println(sql);
        for (String Valor: Valores) {
            sql +="'"+Valor+"'"+ ",";
        }
        sql=sql.substring(0,sql.length()-1);
        sql+=")";
        System.out.println(sql);
        getWritableDatabase().execSQL(sql);
        System.out.println("inserte");
    }
    private void update(Vector<String> campos) throws InvocationTargetException, IllegalAccessException {
        String Tabla = this.getClass().getSimpleName();
        Vector<String> Valores=get_values();
        String sql="UPDATE "+Tabla+" set ";
        int i=0;
        for (String campo: campos) {
            sql+=campo+"= '"+ Valores.get(i)+"', ";
            i++;
        }
        System.out.println("sql:"+sql);
        sql=sql.substring(0,sql.length()-2);
        sql+=" WHERE "+getid_tabla()+"="+getId();
        System.out.println(sql);
        getWritableDatabase().execSQL(sql);

    }
    public void delete(){
        String Tabla = this.getClass().getSimpleName();
        String sql="DELETE FROM "+Tabla+" ";
        sql+="WHERE "+getid_tabla()+"="+getId();
        System.out.println(sql);
        getWritableDatabase().execSQL(sql);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL("DROP TABLE "+this.getClass().getSimpleName());
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.getClass().getSimpleName());
        onCreate(sqLiteDatabase);
    }
    public Cursor Ejecutar_consulta(String sql, String [] args){
        return this.sqLiteDatabase.rawQuery(sql,args);
    }
}
