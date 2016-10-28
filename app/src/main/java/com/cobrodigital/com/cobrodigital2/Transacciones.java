package com.cobrodigital.com.cobrodigital2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
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
public class Transacciones extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int cantidad_transacciones_a_mostrar=10;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String dato;
    public static String fecha_desde;
    public static String fecha_hasta;
    public static Context context;
    private long tiempo_estimado_de_espera=5000;

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

        setContentView(R.layout.activity_transacciones);
        LayoutInflater loiViewInflater = LayoutInflater.from(getBaseContext());
        View mview = loiViewInflater.inflate(vista, null);
        final HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT,HorizontalScrollView.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addContentView(mview, params);
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.textView5).setVisibility(View.INVISIBLE);
        findViewById(R.id.Saldo).setVisibility(View.INVISIBLE);
        findViewById(R.id.transascompletas).setVisibility(View.INVISIBLE);
        final Vector<Transaccion> transacciones=new Vector<Transaccion>();
        final TableLayout tabla_layout = (TableLayout) findViewById(R.id.tabla_layout);
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("sigo thread 1");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Transaccion transaccion = null;
                                findViewById(R.id.textView5).setVisibility(View.VISIBLE);
                                findViewById(R.id.Saldo).setVisibility(View.VISIBLE);
                                findViewById(R.id.transascompletas).setVisibility(View.VISIBLE);
                                String saldo = "";
                                for (int i = 0; (i < cantidad_transacciones_a_mostrar || cantidad_transacciones_a_mostrar == 0) && i < transacciones.size(); i++) {
                                    transaccion = (Transaccion) transacciones.get(i);
                                    TableRow row;
                                    row = new TableRow(tabla_layout.getContext());
                                    row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//                                    row.setBackgroundResource(R.drawable.celda);
                                    row.setBackgroundColor(Color.RED);
                                    ///primera parte
                                    LinearLayout linea = new LinearLayout(tabla_layout.getContext());
                                    linea.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                                    linea.setOrientation(LinearLayout.VERTICAL);
                                    RelativeLayout celda = new RelativeLayout(tabla_layout.getContext());
                                    celda.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                                    celda.setPadding(10, 10, 10, 10);
                                    TextView fecha= new TextView(tabla_layout.getContext());
                                    fecha.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                                    fecha.setTextSize(9);
                                    fecha.setTextColor(Color.BLACK);
                                    fecha.setText(transaccion.getFecha());
                                    celda.addView(fecha);
                                    TextView neto= new TextView(tabla_layout.getContext());
                                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    neto.setLayoutParams(params);
                                    neto.setTextSize(9);
                                    neto.setPadding(10, 10, 10, 10);
                                    neto.setText("$" + String.valueOf(transaccion.getNeto()));
                                    neto.setTextColor(Color.BLACK);

                                    celda.addView(neto);
                                    linea.addView(celda);
//                                    row.addView(linea);
                                    ////segunda parte
                                    linea = new LinearLayout(tabla_layout.getContext());
                                    linea.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                                    linea.setOrientation(LinearLayout.VERTICAL);
                                    celda = new RelativeLayout(tabla_layout.getContext());
                                    celda.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                                    celda.setPadding(10, 10, 10, 10);
                                    TextView bol= new TextView(tabla_layout.getContext());
                                    bol.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                                    bol.setTextSize(9);
                                    bol.setTextColor(Color.BLACK);
                                    bol.setTextAlignment(celda.TEXT_ALIGNMENT_TEXT_START);
                                    bol.setText(transaccion.getNro_boleta());
                                    celda.addView(bol);


                                    TextView info= new TextView(tabla_layout.getContext());
                                    params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    info.setLayoutParams(params);
                                    info.setTextSize(9);
                                    info.setTextColor(Color.BLACK);
                                    info.setTextAlignment(celda.TEXT_ALIGNMENT_TEXT_START);
                                    info.setText(transaccion.getInfo());
                                    celda.addView(info);
                                    linea.addView(celda);
                                    row.addView(linea);
//

//                                    celda = new TextView(tabla_layout.getContext());
//                                    celda.setTextSize(9);
//                                    celda.setPadding(10, 10, 10, 10);
//                                    celda.setTextColor(Color.BLACK);
//                                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_TEXT_START);
//                                    celda.setText(transaccion.getIdentificacion());
//                                    row.addView(celda);
//
//                                    celda = new TextView(tabla_layout.getContext());
//                                    celda.setTextSize(9);
//                                    celda.setPadding(10, 10, 10, 10);
//                                    celda.setTextColor(Color.BLACK);
//                                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
//                                    celda.setText(transaccion.getInfo());
//                                    row.addView(celda);
//
//                                    celda = new TextView(tabla_layout.getContext());
//                                    Object concepto = transaccion.getConcepto();
//                                    if (concepto.toString() == "null")
//                                        concepto = "";
//                                    celda.setText((String) concepto);
//                                    celda.setTextSize(9);
//                                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
//                                    celda.setPadding(10, 10, 10, 10);
//                                    celda.setTextColor(Color.BLACK);
//                                    row.addView(celda);
//
//
//
                                    saldo = transaccion.getSaldo_acumulado();
                                    tabla_layout.addView(row);
                                    TextView Saldo_vista = (TextView) findViewById(R.id.Saldo);
                                    Saldo_vista.setText("$" + saldo);
                                }
                                findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).start();
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("sigo tread uiThread");
                try {
                    HashMap<String, String> variables = obtener_filtros();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String hasta = fecha_hasta;
                String desde = fecha_desde;
                HashMap<String, String> variables = null;
                try {
                    variables = obtener_filtros();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("Busco en el ws");
                    if (!Gestor_de_credenciales.esta_asociado())
                        return;
                    LinkedHashMap filtros = new LinkedHashMap();
                    if (variables.size() > 0) {
                        desde = (String) variables.get("desde");
                        hasta = (String) variables.get("hasta");
                        variables.remove("desde");
                        variables.remove("hasta");
                    } else {
                        Date Fecha = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                        desde = format.format(Fecha);
                        hasta = format.format(Fecha);
                    }
                    CobroDigital.webservice.webservice_transacciones.consultar_transacciones(desde, hasta, filtros);
                    if (CobroDigital.webservice.obtener_resultado().equals("1")) {
                        Object transicion[] = CobroDigital.webservice.obtener_datos().toArray();
                        if (transicion.length > 0) {
                            JSONArray datos = new JSONArray((String) transicion[0]);
                            for (int i = 0; i < datos.length(); i++) {
                                Transaccion transaccion = new Transaccion();
                                transaccion = transaccion.leerTransaccion(Transacciones.context, datos.getJSONObject(i));
                                if (transaccion != null)
                                    transacciones.add(transaccion);
                            }
                        } else {
                            System.out.println("No hay datos disponibles");
                            Gestor_de_mensajes_usuario.mensaje("No hay datos disponibles.", Transacciones.context);
                            return;
                        }
                    } else {
                        try {
                            Thread.currentThread().sleep(tiempo_estimado_de_espera);
                            this.run();

                        }catch (InterruptedException e){
                            Thread.currentThread().interrupt();
                            System.out.println("Comunicacion fallida!");
                            Gestor_de_mensajes_usuario.mensaje("Comunicacion fallida!", Transacciones.context);
                            return;
                        }
                    }

                } catch (SQLException e) {
                    System.out.println("No hace falta agregar");
                }
                catch (JSONException e) {
                    System.out.println("Error en la lectura de datos.");
                }
                catch (MalformedURLException e) {
                    System.out.println("Error en el envio de datos.");
                } catch (IOException e) {
                    System.out.println("Error de entrada salida.");
                }
                handler.dispatchMessage(new Message());
            }
        }).start();
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

