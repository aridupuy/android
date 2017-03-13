package com.cobrodigital.com.cobrodigital2.Modulos.Transacciones;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.fragment.Detalle_transacciones_fragment;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

public class Detalle_transacciones extends Navegacion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_transaccion);
        /////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalle_transaccion);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_detalle_transaccion);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_detalle_transaccion);
        navigationView.setNavigationItemSelectedListener(this);

        Detalle_transacciones_fragment fragment=Detalle_transacciones_fragment.newInstance(getIntent().getExtras(),this.getApplication().getApplicationContext());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft=ft.add(R.id.include_detalle_transacciones_activity, fragment);
        int res=ft.commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_detalle_transaccion);
        drawer.closeDrawer(GravityCompat.START);
        return navegacion.navegar(item.getItemId());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

}
