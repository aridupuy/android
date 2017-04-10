package com.cobrodigital.com.cobrodigital2.Modulos.Cobrar_por_correo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Cobrar_por_correo.Tareas_asincronicas.Tarea_cobrar_por_correo;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Cobrar_por_correo extends Navegacion {
    final Calendar calendario = Calendar.getInstance();
    String date_1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cobrar_por_correo);
        findViewById(R.id.cobrarxcorreo_ok).setVisibility(View.GONE);
        findViewById(R.id.cobrarxcorreo_fail).setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_boletas);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_boletas);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        Button button = (Button) findViewById(R.id.enviar_cobxcorreo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre=((EditText)findViewById(R.id.Nombre_cobxcorreo)).getText().toString();
                String apellido=((EditText)findViewById(R.id.apellido_cobxcorreo)).getText().toString();
                String email=((EditText)findViewById(R.id.correo_cobxcorreo)).getText().toString();
                String concepto=((EditText)findViewById(R.id.concepto_cobxcorreo)).getText().toString();
                String documento=((EditText)findViewById(R.id.documento_cobxcorreo)).getText().toString();
                String direccion=((EditText)findViewById(R.id.direccion_cobxcorreo)).getText().toString();
                String fecha= date_1;
                String importe=((EditText)findViewById(R.id.importecobrarxcorreo)).getText().toString();
                generar_boleta(nombre,apellido,concepto,email,documento,direccion,fecha,importe);
            }
        });
        findViewById(R.id.fecha_cobrarxcorreo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                si_elige_fecha(R.id.fecha_cobrarxcorreo);
            }
        });
    }
    private void actualizar_fecha(int id,Calendar calendario){
        SimpleDateFormat formatter_vista= new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter_back= new SimpleDateFormat("yyyyMMdd");
        ((TextView)findViewById(id)).setText(formatter_vista.format(calendario.getTime()));
        if(id==R.id.fecha_cobrarxcorreo)
            date_1=formatter_back.format(calendario.getTime());
    }
    public void si_elige_fecha(final int id){
        new DatePickerDialog(Cobrar_por_correo.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, monthOfYear);
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                actualizar_fecha(id,calendario);
            }
        }, calendario
                .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();

    }
    private void generar_boleta(String nombre,String apellido,String concepto,String email,String documento,String direccion,String fecha,String importe){
        final boolean[] result = new boolean[1];
        Tarea_cobrar_por_correo cobrar= new Tarea_cobrar_por_correo(this, new Tarea_cobrar_por_correo.AsyncResponse() {
            @Override
            public void processFinish(Boolean resultado) {
                if(resultado){
                    findViewById(R.id.cobrarxcorreo_fail).setVisibility(View.GONE);
                    findViewById(R.id.cobrarxcorreo_ok).setVisibility(View.VISIBLE);


                }
                else{
                    findViewById(R.id.cobrarxcorreo_fail).setVisibility(View.VISIBLE);
                    findViewById(R.id.cobrarxcorreo_ok).setVisibility(View.GONE);
                }
            ((LinearLayout)findViewById(R.id.progressbar_Boleta)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.vista_cobxcorreo)).setVisibility(View.VISIBLE);
            }
        });
        cobrar.execute(nombre,apellido,documento,email,direccion,concepto,fecha,importe);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion = new Gestor_de_navegacion(getApplicationContext());
        return navegacion.navegar(item.getItemId());
    }

}
