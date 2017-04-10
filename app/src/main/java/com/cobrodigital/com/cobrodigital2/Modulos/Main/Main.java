package com.cobrodigital.com.cobrodigital2.Modulos.Main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_credenciales;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Estado_cuenta;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.Services.FCM.Tareas_asincronicas.Tarea_registrar;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Main extends Navegacion implements Tarea_registrar.AsyncResponse {
    final public static boolean emulador = false;
    public static final String TOPIC = "COBRODIGITAL";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.init);
        super.onCreate(savedInstanceState);
        final Navegacion _this=this;
        if (ContextCompat.checkSelfPermission(_this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(_this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(_this,
                        new String[]{Manifest.permission.CAMERA},
                        12);
            }
        }
        new AsyncTask<Void,Void,Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    if (!Gestor_de_credenciales.esta_asociado()) {
                        if (!Gestor_de_credenciales.re_asociar(getApplicationContext())) {
                            return false;
                        }

//                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(_this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//                        drawer.setDrawerListener(toggle);
//                        toggle.syncState();
//                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//                        navigationView.setNavigationItemSelectedListener(_this);

//                startActivity(new Intent(this, Transacciones.class));
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("mensaje","no se pudo reasociar");
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(!aBoolean){
                    _this.setContentView(R.layout.activity_loggin);
                    Log.d("mensaje","inicio como loggin");
                }
                else{
//                    _this.setContentView(R.layout.activity_main);
//                    FirebaseInstanceId.getInstance().getToken();
                    FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
                    _this.startActivity(new Intent(_this, Estado_cuenta.class));
                    _this.finish();
                }
            }
        }.execute();



    }

    //clicks de botones
    public void onClickEscanear(View view) {
        Gestor_de_navegacion navegacion = new Gestor_de_navegacion(this);
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

    private void ejecutar() throws NoSuchPaddingException, PackageManager.NameNotFoundException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

    }
    @Override
    //capturo el resultado del scanner
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if(!Gestor_de_credenciales.iniciar_asociacion(getApplicationContext(), intent,this)){
//                    Gestor_de_mensajes_usuario.mensaje("Revisar credenciales.", getApplicationContext());
                    return;
                }
//                else
//                    Gestor_de_mensajes_usuario.mensaje("Usuario loggeado correctamente.", getApplicationContext());
            } else if (resultCode == RESULT_CANCELED) {
                Gestor_de_mensajes_usuario mensajes_usuario = new Gestor_de_mensajes_usuario(getApplicationContext());
                mensajes_usuario.mensaje("La operación fué cancelada");
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        finish();
    }

    static public Context getContext() {
        Main activity = new Main();
        return activity.getBaseContext();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

}
