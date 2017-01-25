package com.cobrodigital.com.cobrodigital2.Modulos.Boletas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Modulos.Correo.Enviar_correo;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Model.Boleta;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class Boletas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String NRO_BOLETA = "nro_boleta";
    final Calendar calendario = Calendar.getInstance();
    String date_1;
    String date_2;
    String date_3;
    int nro_boleta;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boletas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                String modelo="init";

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
    protected void generar_boleta(String identificador, String campo_a_buscar, String concepto, String fecha_1, String importe_1, String modelo, String fecha_2, String importe_2, String fecha_3, String importe_3){
        if(!validar_formulario())
            return;
        try {
            Vector<Boleta> boletas=new Vector<Boleta>();
            if((nro_boleta=CobroDigital.webservice.webservice_boleta.generar_boleta(identificador, campo_a_buscar, concepto, fecha_1, importe_1, modelo, fecha_2, importe_2, fecha_3, importe_3))!=-1) {
                Intent intent= new Intent(Boletas.this,Enviar_correo.class);
                intent.putExtra(NRO_BOLETA,nro_boleta);
                startActivity(intent);
                Gestor_de_mensajes_usuario.mensaje("Boleta generada correctamente.",getApplicationContext());
            }
            else{
                Gestor_de_mensajes_usuario.mensaje(CobroDigital.webservice.obtener_log().toString());
                ///continuar desde aca para hacer que mande mail tambien;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Gestor_de_mensajes_usuario.mensaje(e.getMessage(),getApplicationContext());
        }

    }
    private void cargar_spiner(){

        List<String> estructura_clientes=new ArrayList<String>();
        estructura_clientes.add("campo para buscar");
        try {
            if(CobroDigital.webservice.webservice_pagador.consultar_estructura_pagadores()=="1"){
                Vector datos=CobroDigital.webservice.obtener_datos();
                for (int i=0; i < datos.size();i++ ) {
                    estructura_clientes.add(((String) datos.get(i)).replace("\"",""));
                }
            }
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
        Spinner spinner= (Spinner) findViewById(R.id.campo_a_buscar);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estructura_clientes);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)findViewById(R.id.identificador)).setHint("Ingrese "+((String)adapterView.getItemAtPosition(i)).toLowerCase());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }
    private void actualizar_fecha(int id,Calendar calendario){
        SimpleDateFormat formatter_vista= new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter_back= new SimpleDateFormat("yyyyMMdd");
        ((TextView)findViewById(id)).setText(formatter_vista.format(calendario.getTime()));
        if(id==R.id.fecha_1)
            date_1=formatter_back.format(calendario.getTime());
        if(id==R.id.fecha_2)
            date_2=formatter_back.format(calendario.getTime());
        if(id==R.id.fecha_3)
            date_3=formatter_back.format(calendario.getTime());
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
        View.OnClickListener clicklistener= new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Boletas.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
                actualizar_fecha(v.getId(),calendario);
            }
        };
        findViewById(R.id.fecha_1).setOnClickListener(clicklistener);
        findViewById(R.id.fecha_2).setOnClickListener(clicklistener);
        findViewById(R.id.fecha_3).setOnClickListener(clicklistener);
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

