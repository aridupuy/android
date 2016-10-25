package com.cobrodigital.com.cobrodigital2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.content.Loader;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.AsyncTask.Tabla_transaccion;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
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
import java.util.Objects;
import java.util.Vector;

import static android.R.attr.data;
import static android.R.attr.process;

/**
 * Created by Ariel on 28/08/16.
 */
public class Transacciones extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int cantidad_transacciones_a_mostrar=10;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String dato;
    public static String fecha_desde;
    public static String fecha_hasta;
    public static Context context;
    public Transacciones() {
    }
    protected void onCreate(Bundle savedInstanceState) {
        context=this.getApplicationContext();
        super.onCreate(savedInstanceState);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setDateTimeField();
        try {
            this.listar(R.layout.app_bar_transacciones);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView vermas=(TextView)findViewById(R.id.transascompletas);
        if(vermas!=null)
        vermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int anterior=cantidad_transacciones_a_mostrar;
                    cantidad_transacciones_a_mostrar=0;
                    listar(R.layout.app_bar_transacciones_completas);
                    cantidad_transacciones_a_mostrar=anterior;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
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
    public void OnclickVolver(View view) {
        finish();
    }
    @SuppressLint("WrongViewCast")
    private  void listar(int vista) throws ParseException {

        String hasta = fecha_hasta;
        String desde = fecha_desde;
        Vector<Transaccion> transacciones = new Vector<Transaccion>();
        HashMap<String, String> variables = obtener_filtros();
        //View layout=View.inflate(getApplicationContext(),R.layout.activity_transacciones,null);
        setContentView(R.layout.activity_transacciones);
        LayoutInflater loiViewInflater = LayoutInflater.from(getBaseContext());
        View mview = loiViewInflater.inflate(vista, null);
        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT,
                HorizontalScrollView.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addContentView(mview, params);
        Tabla_transaccion tabla=new Tabla_transaccion(getApplicationContext(),transacciones);
        transacciones=(Vector<Transaccion>)tabla.loadInBackground();
        final ProgressBar estado=(ProgressBar)findViewById(R.id.progressbar);
        estado.setVisibility(View.VISIBLE);
        final android.content.Loader.OnLoadCompleteListener<Vector<Transaccion>> listener = new android.content.Loader.OnLoadCompleteListener <Vector<Transaccion>>() {
            @Override
            public void onLoadComplete(android.content.Loader<Vector<Transaccion>> loader, Vector<Transaccion> transacciones) {
                Transaccion transaccion = null;
                String saldo = "";
                TableLayout tabla_layout = (TableLayout) findViewById(R.id.tabla_layout);
                for (int i = 0; (i < cantidad_transacciones_a_mostrar || cantidad_transacciones_a_mostrar == 0) && i < transacciones.size(); i++) {
                    transaccion = (Transaccion) transacciones.get(i);
                    TableRow row;
                    row = new TableRow(tabla_layout.getContext());
                    row.setGravity(Gravity.NO_GRAVITY);
                    row.setBackgroundResource(R.drawable.celda);
                    TextView celda = new TextView(tabla_layout.getContext());
                    celda.setTextSize(9);
                    celda.setPadding(10, 10, 10, 10);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_TEXT_START);
                    celda.setText(transaccion.getFecha());
                    row.addView(celda);
                    celda = new TextView(tabla_layout.getContext());
                    celda.setTextSize(9);
                    celda.setPadding(10, 10, 10, 10);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_TEXT_START);
                    celda.setText(transaccion.getNro_boleta());
                    celda.setTextSize(9);
                    celda.setPadding(10, 10, 10, 10);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_TEXT_START);
                    row.addView(celda);
                    celda = new TextView(tabla_layout.getContext());
                    celda.setTextSize(9);
                    celda.setPadding(10, 10, 10, 10);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_TEXT_START);
                    celda.setText(transaccion.getIdentificacion());
                    row.addView(celda);
                    celda = new TextView(tabla_layout.getContext());
                    celda.setTextSize(9);
                    celda.setPadding(10, 10, 10, 10);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText(transaccion.getInfo());
                    row.addView(celda);
                    celda = new TextView(tabla_layout.getContext());
                    Object concepto = transaccion.getConcepto();
                    if (concepto.toString() == "null")
                        concepto = "";
                    celda.setText((String) concepto);
                    celda.setTextSize(9);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setPadding(10, 10, 10, 10);
                    row.addView(celda);
                    celda = new TextView(tabla_layout.getContext());
                    celda.setTextSize(9);
                    celda.setPadding(10, 10, 10, 10);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_TEXT_START);
                    celda.setText("$" + String.valueOf(transaccion.getNeto()));
                    row.addView(celda);
                    saldo = transaccion.getSaldo_acumulado();
                    tabla_layout.addView(row);
                    TextView Saldo_vista = (TextView) findViewById(R.id.Saldo);
                    Saldo_vista.setText("$" + saldo);
                }
                    estado.setVisibility(View.INVISIBLE);
            }

        };
        tabla.registerListener(R.id.tabla_layout, (Loader.OnLoadCompleteListener) listener);
        findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
    }
    public static HashMap<String,String> obtener_filtros () throws ParseException{
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


        } else if (id == R.id.fecha_hasta) {

        }  else if (id == R.id.nav_manage) {


        } else if (id == R.id.nav_share) {

        }
        try {
            listar(R.layout.app_bar_transacciones);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_transacciones);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void OnClickVermas(View v){

    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}

