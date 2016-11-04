package com.cobrodigital.com.cobrodigital2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_personalizacion;
import com.cobrodigital.com.cobrodigital2.Model.Boleta;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONArray;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Boletas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boletas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.cargar_spiner();

   }
    protected void generar_boleta(String identificador, String campo_a_buscar, String concepto, String fecha_1, String importe_1, String modelo, String fecha_2, String importe_2, String fecha_3, String importe_3){
        try {
            Vector<Boleta> boletas=new Vector<Boleta>();
            CobroDigital.webservice.webservice_boleta.generar_boleta(identificador, campo_a_buscar, concepto, fecha_1, importe_1, modelo, fecha_2, importe_2, fecha_3, importe_3);
            Boleta boleta=new Boleta(getApplicationContext());
            if(CobroDigital.webservice.obtener_resultado().equals("1")){
                Object transicion[]= CobroDigital.webservice.obtener_datos().toArray();
                if(transicion.length>0) {
                    JSONArray datos = new JSONArray((String) transicion[0]);
                    for (int i = 0; i < datos.length(); i++) {
                        boleta = boleta.leerBoleta(datos.getJSONObject(i));
                        if(boleta!=null)
                            boletas.add(boleta);
                    }
                    Gestor_de_mensajes_usuario.mensaje("Boleta generada correctamente.",getApplicationContext());
                }
                else{
                    System.out.println("No hay datos disponibles");
                    Gestor_de_mensajes_usuario.mensaje("No hay datos disponibles.",getApplicationContext());
                    finish();
                }
            }
            else{
                System.out.println("Comunicacion fallida!");
                Gestor_de_mensajes_usuario.mensaje("Comunicacion fallida!",getApplicationContext());
                finish();
                return;
            }

        } catch (Exception e) {
            Gestor_de_mensajes_usuario.mensaje(e.getMessage(),getApplicationContext());
        }

    }
    private void cargar_spiner(){
        List<String> estructura_clientes=new ArrayList<String>();
        estructura_clientes.add("campo para buscar");
//        estructura_clientes
        for (String campo:CobroDigital.personalizacion.estructura) {
            estructura_clientes.add(campo);
        }
        Spinner spinner= (Spinner) findViewById(R.id.campo_a_buscar);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estructura_clientes);
        spinner.setAdapter(arrayAdapter);
    }


}
