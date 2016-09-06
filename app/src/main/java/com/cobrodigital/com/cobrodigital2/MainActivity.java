package com.cobrodigital.com.cobrodigital2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cobrodigital.com.cobrodigital2.core.BaseDeDatos;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;
import com.cobrodigital.com.cobrodigital2.core.LectorQR;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.oned.Code93Reader;
import com.google.zxing.qrcode.QRCodeReader;
import android.webkit.WebView;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static String contents;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    private static final String PACKAGE = "com.cobrodigital.com.cobrodigital2";
    private static final String SCANNER = "com.google.zxing.client.android.SCAN";
    private static final String SCAN_FORMATS = "UPC_A,UPC_E,EAN_8,EAN_13,CODE_39,CODE_93,CODE_128";
    private static final String SCAN_MODE = "QR_CODE_MODE";
    public static final int REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        BaseDeDatos baseDeDatos = new BaseDeDatos(getApplicationContext());
        baseDeDatos.onCreate(baseDeDatos.getWritableDatabase());
        HashMap<String, String> credencial = baseDeDatos.obtener_credencial();
        if (credencial != null) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            IntentIntegrator scanIntent = new IntentIntegrator(MainActivity.this);
            //scanIntent.setPackage(PACKAGE);
            //scanIntent.addCategory(Intent.CATEGORY_DEFAULT);
            //scanIntent.putExtra("SCAN_FORMATS", SCAN_FORMATS);
            //scanIntent.putExtra("SCAN_MODE", SCAN_MODE);
            scanIntent.initiateScan();
        }
        System.out.println("No hay Credenciales");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OnClickPagadorNew(View view) {
        Intent pagador = new Intent(getApplicationContext(), Pagador.class);
        startActivity(pagador);
    }

    public void OnClickListarTransacciones(View View) {
        /// setContentView(R.layout.listar_transacciones);
        Intent transacciones = new Intent(getApplicationContext(), ListarTransacciones.class);
        startActivity(transacciones);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

}
