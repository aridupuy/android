package com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Fragment.Detalle_saldo_fragment;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

public class Detalle_saldo extends Navegacion {
    protected String saldo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_saldo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEstadoSaldo);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_estado_saldo);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_estado_saldo);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        if(getIntent().hasExtra("Saldo"))
            saldo=getIntent().getStringExtra("Saldo");
        Detalle_saldo_fragment fragment=Detalle_saldo_fragment.newInstance(saldo);
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft=ft.add(R.id.include_detalle_saldo, fragment);
        int res=ft.commit();
        Log.d("res",res+"");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        return  navegacion.navegar(item.getItemId());
    }
}
