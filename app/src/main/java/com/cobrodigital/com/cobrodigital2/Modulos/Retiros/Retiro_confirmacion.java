package com.cobrodigital.com.cobrodigital2.Modulos.Retiros;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Tareas_asincronicas.Tarea_comisiones;
import com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Tareas_asincronicas.Tarea_retiros;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Retiro_confirmacion extends Navegacion {
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
        final String plata_retiro=getIntent().getStringExtra("Plata");
        final String disponible=getIntent().getStringExtra("Disponible");
        final Double plata= Double.parseDouble(plata_retiro);
        final Double saldo_disponible=Double.parseDouble(disponible.replace(",",""));
        final String nombre=getIntent().getStringExtra("Nombre");
        final String titular=getIntent().getStringExtra("Titular");
        final String cuit=getIntent().getStringExtra("Cuit");
        Tarea_comisiones tarea=new Tarea_comisiones(this);
        tarea.execute(plata,saldo_disponible);
        ((TextView)findViewById(R.id.disponible)).setText(df.format(saldo_disponible));
        ((TextView)findViewById(R.id.Importe)).setText(df.format(plata));
        ((TextView)findViewById(R.id.Destino)).setText(nombre.substring(0,13)+"...");
        ((Button)findViewById(R.id.confirma)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    realizar_peticion_retiro(cuit,titular,plata,nombre);
                }
        });
        ((Button)findViewById(R.id.no_confirma)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    finish();
                }
        });
        ((Button)findViewById(R.id.error_acepta)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    finish();
                }
        });

    }
    private void realizar_peticion_retiro(String cuit,String titular,Double plata,String nombre) {
        Tarea_retiros tarea = new Tarea_retiros(this,this);
        tarea.execute(cuit,titular,plata,nombre);
    }

    @Override
    public void processFinish(Boolean resultado) {
        Intent intent = this.getIntent();
        if(!resultado){
            for (Object log : CobroDigital.webservice.webservice_retiro.obtener_log()){
                Gestor_de_mensajes_usuario.mensaje(log.toString(),getApplicationContext());
                this.setResult(RESULT_CANCELED, intent);
                finish();
            }
        }
        else{
            Gestor_de_mensajes_usuario.mensaje("Petición generada correctamente.",this);
            this.setResult(RESULT_OK, intent);
            finish();
        }
    }
}
