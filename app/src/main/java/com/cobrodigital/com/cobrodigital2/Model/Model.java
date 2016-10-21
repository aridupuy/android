package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Ariel on 6/09/16.
 */
public abstract class Model extends SQLiteOpenHelper{
    protected final Boolean ACTIVAR_DEBUG=true;
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
        SQLiteDatabase db =getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        this.contexto=contexto;
        Vector<String> campos=get_campos();
        String columnas=""+getid_tabla()+" STRING PRIMARY KEY ";
        for (String campo: campos) {
            if(campo!=getid_tabla()) {
                System.out.println(campo);
                System.out.println(getunique());
                if (campo == getunique())
                    columnas += "," + campo + " String UNIQUE";
                else
                    columnas += "," + campo + " String";
            }
        }

        String sql="CREATE TABLE IF NOT EXISTS "+this.getClass().getSimpleName()+"("+columnas+")";
        if (ACTIVAR_DEBUG)
            System.out.println(sql);
        int pasa=0;
        while(pasa==0) {
            try {
                db.execSQL(sql);
                pasa = 1;
            } catch (Exception e) {
                pasa = 0;
            }
        }
    }
    private Vector<String> get_campos(){
        Field [] Campos=this.getClass().getDeclaredFields();
        Vector<String> columnas = new Vector<String>();
        for (Field campo : Campos) {
            if (!campo.getName().equals("Id") && !campo.getName().equals("ID_Tabla") && !campo.getName().equals("contexto") && !campo.getName().equals("campos")&& !campo.getName().equals("unique")&& !campo.getName().equals("campos_variables"))
                columnas.add((String) campo.getName());
        if(campo.getName()=="campos_variables"){
            Method[] metodos=this.getClass().getDeclaredMethods();
            for (Method metodo:metodos)
                if (metodo.getName() == "get_campos_variables") {
                    try {
                        Vector<String> campos = (Vector<String>) metodo.invoke(this, (Object[]) null);
                        for (String valor : campos) {
                            columnas.add(valor);
                        }
                    }catch (IllegalAccessException i){
                        System.out.println("Error en el acceso");
                    }
                    catch (InvocationTargetException e){
                        System.out.println("Error no se puede invocar el metodo");
                    }
                }
        }
        }
        return columnas;
    }
    private Vector<String> get_values(){
        Method [] metodos=this.getClass().getDeclaredMethods();
        Vector<String> Values=new Vector<String>();
        for (Method metodo: metodos) {
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
        if (ACTIVAR_DEBUG)
            System.out.println(Values);
        return Values;
    }
    public abstract int getId();
    public abstract String getid_tabla();
    public void set(){
        Vector<String> campos=this.get_campos();
        if(this.getId()==0) {
            try {
                this.insert(campos);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else{
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
        SQLiteDatabase db=getWritableDatabase();
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
        Cursor RecordSet=db.rawQuery(sql,array);
        return RecordSet;
    }
    private void insert(Vector<String> campos) throws InvocationTargetException, IllegalAccessException {
        String Tabla = this.getClass().getSimpleName();
        Vector<String> Valores=get_values();
        String sql="INSERT INTO "+Tabla+" (";
        for (String campo: campos) {
            sql+=campo+",";
        }
        sql=sql.substring(0,sql.length()-1);
        sql+=") VALUES (";
        for (String Valor: Valores) {
            sql +="'"+Valor+"'"+ ",";
        }
        sql=sql.substring(0,sql.length()-1);
        sql+=")";
        if (ACTIVAR_DEBUG)
            System.out.println(sql);
        getWritableDatabase().execSQL(sql);
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
        sql=sql.substring(0,sql.length()-2);
        sql+=" WHERE "+getid_tabla()+"="+getId();
        if (ACTIVAR_DEBUG)
            System.out.println(sql);
        getWritableDatabase().execSQL(sql);

    }
    public boolean delete(){
        String Tabla = this.getClass().getSimpleName();
        String where=""+getid_tabla()+"=?";
        String [] args={String.valueOf(getId())};
        if(getWritableDatabase().delete(Tabla,where,args)>0) {
            return true;
        }
        return false;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL("DROP TABLE "+this.getClass().getSimpleName());
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        String sql="DROP TABLE IF EXISTS " + this.getClass().getSimpleName();
        sqLiteDatabase.execSQL(sql);
        if(ACTIVAR_DEBUG)
            System.out.println("sql:"+sql);
        onCreate(sqLiteDatabase);
    }
    public Cursor Ejecutar_consulta(String sql, String [] args){
        if(ACTIVAR_DEBUG){
            System.out.println(sql);
            System.out.println(args);
        }
        return this.sqLiteDatabase.rawQuery(sql,args);
    }
    public abstract String getunique();
    }
