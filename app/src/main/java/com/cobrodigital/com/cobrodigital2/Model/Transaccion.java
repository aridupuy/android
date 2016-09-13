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
    public Transaccion obtener_ultima_transaccion_json(JSONArray datos){
        Transaccion transaccion=null;
        try {
                JSONObject dato = datos.getJSONObject(datos.length()-1);
                transaccion=this.leerTransaccion(dato);

            } catch (JSONException e) {
            e.printStackTrace();
        }
        return transaccion;
    }
    public Cursor select_ultima_transaccion(){
        String sql="SELECT max(id_transaccion) FROM Transaccion";
        String [] variables=new String[]{};
        return this.Ejecutar_consulta(sql,variables);
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