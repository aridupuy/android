package com.cobrodigital.com.cobrodigital2.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ariel on 21/02/17.
 */

public class Banco {
    String Nombre;
    String Titular;
    String cuit;
    public Banco(String dato) throws JSONException {
        JSONObject Json=new JSONObject(dato);
        setNombre(Json.getString("Nombre"));
        setTitular(Json.getString("Titular"));
        setCuit(Json.getString("CUIT"));
    }
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTitular() {
        return Titular;
    }

    public void setTitular(String titular) {
        Titular = titular;
    }
}
