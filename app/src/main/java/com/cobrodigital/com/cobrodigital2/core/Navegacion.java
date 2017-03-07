package com.cobrodigital.com.cobrodigital2.core;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Tareas_asincronicas.Tarea_retiros;

/**
 * Created by ariel on 07/03/17.
 */

public class Navegacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,Tarea_retiros.AsyncResponse {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        navegacion.navegar(item.getItemId());
        return true;
    }

    @Override
    public void processFinish(Boolean resultado) {

    }
}
