package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by ariel on 20/10/16.
 */
@DatabaseTable(tableName = "Pagador")
public class Pagador implements Serializable{
    @DatabaseField(dataType= DataType.SERIALIZABLE)
    String []campos_variables;
    public Pagador(){
    }
    public String[] get_campos_variables(){

        return campos_variables;
    }
    public void set_campos_variables(String [] campos){
        this.campos_variables=campos;
    }
}
