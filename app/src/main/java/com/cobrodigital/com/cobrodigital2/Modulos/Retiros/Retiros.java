package com.cobrodigital.com.cobrodigital2.Modulos.Retiros;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.R;

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

//        Detalle_saldo_fragment fragment=Detalle_saldo_fragment.newInstance();
//        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.include_detalle_saldo_fragment, fragment).commit();
//        FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
//        Fragment_bancos fragment2= Fragment_bancos.newInstance();
//        ft2.add(R.id.include_fragment_bancos, fragment2).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        navegacion.navegar(item.getItemId());
        return true;
    }
}
