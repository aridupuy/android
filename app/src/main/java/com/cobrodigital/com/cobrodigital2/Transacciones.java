package com.cobrodigital.com.cobrodigital2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by Ariel on 28/08/16.
 */
public class Transacciones extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String dato;
    private String fecha_desde;
    private String fecha_hasta;
    public Transacciones() {
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Credencial credencial=new Credencial(getApplicationContext());
        CobroDigital.credencial=credencial.obtenerCredencial();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setDateTimeField();
        try {
            this.listar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTransaccion);
        DrawerLayout drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_transacciones);
        ActionBarDrawerToggle menu = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.setDrawerListener(menu);
        menu.syncState();

    }
    private void setDateTimeField() {
        MenuItem fechaDesde=(MenuItem) findViewById(R.id.fecha_desde);
        //fechaDesde.setOnClickListener(this);
        MenuItem fechaHasta=(MenuItem)findViewById(R.id.fecha_hasta);
        //fechaHasta.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                MenuItem fechaDesde=(MenuItem)findViewById(R.id.fecha_desde);
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fecha_desde=dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                EditText fechaHasta=(EditText)findViewById(R.id.fecha_hasta);
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fecha_hasta=dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    public void onClick(View view) {
        if(view == findViewById(R.id.fecha_desde)) {
            fromDatePickerDialog.show();
        } else if(view == findViewById(R.id.fecha_hasta)) {
            toDatePickerDialog.show();
        }
    }
    public void OnClickVer(View view) throws ParseException {
        try{listar();}catch (ParseException e){System.out.println(e.getMessage());}
    }
    public void OnClickFiltro(View view) throws ParseException {
        listar();
    }
    public void OnclickVolver(View view) {
        finish();
    }
    private void listar() throws ParseException {
        Credencial credencial;
        CobroDigital core = null;
        String hasta=fecha_hasta;
        String desde=fecha_desde;
        Vector<Transaccion>  transacciones=new Vector<Transaccion>();
        HashMap<String,String> variables=obtener_filtros();
        // Transaccion.(getApplicationContext(),variables.get("desde"),variables.get("hasta"),new HashMap<String, String>());
        setContentView(R.layout.activity_transacciones);
        TableLayout tabla = (TableLayout) findViewById(R.id.tabla);
        try {
            System.out.println("Busco en el ws");
            credencial = new Credencial(getApplicationContext());
            core= new CobroDigital(credencial.obtenerCredencial());
            LinkedHashMap filtros=new LinkedHashMap();
            if(variables.size()>0){
                desde=(String)variables.get("desde");
                hasta=(String)variables.get("hasta");
                variables.remove("desde");
                variables.remove("hasta");
            }
            else{
                Date Fecha = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                desde=format.format(Fecha);
                hasta=format.format(Fecha);
            }
            core.consultar_transacciones(desde, hasta, filtros);
            if(core.obtener_resultado().equals("1")){
                Object transicion[]= core.obtener_datos().toArray();
                if(transicion.length>0) {
                    JSONArray datos = new JSONArray((String) transicion[0]);
                    for (int i = 0; i < datos.length(); i++) {
                        Transaccion transaccion = new Transaccion(getApplicationContext());
                        transaccion = transaccion.leerTransaccion(datos.getJSONObject(i));
                        if(transaccion!=null)
                            transacciones.add(transaccion);
                    }
                }
                else{
                    System.out.println("No hay datos disponibles");
                    return;
                }
            }
            else{
                System.out.println("Comunicacion fallida!");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            e.getMessage();
            e.getLocalizedMessage();
            System.out.println(core.obtener_log());
        }
        for(Transaccion transaccion: transacciones) {
            TableRow row = new TableRow(this);
            row.setBackgroundResource(R.drawable.celda);
            TextView celda = new TextView(this);
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setText(transaccion.get_Fecha());
            row.addView(celda);
            celda = new TextView(this);
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setText(transaccion.get_Nro_boleta());
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            row.addView(celda);
            celda = new TextView(this);
            celda.setText( transaccion.get_Identificacion());
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            row.addView(celda);
            celda = new TextView(this);
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setText( transaccion.get_Nombre());
            row.addView(celda);
            celda = new TextView(this);
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setText( transaccion.get_Info());
            row.addView(celda);
            celda = new TextView(this);
            Object concepto = transaccion.get_Concepto();
            if (concepto.toString() == "null")
                concepto = "";
            celda.setText((String)concepto);
            celda.setTextSize(9);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setPadding(5, 5, 5, 5);
            row.addView(celda);
            celda = new TextView(this);
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setText( transaccion.get_Bruto());
            row.addView(celda);
            celda = new TextView(this);
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setText( transaccion.get_Comision());
            row.addView(celda);
            celda = new TextView(this);
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setText(String.valueOf(transaccion.get_Neto()));
            row.addView(celda);
            celda = new TextView(this);
            celda.setTextSize(9);
            celda.setPadding(5, 5, 5, 5);
            celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
            celda.setText( transaccion.get_Saldo_acumulado());
            row.addView(celda);
            tabla.addView(row);
        }
    }
    private HashMap<String,String> obtener_filtros () throws ParseException{
        HashMap<String , String > filtros=new HashMap<String, String>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        String fecha;
        if(fecha_desde!=null){
            fecha=dateFormatter.format(fecha_desde);
            filtros.put("desde",fecha);
        }
        if(fecha_hasta!=null){
            fecha=dateFormatter.format(fecha_hasta);
            filtros.put("hasta",fecha);
        }
        //EditText nro_boleta = (EditText) findViewById(R.id.Nro_boleta);
       /* EditText id = (EditText) findViewById(R.id.ID);
        EditText nombre = (EditText) findViewById(R.id.Nombre);
        EditText ingresos = (EditText) findViewById(R.id.Ingresos);
        Date date=dateFormatter.parse(textdesde.getText().toString());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        String desde=dateFormatter.format(date);
        if(desde!=null || desde!="")
            filtros.put("desde",desde);
        date=this.dateFormatter.parse(texthasta.getText().toString());
        String hasta=dateFormatter.format(date);
        if(hasta!=null || hasta!="")
            filtros.put("hasta",hasta);
        if(nro_boleta!=null && "" != nro_boleta.getText().toString())
            filtros.put("nro_boleta",nro_boleta.getText().toString());
        if(id!=null &&"" != id.getText().toString())
            filtros.put("id",id.getText().toString());
        if(nombre!=null &&"" != nombre.getText().toString())
            filtros.put("nombre",nombre.getText().toString());
        if(ingresos!=null &&"" != ingresos.getText().toString())
            filtros.put("ingresos",ingresos.getText().toString());*/
        return filtros;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        System.out.println("vouasdasdasdasdasd");
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        System.out.println("voy por aca");
        if (id == R.id.fecha_desde) {
            fecha_desde=crear_alert_dialog();

        } else if (id == R.id.fecha_hasta) {
            fecha_hasta=crear_alert_dialog();
        }  else if (id == R.id.nav_manage) {


        } else if (id == R.id.nav_share) {

        }
        try {
            listar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_transacciones);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    protected String crear_alert_dialog(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Ingrese filtro");
        final EditText input = (EditText) findViewById(R.id.dato);
        alert.setView(R.layout.menu_emergente);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dato=input.getText().toString();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog a = alert.create();
        a.show();
        return dato;
    }
}
