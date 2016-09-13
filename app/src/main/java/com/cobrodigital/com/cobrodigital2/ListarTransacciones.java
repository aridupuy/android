package com.cobrodigital.com.cobrodigital2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by Ariel on 28/08/16.
 */
public class ListarTransacciones extends Activity implements View.OnClickListener {
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
//    KA289659
//            V2nJUHv7110BI4v1FLxdeQrFlWUw08j5L3VAxZB3P9Dm0EJbsDW5vJsi960
    private SimpleDateFormat dateFormatter;
    public ListarTransacciones() {
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_transacciones);
        Credencial credencial=new Credencial(getApplicationContext());
        CobroDigital.credencial=credencial.obtenerCredencial();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setDateTimeField();
    }
    private void setDateTimeField() {
        EditText fechaDesde=(EditText)findViewById(R.id.fecha_desde);
        fechaDesde.setOnClickListener(this);
        EditText fechaHasta=(EditText)findViewById(R.id.fecha_hasta);
        fechaHasta.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                EditText fechaDesde=(EditText)findViewById(R.id.fecha_desde);
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaDesde.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                EditText fechaHasta=(EditText)findViewById(R.id.fecha_hasta);
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaHasta.setText(dateFormatter.format(newDate.getTime()));
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
        EditText textdesde = (EditText) findViewById(R.id.fecha_desde);
        EditText texthasta = (EditText) findViewById(R.id.fecha_hasta);
        Date date=dateFormatter.parse(textdesde.getText().toString());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        String desde=dateFormatter.format(date);

        date=this.dateFormatter.parse(texthasta.getText().toString());
        String hasta=dateFormatter.format(date);
        Credencial credencial=new Credencial(getApplicationContext());
        CobroDigital cd = null;
        Vector vectordatos =null;
        try {
            cd = new CobroDigital(credencial.obtenerCredencial());
            cd.consultar_transacciones(desde, hasta, new LinkedHashMap());
            String ejecucion_correcta = cd.obtener_resultado();
            vectordatos = cd.obtener_datos();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setContentView(R.layout.tabla_transacciones);
        TableLayout tabla = (TableLayout) findViewById(R.id.tabla);
        if (vectordatos == null) {
            Vector vector = cd.obtener_log();
            Object dato[] = vector.toArray();

            for (int i = 0; i < dato.length; i++) {
                TableRow row = new TableRow(this);
                TextView celda = new TextView(this);
                celda.setText((String) dato[i]);
                row.addView(celda);
                tabla.addView(row);
            }
        } else {
            Object transicion[] = vectordatos.toArray();

            try {
                JSONArray datos = new JSONArray((String) transicion[0]);
                System.out.println(datos.length());
                for (int i = 0; i < datos.length(); i++) {
                    Transaccion transaccion=new Transaccion(getApplicationContext());
                    transaccion.leerTransaccion(datos.getJSONObject(i),true);
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
                    transaccion.set();
                    System.out.println(i);
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    }

    public void OnclickVolver(View view) {
        finish();
    }

}
