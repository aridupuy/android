package com.cobrodigital.com.cobrodigital2;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Created by Ariel on 28/08/16.
 */
public class Pagador extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_pagador);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener();
//
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    public void OnClickPagadorNew(View view) {
        //setContentView(R.layout.main);
        //String id_comercio="KA289659";
        String id_comercio = "CI366779";
        //String sid="V2nJUHv7110BI4v1FLxdeQrFlWUw08j5L3VAxZB3P9Dm0EJbsDW5vJsi960";
        String sid = "MeAOO0d8tpk87Ud3AG0mZO7WCIP76GuKfU48UMVCuLO66aQGa0Iw3R6cDVs";
        CobroDigital cd = null;
        try {
            cd = new CobroDigital(id_comercio, sid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        EditText nombre = (EditText) findViewById(R.id.nombre);
        EditText identificador = (EditText) findViewById(R.id.identificador);
        EditText apellido = (EditText) findViewById(R.id.apellido);
        LinkedHashMap pagador = new LinkedHashMap();
        //System.out.println("nombre "+nombre.getText().toString());
        pagador.put("nombre", nombre.getText().toString());
        pagador.put("identificador", identificador.getText().toString());
        pagador.put("apellido", apellido.getText().toString());

        try {
            cd.crear_pagador(pagador);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(cd.obtener_log());
        Vector vector = cd.obtener_log();
        Object[] log = vector.toArray();
        String respuesta = "";
        if (log != null)
            for (int i = 0; i < log.length; i++) {
                respuesta += (String) log[i] + " \n";
            }
        Vector vectordatos = cd.obtener_datos();
        Object[] datos = vectordatos.toArray();
        if (datos != null)
            for (int i = 0; i < datos.length; i++) {
                respuesta += (String) datos[i] + " \n";
            }
        TextView respuestatxt = (TextView) findViewById(R.id.respuesta);
        respuestatxt.setText(respuesta);
    }
    public void OnclickVolver(View view) {
        finish();
    }

}
