package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TableRow;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

public class Transaccion extends Model {

    final private String ID_Tabla="id_transaccion";
    private int Id;
    private String fecha;
    private String nro_boleta;
    private String identificacion;
    private String nombre;
    private String info;
    private String concepto;
    private String bruto;
    private String comision;
    private String neto;
    private String saldo_acumulado;
    public Transaccion(Context context){
        super(context);
    }

    @Override
    public int getId() {
        return Id;
    }

    @Override
    public String getid_tabla() {
        return this.ID_Tabla;
    }
    public void set_Fecha(String fecha) {
        this.fecha = fecha;
    }

    public void set_Nro_boleta(String nro_boleta) {
        this.nro_boleta = nro_boleta;
    }

    public String get_Fecha() {
        return fecha;
    }

    public String get_Nro_boleta() {
        return nro_boleta;
    }

    public String get_Identificacion() {
        return identificacion;
    }

    public void set_Identificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String get_Nombre() {
        return nombre;
    }

    public void set_Nombre(String nombre) {
        this.nombre = nombre;
    }

    public String get_Info() {
        return info;
    }

    public void set_Info(String info) {
        this.info = info;
    }

    public String get_Concepto() {
        return concepto;
    }

    public void set_Concepto(String concepto) {
        this.concepto = concepto;
    }

    public String get_Bruto() {
        return bruto;
    }

    public void set_Bruto(String bruto) {
        this.bruto = bruto;
    }

    public String get_Comision() {
        return comision;
    }

    public void set_Comision(String comision) {
        this.comision = comision;
    }

    public Float get_Neto() {
        return Float.parseFloat((String)this.neto.replace(".","").replace(",","."));
    }

    public void set_Neto(String neto) {
        this.neto = neto;
    }

    public String get_Saldo_acumulado() {
        return saldo_acumulado;
    }

    public void set_Saldo_acumulado(String saldo_acumulado) {
        this.saldo_acumulado = saldo_acumulado;
    }

    public static Transaccion newInstance(Context context) {
        Bundle args = new Bundle();
        Transaccion fragment = new Transaccion(context);
        return fragment;
    }
    public Transaccion leerTransaccion(JSONObject dato ){
        try{
            this.set_Fecha((String) dato.get("Fecha"));
            this.set_Nro_boleta((String) dato.get("Nro Boleta"));
            this.set_Identificacion((String) dato.get("Identificación"));
            this.set_Nombre((String)dato.get("Nombre"));
            this.set_Info((String)dato.get("Info"));
            this.set_Concepto((String)dato.get("Concepto"));
            this.set_Bruto((String)dato.get("Bruto"));
            this.set_Comision((String)dato.get("Comisión"));
            this.set_Neto((String)dato.get("Neto"));
            this.set_Saldo_acumulado((String)dato.get("Saldo acumulado"));
        } catch (JSONException e1) {
                e1.printStackTrace();
            return null;
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
            return null;
        }
        return this;
    }
    public Transaccion leerTransaccion(JSONObject dato ,boolean grabar){
        Transaccion tr=leerTransaccion(dato);
        if(grabar==true){
            //if(select_ultima_transaccion()==obtener_ultima_transaccion_json(dato))//ver mas adenlate
            tr.set();
        }
        return tr;
    }
    public static Vector<Transaccion> obtener_transacciones_locales(Context context,String desde, String hasta, HashMap<String,String> variables){
        Transaccion transaccion=new Transaccion(context);
        String sql="SELECT * FROM Transaccion " +
                "WHERE DATE(substr(fecha,7,4)\n" +
                "||substr(fecha,4,2)\n" +
                "||substr(fecha,1,2)) \n" +
                "BETWEEN DATE(?) AND DATE(?) ORDER by fecha desc; ";
        String[] valores=new String[]{desde,hasta};
        Cursor RecordSet=transaccion.getReadableDatabase().rawQuery(sql,valores);
        System.out.println(sql);
        //Cursor RecordSet=transaccion.getReadableDatabase().rawQuery(sql,new String[]{} );
        Vector<Transaccion> transacciones=new Vector<Transaccion>();
        if(RecordSet.moveToFirst()) {
            while (!RecordSet.isAfterLast()){
                transaccion = new Transaccion(context);
                transaccion.set_Fecha(RecordSet.getString(RecordSet.getColumnIndex("fecha")));
                transaccion.set_Nro_boleta(RecordSet.getString(RecordSet.getColumnIndex("nro_boleta")));
                transaccion.set_Identificacion(RecordSet.getString(RecordSet.getColumnIndex("identificacion")));
                transaccion.set_Nombre(RecordSet.getString(RecordSet.getColumnIndex("nombre")));
                transaccion.set_Info(RecordSet.getString(RecordSet.getColumnIndex("info")));
                transaccion.set_Concepto(RecordSet.getString(RecordSet.getColumnIndex("concepto")));
                transaccion.set_Bruto(RecordSet.getString(RecordSet.getColumnIndex("bruto")));
                transaccion.set_Comision(RecordSet.getString(RecordSet.getColumnIndex("comision")));
                transaccion.set_Neto(RecordSet.getString(RecordSet.getColumnIndex("neto")));
                transaccion.set_Saldo_acumulado(RecordSet.getString(RecordSet.getColumnIndex("saldo_acumulado")));
                transacciones.add(transaccion);
                RecordSet.moveToNext();
            }
        }
        return transacciones;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        super.onUpgrade(sqLiteDatabase, i, i1);
    }
}
