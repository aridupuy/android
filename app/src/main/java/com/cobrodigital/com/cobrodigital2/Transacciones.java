package com.cobrodigital.com.cobrodigital2;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
import org.w3c.dom.Text;

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
        super.onCreate(savedInstanceState);
        context=this.getApplicationContext();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setDateTimeField();

        try {
            this.listar(R.layout.app_bar_transacciones,cantidad_transacciones_a_mostrar
            );

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
                        listar(R.layout.app_bar_transacciones_completas,cantidad_transacciones_a_mostrar);
                        cantidad_transacciones_a_mostrar=anterior;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

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
    private  void listar(final int vista,final int  cantidad_transacciones_a_mostrar) throws ParseException {

        setContentView(R.layout.activity_transacciones);
        LayoutInflater loiViewInflater = LayoutInflater.from(getBaseContext());
        View mview = loiViewInflater.inflate(vista, null);
        final HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT,HorizontalScrollView.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addContentView(mview, params);
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.textView5).setVisibility(View.INVISIBLE);
        findViewById(R.id.Saldo).setVisibility(View.INVISIBLE);
        if(vista==R.layout.app_bar_transacciones)
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
                                Looper.prepare();
                                Transaccion transaccion = null;
                                findViewById(R.id.textView5).setVisibility(View.VISIBLE);
                                findViewById(R.id.Saldo).setVisibility(View.VISIBLE);
                                if(vista==R.layout.app_bar_transacciones)
                                findViewById(R.id.transascompletas).setVisibility(View.VISIBLE);
                                String saldo = "";
                                System.out.println(transacciones.size());
                                for (int i = 0; (i < cantidad_transacciones_a_mostrar || cantidad_transacciones_a_mostrar == 0) && i < transacciones.size(); i++) {
                                    transaccion = (Transaccion) transacciones.get(i);
//                                    System.out.println(transaccion.toString());
                                    TableRow row = new TableRow(Transacciones.this);
//                                    row.setBackgroundColor(Color.BLUE);
//                                    row.setBackgroundResource(R.drawable.celda);
                                    LayoutInflater inflador=getLayoutInflater();
                                    View ejemplo_fila=inflador.inflate(R.layout.test,row);
                                    ///primera parte
                                    TextView fecha=(TextView)ejemplo_fila.findViewById(R.id.fecha);
                                    fecha.setText(transaccion.getFecha());
                                    TextView neto= (TextView) ejemplo_fila.findViewById(R.id.neto);
                                    neto.setText("$" + String.valueOf(transaccion.getNeto()));
                                    ///segunda parte
                                    TextView bol= (TextView)ejemplo_fila.findViewById(R.id.bol);;
                                    bol.setTextSize(9);
                                    bol.setText(transaccion.getNro_boleta());

                                    TextView nombre= (TextView)ejemplo_fila.findViewById(R.id.nombre);
                                    nombre.setText(transaccion.getNombre());
                                    TextView concepto= (TextView)ejemplo_fila.findViewById(R.id.concepto);
                                    concepto.setText(transaccion.getNombre());

                                    TextView info= (TextView)ejemplo_fila.findViewById(R.id.info);
                                    info.setText(transaccion.getInfo());


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
                Looper.prepare();
                System.out.println("sigo tread uiThread");
//                try {
//                    HashMap<String, String> variables = obtener_filtros();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                String hasta = fecha_hasta;
                String desde = fecha_desde;
                HashMap<String, String> variables =new HashMap<String, String>();;
//                try {
//                    variables = obtener_filtros();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
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
                            Gestor_de_mensajes_usuario.mensaje("No hay datos disponibles.",Transacciones.context);
                            finish();
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
//    public static HashMap<String,String> obtener_filtros () throws ParseException{
//        HashMap<String , String > filtros=new HashMap<String, String>();
//        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
//        String fecha;
//        if(fecha_desde!=null){
//            fecha=dateFormatter.format(fecha_desde);
//            filtros.put("desde",fecha);
//        }
//        if(fecha_hasta!=null){
//            fecha=dateFormatter.format(fecha_hasta);
//            filtros.put("hasta",fecha);
//        }
//        return filtros;
//    }
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
            listar(R.layout.app_bar_transacciones,cantidad_transacciones_a_mostrar);
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
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onCreate(null);
    }
}

