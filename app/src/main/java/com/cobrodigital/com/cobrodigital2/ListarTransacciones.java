package com.cobrodigital.com.cobrodigital2;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Created by Ariel on 28/08/16.
 */
public class ListarTransacciones extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_transacciones);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void OnClickVer(View view) {

        EditText desde = (EditText) findViewById(R.id.fecha_desde);
        EditText hasta = (EditText) findViewById(R.id.fecha_hasta);
//        String id_comercio = "KA289659";
        //String id_comercio="CI366779";
//        String sid = "V2nJUHv7110BI4v1FLxdeQrFlWUw08j5L3VAxZB3P9Dm0EJbsDW5vJsi960";
        //String sid="MeAOO0d8tpk87Ud3AG0mZO7WCIP76GuKfU48UMVCuLO66aQGa0Iw3R6cDVs";
        String id_comercio=CobroDigital.idComercio;
        String sid=CobroDigital.idComercio;
        CobroDigital cd = null;
        try {
            cd = new CobroDigital(id_comercio, sid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            cd.consultar_transacciones(desde.getText().toString(), hasta.getText().toString(), new LinkedHashMap());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String respuesta = "";
        String ejecucion_correcta = cd.obtener_resultado();
        Vector vectordatos = cd.obtener_datos();
        setContentView(R.layout.tabla_transacciones);
        TableLayout tabla = (TableLayout) findViewById(R.id.tabla);
        if (vectordatos == null) {
            Vector vector = cd.obtener_log();
            Object dato[] = vector.toArray();

            for (int i = 0; i < dato.length; i++) {
                System.out.println("dato");
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
                for (int i = 0; i < datos.length(); i++) {
                    JSONObject dato = datos.getJSONObject(i);
                    TableRow row = new TableRow(this);
                    row.setBackgroundResource(R.drawable.celda);
                    TextView celda = new TextView(this);
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText((String) dato.get("Fecha"));
                    row.addView(celda);
                    celda = new TextView(this);
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText((String) dato.get("Nro Boleta"));
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    row.addView(celda);
                    celda = new TextView(this);
                    celda.setText((String) dato.get("Identificación"));
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    row.addView(celda);
                    celda = new TextView(this);
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText((String) dato.get("Nombre"));
                    row.addView(celda);
                    celda = new TextView(this);
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText((String) dato.get("Info"));
                    row.addView(celda);
                    celda = new TextView(this);
                    Object concepto = dato.get("Concepto");
                    System.out.println(concepto.toString());
                    if (concepto.toString() == "null")
                        concepto = "";
                    celda.setText((String) concepto);
                    celda.setTextSize(9);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setPadding(5, 5, 5, 5);
                    row.addView(celda);
                    celda = new TextView(this);
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText((String) dato.get("Bruto"));
                    row.addView(celda);
                    celda = new TextView(this);
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText((String) dato.get("Comisión"));
                    row.addView(celda);
                    celda = new TextView(this);
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText((String) dato.get("Neto"));
                    row.addView(celda);
                    celda = new TextView(this);
                    celda.setTextSize(9);
                    celda.setPadding(5, 5, 5, 5);
                    celda.setTextAlignment(celda.TEXT_ALIGNMENT_CENTER);
                    celda.setText((String) dato.get("Saldo acumulado"));
                    row.addView(celda);
                    tabla.addView(row);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }
    }

    public void OnclickVolver(View view) {
        finish();
    }

}
