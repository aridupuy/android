package com.cobrodigital.com.cobrodigital2.Modulos.Retiros;

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
import com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Tareas_asincronicas.Tarea_comisiones;
import com.cobrodigital.com.cobrodigital2.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
        Bundle bundle= getIntent().getExtras();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("###,###,###,###.##", otherSymbols);
        df.setDecimalSeparatorAlwaysShown(true);
        df.setMinimumFractionDigits(2);
        String plata_retiro=getIntent().getStringExtra("Plata");
        String disponible=getIntent().getStringExtra("Disponible");
        Double plata= Double.parseDouble(plata_retiro);
        Double saldo_disponible=Double.parseDouble(disponible);
        String nombre=getIntent().getStringExtra("Nombre");

        String titular=getIntent().getStringExtra("Titular");
        String cuit=getIntent().getStringExtra("Cuit");

        Tarea_comisiones tarea=new Tarea_comisiones(this);
        tarea.execute(plata);
        ((TextView)findViewById(R.id.disponible)).setText(df.format(saldo_disponible));
        ((TextView)findViewById(R.id.Importe)).setText(df.format(plata));
        ((TextView)findViewById(R.id.Destino)).setText(nombre.substring(0,13)+"...");

//        ((TextView)findViewById(R.id.Cuit)).setText(cuit);
//        ((TextView)findViewById(R.id.Cuit)).setText(cuit);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        navegacion.navegar(item.getItemId());
        return true;
    }
}
