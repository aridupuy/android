package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Vector;

/**
 * Created by ariel on 20/10/16.
 */
@DatabaseTable(tableName = "Pagador")
public class Pagador extends Model{
    @DatabaseField(id = true,unique = true)
    int id_Pagador;
    @DatabaseField(unique = true)
    Vector<String> campos_variables;


    public Pagador(Context context){
        super(context);
    }
    public Vector<String> get_campos_variables(){
        return campos_variables;
    }
    public void set_campos_variables(Vector<String> campos){
        this.campos_variables=campos;
    }
}
