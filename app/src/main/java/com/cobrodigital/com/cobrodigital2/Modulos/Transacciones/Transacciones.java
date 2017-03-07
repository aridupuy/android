package com.cobrodigital.com.cobrodigital2.Modulos.Transacciones;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Tools.Tools;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.fragment.Detalle_transacciones_fragment;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.fragment.Transacciones_fragment;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

import java.io.IOException;
/**
 * Created by Ariel on 28/08/16.
 */
public class Transacciones extends Navegacion{
    ///////////constantes/////////////////////////////
    public static final String FILTROS = "filtros";
    public static final String TIPO = "tipo";
    public static final String OFFSET = "Offset";
    public static final String LIMIT = "Limit";
    public static final String DESDE = "desde";
    public static final String HASTA = "hasta";
    public static final String TRANSACCIONES = "Transacciones";
    ///////////fin-constantes/////////////////////////////
    private String desde="";
    private String hasta="";
    private int offset;
    private int limit;
    public static Boolean es_doble_display=false;
//    private String tipo="";
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    public static final String CAMPO_RECIBIDO="cantidad_transacciones_a_mostrar";
    public Transacciones() {
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_transacciones);
        /////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_transaccion);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_transaccion);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_transacciones);
        navigationView.setNavigationItemSelectedListener(this);
        ///////////////////////////////
        Intent intent =getIntent();
        String tipo="";
        if(intent.hasExtra(FILTROS)){
            Bundle bundle=intent.getBundleExtra(FILTROS);
            desde=bundle.getString("desde");
            hasta=bundle.getString("hasta");
            tipo=bundle.getString(TIPO,"");
            offset=bundle.getInt(OFFSET,0);
            limit=bundle.getInt(LIMIT,0);
        }
        try {
            Transacciones_fragment fragment=Transacciones_fragment.newInstance(desde,hasta,tipo);
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft=ft.add(R.id.include_transacciones, fragment);
            int res=ft.commit();
            Log.d("res",res+"");
            if (findViewById(R.id.include_detalle_transacciones)!=null){
                es_doble_display=true;
                Detalle_transacciones_fragment fragment2=Detalle_transacciones_fragment.newInstance(getIntent().getExtras(),getApplication().getApplicationContext());
                FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                ft2=ft2.add(R.id.include_detalle_transacciones, fragment2);
                int res2=ft2.commit();
                Log.d("res",res2+"");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Tools.developerLog("Error");
        }

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_transaccion);
        drawer.closeDrawer(GravityCompat.START);
        return navegacion.navegar(item.getItemId());
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    public void onBackPressed(){
        finish();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

}

