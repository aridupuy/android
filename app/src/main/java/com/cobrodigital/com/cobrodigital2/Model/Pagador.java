package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;

import java.util.Vector;

/**
 * Created by ariel on 20/10/16.
 */

public class Pagador extends Model{
    int id;
    final private String ID_Tabla="id_pagador";
    Vector<String> campos_variables;
    private String unique=ID_Tabla;


    public Pagador(Context context){
        super(context);


    }
    public Vector<String> get_campos_variables(){
        return campos_variables;
    }
    public void set_campos_variables(Vector<String> campos){
        this.campos_variables=campos;
    }
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getid_tabla() {
        return ID_Tabla;
    }

    @Override
    public String getunique() {
        return unique;
    }

}
