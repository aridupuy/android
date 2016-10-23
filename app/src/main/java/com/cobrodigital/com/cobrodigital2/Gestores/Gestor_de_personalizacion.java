package com.cobrodigital.com.cobrodigital2.Gestores;

import android.content.Context;
import android.database.Cursor;

import com.cobrodigital.com.cobrodigital2.Model.Pagador;
import com.cobrodigital.com.cobrodigital2.Model.Personalizacion;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * Created by ariel on 20/10/16.
 */

public class Gestor_de_personalizacion {
        public static boolean set_estructura_clientes(Context context){
            try{
                System.out.println("Pagador1");
                CobroDigital nucleo =new CobroDigital(CobroDigital.credencial);
                if(nucleo.consultar_estructura_pagadores()=="1" +
                        ""){
                    Personalizacion personalizacion=new Personalizacion();
                    personalizacion.estructura=nucleo.obtener_datos();
                }
                Pagador pagador=new Pagador(context);
                pagador.set_campos_variables(Personalizacion.estructura);
                pagador.guardar(pagador);
                return true;
            }catch (Exception e){
                System.out.println("error");
                System.out.println(e.getStackTrace());
                System.out.println(e.getLocalizedMessage());
                System.out.println(e.getMessage());
                System.out.println(e);
                return false;
            }
        }
        public Vector<String> get_estructura_clientes(Context context) throws SQLException {
            Pagador pagador=new Pagador(context);
            List<Object> pagadores=pagador.select();
            if(pagadores.size()<0){
                return null;
            }
            pagador=(Pagador)(pagadores.get(0));
            return pagador.get_campos_variables();
        }
}
