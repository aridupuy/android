package com.cobrodigital.com.cobrodigital2.Modulos.Correo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Boletas.Tareas_asincronicas.Tarea_generar_boleta;
import com.cobrodigital.com.cobrodigital2.Modulos.Correo.Tareas_asincronicas.Tarea_enviar_correo;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

public class Enviar_correo extends Navegacion {
    int nro_Boleta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_correo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        nro_Boleta=this.getIntent().getExtras().getInt(Tarea_generar_boleta.NRO_BOLETA);
        ((Button)findViewById(R.id.btn_enviar_correo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviar_mail(nro_Boleta);
            }
        });
    }
    private boolean enviar_mail(int nro_Boleta){
        Tarea_enviar_correo tarea=new Tarea_enviar_correo(this);
        tarea.execute(nro_Boleta);
        finish();
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion= new Gestor_de_navegacion(this.getApplicationContext());
        navegacion.navegar(item.getItemId());
        return true;
    }
}
