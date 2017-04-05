package com.cobrodigital.com.cobrodigital2.Modulos.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Main.Main;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.Config;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

import java.util.Map;

public class Configuracion extends Navegacion {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConfiguracion);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_configuracion);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.cargar_datos();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //al seleccionar esta opcion que valla a un menu de configuracion.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClickDesvincular(View view){
        Gestor_de_credenciales.desasociar(getApplicationContext());
        Config.reiniciar_config(getApplicationContext());
        Gestor_de_mensajes_usuario.mensaje("se ha desvinculado correctamente.",getApplicationContext());
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
    public void onClickFrecuencia(View view){

    }
    public void onclickPlantilla(View v){
        String plantilla=((EditText)findViewById(R.id.nombre_plantilla)).getText().toString();
        CobroDigital.config.put("plantilla",plantilla);
        if(Config.guardar_config(CobroDigital.config,getApplicationContext())==false)
            Gestor_de_mensajes_usuario.mensaje("Error al guardar la configuracion personal.",getApplicationContext());
        else
            Gestor_de_mensajes_usuario.mensaje("Configurado correctamente.",getApplicationContext());
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_configuracion);
        drawer.closeDrawer(GravityCompat.START);
        return navegacion.navegar(item.getItemId());

    }
    private void cargar_datos(){
        for (Map.Entry<String,String> dato:CobroDigital.config.entrySet()) {
            System.out.println(dato.getKey());
            ((EditText)findViewById(R.id.nombre_plantilla)).setText(dato.getValue());
        }
    }
}
