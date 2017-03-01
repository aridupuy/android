package com.cobrodigital.com.cobrodigital2.Modulos.Retiros;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.R;

import java.text.DecimalFormat;

public class Retiro_confirmacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retiro_confirmacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRetiro_confirmacion);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_retiro_confirmacion);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_retiro_confirmacion);
        navigationView.setNavigationItemSelectedListener(this);
        Intent intent = getIntent();
        Bundle extras= intent.getExtras();
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        df.setMinimumFractionDigits(2);
        Double saldo_disponible=Double.parseDouble(extras.getString("Disponible"));
        Double plata= Double.parseDouble(extras.getString("plata"));
        ((TextView)findViewById(R.id.saldo_disponible)).setText(df.format(saldo_disponible));
        ((TextView)findViewById(R.id.Nombre)).setText(extras.getString("Nombre"));
        ((TextView)findViewById(R.id.Titular)).setText(extras.getString("Titular"));
        ((TextView)findViewById(R.id.Cuit)).setText(extras.getString("Cuit"));
        ((TextView)findViewById(R.id.Importe_a_acreditar)).setText(df.format(plata));


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        navegacion.navegar(item.getItemId());
        return true;
    }
}
