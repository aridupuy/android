package com.cobrodigital.com.cobrodigital2.Modulos.Boletas.Tareas_asincronicas;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by ariel on 21/03/17.
 */

public class Tarea_estructura extends AsyncTask<Void,String,List<String> > {
    private Activity activity;
    public Tarea_estructura (Activity activity){
        this.activity=activity;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.findViewById(R.id.vista_boletas).setVisibility(View.GONE);
        activity.findViewById(R.id.primer_progressbar_Boleta).setVisibility(View.VISIBLE);
    }
    @Override
    protected List<String> doInBackground(Void... voids) {
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
            Log.d("Error",e.getMessage()+"/");
        }
        return estructura_clientes;

    }
    @Override
    protected void onPostExecute(List<String> estructura_clientes) {
        Spinner spinner= (Spinner) activity.findViewById(R.id.campo_a_buscar);
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, estructura_clientes);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)activity.findViewById(R.id.identificador)).setHint(((String)adapterView.getItemAtPosition(i)).toLowerCase());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        activity.findViewById(R.id.vista_boletas).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.primer_progressbar_Boleta).setVisibility(View.GONE);
    }
}
