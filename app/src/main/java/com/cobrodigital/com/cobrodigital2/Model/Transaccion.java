package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.cobrodigital.com.cobrodigital2.Factory.transaccionFactory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.DoubleObjectType;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

@DatabaseTable(tableName = "Transaccion")
public class Transaccion extends Model {


    @DatabaseField(id = true,unique = true)
    private String id_transaccion;
    @DatabaseField
    private String fecha;
    @DatabaseField
    private String nro_boleta;
    @DatabaseField
    private String identificacion;
    @DatabaseField
    private String nombre;
    @DatabaseField
    private String info;
    @DatabaseField
    private String concepto;
    @DatabaseField
    private String bruto;
    @DatabaseField
    private String comision;
    @DatabaseField
    private String neto;
    @DatabaseField
    private String saldo_acumulado;
    private static Transaccion instance;
    public Transaccion(Context contexto, String id_transaccion, String fecha, String nro_boleta, String identificacion, String nombre, String info, String concepto, String bruto, String comision, String neto, String saldo_acumulado) {
        super(contexto);
        this.id_transaccion = id_transaccion;
        this.fecha = fecha;
        this.nro_boleta = nro_boleta;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.info = info;
        this.concepto = concepto;
        this.bruto = bruto;
        this.comision = comision;
        this.neto = neto;
        this.saldo_acumulado = saldo_acumulado;
    }

    public Transaccion(Context Context) {
        super(Context);
    }
    public Transaccion getInstance(Context context){
        if(instance==null)
            instance= new Transaccion(context);
        return instance;
    }

    public String getId_transaccion() {
        return id_transaccion;
    }

    public void setId_transaccion(String id_transaccion) {
        this.id_transaccion = id_transaccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNro_boleta() {
        return nro_boleta;
    }

    public void setNro_boleta(String nro_boleta) {
        this.nro_boleta = nro_boleta;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getBruto() {
        return bruto;
    }

    public void setBruto(String bruto) {
        this.bruto = bruto;
    }

    public String getComision() {
        return comision;
    }

    public void setComision(String comision) {
        this.comision = comision;
    }

    public Double getNeto() {
        return Double.parseDouble(neto);
    }

    public void setNeto(String neto) {
        this.neto = neto;
    }

    public String getSaldo_acumulado() {
        return saldo_acumulado;
    }
    public void setSaldo_acumulado(String saldo_acumulado) {
        this.saldo_acumulado = saldo_acumulado;
    }
    public Transaccion leerTransaccion(JSONObject dato ){
        try{
            this.setId_transaccion((String) dato.get("id_transaccion"));
            this.setFecha((String) dato.get("Fecha"));
            this.setNro_boleta((String) dato.get("Nro Boleta"));
            this.setIdentificacion((String) dato.get("Identificación"));
            this.setNombre((String)dato.get("Nombre"));
            this.setInfo((String)dato.get("Info"));
            if(dato.get("Concepto")==null){
                this.setConcepto("");
            }
            else{
                this.setConcepto(dato.get("Concepto").toString());
            }
            this.setBruto((String)dato.get("Bruto"));
            this.setComision((String)dato.get("Comisión"));
            this.setNeto((String)dato.get("Neto"));
            this.setSaldo_acumulado((String)dato.get("Saldo acumulado"));
        } catch (JSONException e1) {
            System.out.println(e1.getMessage());
            return null;
        }
        return this;
    }
    public Transaccion leerTransaccion(JSONObject dato ,boolean grabar){
        Transaccion tr=leerTransaccion(dato);
        if(grabar==true){
            //if(select_ultima_transaccion()==obtener_ultima_transaccion_json(dato))//ver mas adenlate
         try {
             transaccionFactory factory=new transaccionFactory();
             factory.guardar(tr);
         }catch (SQLException e){
             e.printStackTrace();
         }
        }
        return tr;
    }

}
