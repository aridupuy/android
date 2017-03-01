package com.cobrodigital.com.cobrodigital2.Modulos.Main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Estado_cuenta;
import com.cobrodigital.com.cobrodigital2.R;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final public static boolean emulador=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        12);
            }
        }
        super.onCreate(savedInstanceState);
       this.ejecutar();
    }
    //clicks de botones
    public void onClickEscanear(View view){
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        navegacion.escanear(this);
        return;
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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
       Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return navegacion.navegar(item.getItemId());

    }
    private void ejecutar(){

        setContentView(R.layout.activity_main);

        if(Gestor_de_credenciales.esta_asociado()!=false || (Gestor_de_credenciales.re_asociar(getApplicationContext()))!=false) {

            View cuenta = findViewById(R.id.textView7);
            ((ViewGroup) cuenta.getParent()).removeView(cuenta);
            startActivity(new Intent(this, Estado_cuenta.class));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        serviceBoot sb=new serviceBoot(this.getApplicationContext());
//        sb.startTimer();
    }
    @Override
    //capturo el resultado del scanner
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Gestor_de_credenciales.asociar(getApplicationContext(),intent);
                Gestor_de_mensajes_usuario.mensaje("Usuario loggeado correctamente.",getApplicationContext());
            } else if (resultCode == RESULT_CANCELED) {
                Gestor_de_mensajes_usuario mensajes_usuario=new Gestor_de_mensajes_usuario(getApplicationContext());
                mensajes_usuario.mensaje("La operación fué cancelada");
            }
        }
        ejecutar();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ejecutar();
    }
    static public Context getContext(){
        Main activity=new Main();
        return activity.getBaseContext();
    }
}
