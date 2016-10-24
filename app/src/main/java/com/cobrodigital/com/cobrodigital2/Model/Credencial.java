package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.cobrodigital.com.cobrodigital2.Factory.credencialFactory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Ariel on 6/09/16.
 */
@DatabaseTable(tableName = "Credencial")
public class Credencial extends Model{

    @DatabaseField(id=true, index = true)
    private int Id_credencial;
    @DatabaseField(unique = true)
    private String sid;
    @DatabaseField(unique = true)
    private String IdComercio;
    protected Context context;
    public Credencial(String IdComercio,String sid,Context context) {
        super(context);
        set_IdComercio(IdComercio);
        set_sid(sid);
    }
    public Credencial(Context context){
        super(context);
        this.context=context;
    }
    public Credencial(){
        super(null);
    }

    public int getId_credencial() {
        return Id_credencial;
    }
    public void setId_credencial(int id_credencial) {
        Id_credencial = id_credencial;
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

    public Credencial obtenerCredencial() throws SQLException, ClassNotFoundException {
        credencialFactory factory=new credencialFactory(context);
        List<Credencial> Credenciales=(List<Credencial>)factory.select();
        if(Credenciales.size()>0)
            for (Credencial credencial:Credenciales) {
                this.Id_credencial=credencial.getId_credencial();
                this.IdComercio=credencial.get_IdComercio();
                this.sid=credencial.get_sid();

            }
        if(this.IdComercio==null || this.sid==null)
            return null;
        return this;
    }
    public boolean BorrarCredencial() throws SQLException, ClassNotFoundException {
        credencialFactory factory=new credencialFactory(this.context);
        if(factory.delete(this))
            return true;
        return false;
    }

}
