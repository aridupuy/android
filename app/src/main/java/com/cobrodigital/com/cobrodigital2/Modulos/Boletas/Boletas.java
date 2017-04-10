package com.cobrodigital.com.cobrodigital2.Modulos.Boletas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Boletas.Tareas_asincronicas.Tarea_estructura;
import com.cobrodigital.com.cobrodigital2.Modulos.Boletas.Tareas_asincronicas.Tarea_generar_boleta;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.Config;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Boletas extends Navegacion {

    final Calendar calendario = Calendar.getInstance();
    String date_1;
    String date_2;
    String date_3;
    int nro_boleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boletas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_boletas);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_boletas);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        this.cargar_spiner();
        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String campo_a_buscar=((Spinner)findViewById(R.id.campo_a_buscar)).getSelectedItem().toString();
                String identificador=((EditText)findViewById(R.id.identificador)).getText().toString();
                String concepto=((EditText)findViewById(R.id.concepto)).getText().toString();
                String modelo= CobroDigital.config.get(Config.POSICION_MODELO_BOLETA);
                String fecha_1 = date_1;
                String importe_1=((EditText)findViewById(R.id.importe_1)).getText().toString();
                String fecha_2=null;
                String fecha_3=null;
                String importe_2=null;
                String importe_3=null;
                if(findViewById(R.id.Segundo_vencimiento).getVisibility()==View.VISIBLE){
                    fecha_2 =  date_2;
                    importe_2 = ((EditText) findViewById(R.id.importe_2)).getText().toString();
                }
                if(findViewById(R.id.Tercer_vencimiento).getVisibility()==View.VISIBLE){
                    fecha_3 =  date_3;
                    importe_3 = ((EditText) findViewById(R.id.importe_3)).getText().toString();
                }
                generar_boleta(campo_a_buscar,identificador,concepto,fecha_1,importe_1,modelo,fecha_2,importe_2,fecha_3,importe_3);
            }
        });
        this.efectos_de_formulario();

   }
    private void generar_boleta(String identificador, String campo_a_buscar, String concepto, String fecha_1, String importe_1, String modelo, String fecha_2, String importe_2, String fecha_3, String importe_3){
        if(!validar_formulario())
            return;
        Tarea_generar_boleta tarea=new Tarea_generar_boleta(this);
        tarea.execute(identificador,campo_a_buscar,concepto,fecha_1,importe_1,modelo,fecha_2,importe_2,fecha_3,importe_3);

    }
    private void cargar_spiner(){
        Tarea_estructura estructura = new Tarea_estructura(this);
        estructura.execute();
    }
    private void actualizar_fecha(int id,Calendar calendario){
        SimpleDateFormat formatter_vista= new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter_back= new SimpleDateFormat("yyyyMMdd");
        ((TextView)findViewById(id)).setText(formatter_vista.format(calendario.getTime()));
        date_1=formatter_back.format(calendario.getTime());
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion = new Gestor_de_navegacion(getApplicationContext());
        return navegacion.navegar(item.getItemId());
    }
    private void efectos_de_formulario(){
        ((Button)findViewById(R.id.add1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LinearLayout)findViewById(R.id.Segundo_vencimiento)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.add1)).setVisibility(View.GONE);
                ((Button)findViewById(R.id.add2)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.sup1)).setVisibility(View.VISIBLE);

            }
        });
        ((Button)findViewById(R.id.add2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LinearLayout)findViewById(R.id.Tercer_vencimiento)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.add2)).setVisibility(View.GONE);
                ((Button)findViewById(R.id.sup2)).setVisibility(View.VISIBLE);
            }
        });

        ((Button)findViewById(R.id.sup1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LinearLayout)findViewById(R.id.Segundo_vencimiento)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.Tercer_vencimiento)).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.fecha_2)).setText("");
                ((TextView)findViewById(R.id.fecha_3)).setText("");
                ((EditText)findViewById(R.id.importe_2)).setText("");
                ((EditText)findViewById(R.id.importe_3)).setText("");
                ((Button)findViewById(R.id.add1)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.add2)).setVisibility(View.GONE);
                ((Button)findViewById(R.id.sup1)).setVisibility(View.GONE);
                ((Button)findViewById(R.id.sup2)).setVisibility(View.GONE);

            }
        });
        ((Button)findViewById(R.id.sup2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LinearLayout)findViewById(R.id.Tercer_vencimiento)).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.fecha_3)).setText("");
                ((EditText)findViewById(R.id.importe_3)).setText("");
                ((Button)findViewById(R.id.add2)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.sup2)).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.fecha_1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                si_elige_fecha(R.id.fecha_1);

            }
        });
      findViewById(R.id.fecha_2).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              si_elige_fecha(R.id.fecha_2);

          };
      });
      findViewById(R.id.fecha_3).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              si_elige_fecha(R.id.fecha_3);

          };
      });
//        findViewById(R.id.fecha_2).setOnClickListener(clicklistener);
//        findViewById(R.id.fecha_3).setOnClickListener(clicklistener);
    }
    public void si_elige_fecha(final int id){
        new DatePickerDialog(Boletas.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, monthOfYear);
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                actualizar_fecha(id,calendario);
            }
        }, calendario
                .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();

    }
//    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//    };
    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    private boolean validar_formulario(){
        Gestor_de_mensajes_usuario.context=getApplicationContext();
        ((Spinner)findViewById(R.id.campo_a_buscar)).setBackgroundResource(0);
        ((EditText)findViewById(R.id.identificador)).setBackgroundResource(0);
        ((EditText)findViewById(R.id.concepto)).setBackgroundResource(0);
        ((TextView)findViewById(R.id.fecha_1)).setBackgroundResource(0);
        ((EditText)findViewById(R.id.importe_1)).setBackgroundResource(0);
        ((TextView)findViewById(R.id.fecha_2)).setBackgroundResource(0);
        ((EditText)findViewById(R.id.importe_2)).setBackgroundResource(0);
        ((TextView)findViewById(R.id.fecha_3)).setBackgroundResource(0);
        ((EditText)findViewById(R.id.importe_3)).setBackgroundResource(0);
        if(((Spinner)findViewById(R.id.campo_a_buscar)).getSelectedItem().toString().equals("") ||((Spinner)findViewById(R.id.campo_a_buscar)).getSelectedItem().toString().equals("campo para buscar")){
            Gestor_de_mensajes_usuario.mensaje("Falta seleccionar el campo a buscar");
            ((Spinner)findViewById(R.id.campo_a_buscar)).setBackgroundResource(R.drawable.borde_error);
            return false;
        }
        if((((EditText)findViewById(R.id.identificador)).getText().toString()).equals("")){
            Gestor_de_mensajes_usuario.mensaje("Falta Ingresar el Valor a buscar");
            ((EditText)findViewById(R.id.identificador)).setBackgroundResource(R.drawable.borde_error);
            return false;
        }
        if((((EditText)findViewById(R.id.concepto)).getText().toString()).equals("")){
            Gestor_de_mensajes_usuario.mensaje("Falta Ingresar el Concepto");
            ((EditText)findViewById(R.id.concepto)).setBackgroundResource(R.drawable.borde_error);
            return false;
        }
        if((((TextView)findViewById(R.id.fecha_1)).getText().toString()).equals("")){
            Gestor_de_mensajes_usuario.mensaje("Falta Ingresar la Fecha de vencimiento");
            ((TextView)findViewById(R.id.fecha_1)).setBackgroundResource(R.drawable.borde_error);
            return false;
        }
        if((((EditText)findViewById(R.id.importe_1)).getText().toString()).equals("")){
            Gestor_de_mensajes_usuario.mensaje("Falta Ingresar el Importe ");
            ((EditText)findViewById(R.id.importe_1)).setBackgroundResource(R.drawable.borde_error);
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        int visi=((LinearLayout)findViewById(R.id.Segundo_vencimiento)).getVisibility();
        if(((LinearLayout)findViewById(R.id.Segundo_vencimiento)).getVisibility()!=View.GONE) {
            if(!(((TextView)findViewById(R.id.fecha_2)).getText().toString()).equals("") && (((EditText)findViewById(R.id.importe_2)).getText().toString()).equals("")){
                Gestor_de_mensajes_usuario.mensaje("Falta Ingresar El importe del segundo vencimiento");
                ((EditText)findViewById(R.id.importe_2)).setBackgroundResource(R.drawable.borde_error);
                return false;
            }
            try {
                if (sdf.parse(((TextView) findViewById(R.id.fecha_2)).getText().toString()).compareTo(sdf.parse((((TextView) findViewById(R.id.fecha_1)).getText().toString()))) <= 0) {
                    Gestor_de_mensajes_usuario.mensaje("La Segunda fecha de vencimiento no puede ser menor o igual a la anterior");
                    ((TextView) findViewById(R.id.fecha_2)).setBackgroundResource(R.drawable.borde_error);
                    return false;
                }
            } catch (ParseException e) {
                Log.wtf("error", e.getMessage());
                Gestor_de_mensajes_usuario.mensaje("Las 2da fecha no es correcta");
                return false;
            }
            if(!((EditText)findViewById(R.id.importe_2)).getText().toString().equals("") && Double.parseDouble((((EditText)findViewById(R.id.importe_2)).getText().toString()))<=Double.parseDouble((((EditText)findViewById(R.id.importe_1)).getText().toString()))){
                Gestor_de_mensajes_usuario.mensaje("El importe del Segundo vencimiento no puede ser menor o igual al anterior");
                ((EditText)findViewById(R.id.importe_2)).setBackgroundResource(R.drawable.borde_error);
                return false;
            }
        }
        if(((LinearLayout)findViewById(R.id.Tercer_vencimiento)).getVisibility()!=View.GONE ) {
            if(!(((TextView)findViewById(R.id.fecha_3)).getText().toString()).equals("")&& (((EditText)findViewById(R.id.importe_3)).getText().toString()).equals("")){
                Gestor_de_mensajes_usuario.mensaje("Falta Ingresar El importe del Tercer vencimiento");
                ((EditText)findViewById(R.id.importe_3)).setBackgroundResource(R.drawable.borde_error);
                return false;
            }
            try {
                if (sdf.parse(((TextView) findViewById(R.id.fecha_3)).getText().toString()).compareTo(sdf.parse((((TextView) findViewById(R.id.fecha_2)).getText().toString()))) <= 0) {
                    Gestor_de_mensajes_usuario.mensaje("La Tercer fecha de vencimiento no puede ser menor o igual a la anterior");
                    ((EditText) findViewById(R.id.importe_3)).setBackgroundResource(R.drawable.borde_error);
                    return false;
                }
            } catch (ParseException e) {
                Log.wtf("error", e.getMessage());
                Gestor_de_mensajes_usuario.mensaje("La 3era fecha no es correcta");
                return false;
            }
            if(!((EditText)findViewById(R.id.importe_3)).getText().toString().equals("") && Double.parseDouble((((EditText)findViewById(R.id.importe_3)).getText().toString()))<=Double.parseDouble((((EditText)findViewById(R.id.importe_2)).getText().toString()))){
                Gestor_de_mensajes_usuario.mensaje("El importe del Tercer vencimiento no puede ser menor o igual al anterior");
                ((EditText)findViewById(R.id.importe_3)).setBackgroundResource(R.drawable.borde_error);
                return false;
            }
        }
        return true;
    }
}
