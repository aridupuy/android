package com.cobrodigital.com.cobrodigital2;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.cobrodigital.com.cobrodigital2.fragment.Transacciones_fragment;
import java.io.IOException;
/**
 * Created by Ariel on 28/08/16.
 */
public class Transacciones extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String FILTROS = "filtros";
    public static final String TIPO = "tipo";
    private String desde="";
    private String hasta="";
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
        }

        try {
            Transacciones_fragment fragment=Transacciones_fragment.newInstance(desde,hasta,tipo);
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft=ft.add(R.id.include_transacciones, fragment);
            int res=ft.commit();
            Log.d("res",res+"");

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fecha_desde) {


        } else if (id == R.id.fecha_hasta) {

        }  else if (id == R.id.nav_manage) {


        } else if (id == R.id.nav_share) {

        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_transacciones);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
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

