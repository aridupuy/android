package com.cobrodigital.com.cobrodigital2.Modulos.Retiros;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Fragment.Detalle_saldo_fragment;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Retiros extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retiros);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEstadoSaldo);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_retiros);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_retiros);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        Detalle_saldo_fragment fragment=Detalle_saldo_fragment.newInstance();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.include_detalle_saldo_fragment, fragment).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        navegacion.navegar(item.getItemId());
        return true;
    }
}
