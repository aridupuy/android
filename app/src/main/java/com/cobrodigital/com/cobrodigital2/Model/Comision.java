package com.cobrodigital.com.cobrodigital2.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ariel on 03/03/17.
 */

public class Comision{

    String Monto_pagador;
    String Pag_fix;
    String Pag_var;
    String Monto_cd;
    String Cdi_fix;
    String Cdi_var;
    String Monto_marchand;
    public Comision(String dato) throws JSONException {
        ///dudoso!!!!!!
        JSONObject Json=new JSONObject(dato);
        setMonto_pagador(Json.getString("monto_pagador"));
        setPag_fix(Json.getString("pag_fix"));
        setPag_var(Json.getString("pag_var"));
        setMonto_cd(Json.getString("monto_cd"));
        setCdi_fix(Json.getString("cdi_fix"));
        setCdi_var(Json.getString("cdi_var"));
        setMonto_marchand(Json.getString("monto_marchand"));
    }

    public String getMonto_pagador() {
        return Monto_pagador;
    }

    public void setMonto_pagador(String monto_pagador) {
        Monto_pagador = monto_pagador;
    }

    public String getPag_fix() {
        return Pag_fix;
    }

    public void setPag_fix(String pag_fix) {
        Pag_fix = pag_fix;
    }

    public String getPag_var() {
        return Pag_var;
    }

    public void setPag_var(String pag_var) {
        Pag_var = pag_var;
    }

    public String getMonto_cd() {
        return Monto_cd;
    }

    public void setMonto_cd(String monto_cd) {
        Monto_cd = monto_cd;
    }

    public String getCdi_fix() {
        return Cdi_fix;
    }

    public void setCdi_fix(String cdi_fix) {
        Cdi_fix = cdi_fix;
    }

    public String getCdi_var() {
        return Cdi_var;
    }

    public void setCdi_var(String cdi_var) {
        Cdi_var = cdi_var;
    }

    public String getMonto_marchand() {
        return Monto_marchand;
    }

    public void setMonto_marchand(String monto_marchand) {
        Monto_marchand = monto_marchand;
    }
}
