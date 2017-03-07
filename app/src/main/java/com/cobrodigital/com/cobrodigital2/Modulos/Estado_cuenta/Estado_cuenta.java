package com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Tareas_asincronicas.Tarea_estado_cuenta;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

import org.json.JSONException;

import java.text.ParseException;

public class Estado_cuenta extends Navegacion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_cuenta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEstadoCuenta);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_estado_cuenta);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_estado_cuenta);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        try {
            dibujar();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((LinearLayout)findViewById(R.id.layout_saldo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Estado_cuenta.this,Detalle_saldo.class);
                String saldo=((TextView)findViewById(R.id.saldo_cuenta)).getText().toString();
                intent.putExtra("Saldo",saldo);
                startActivity(intent);
            }
        });
    }
    protected void dibujar() throws JSONException, ParseException {
        Tarea_estado_cuenta tarea= new Tarea_estado_cuenta(this);
        tarea.execute();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion = new Gestor_de_navegacion(getApplicationContext());
        return navegacion.navegar(item.getItemId());
    }
}
