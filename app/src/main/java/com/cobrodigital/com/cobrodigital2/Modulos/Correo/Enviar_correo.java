package com.cobrodigital.com.cobrodigital2.Modulos.Correo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Modulos.Boletas.Boletas;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class Enviar_correo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int nro_Boleta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_correo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        nro_Boleta=this.getIntent().getExtras().getInt(Boletas.NRO_BOLETA);
        ((Button)findViewById(R.id.btn_enviar_correo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!enviar_mail(nro_Boleta))
                    finish();
            }
        });
    }
    private boolean enviar_mail(int nro_Boleta){
            String destinatario=((EditText)findViewById(R.id.destinatario)).getText().toString();
            String asunto="Nueva boleta enviada";
            String mensaje="Se ha generado una nueva boleta";//poner un mensaje mas bonito aqui;
            try {
                CobroDigital.webservice.webservice_enviar_correo.enviar_boleta_correo(destinatario,asunto,mensaje,nro_Boleta);
            } catch (Exception e) {
                e.printStackTrace();
                Gestor_de_mensajes_usuario.mensaje("Ha ocurrido un error al enviar el mail",this.getApplicationContext());
                return false;
            }
            Gestor_de_mensajes_usuario.mensaje("Boleta enviada correctamente",this.getApplicationContext());
            return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion= new Gestor_de_navegacion(this.getApplicationContext());
        navegacion.navegar(item.getItemId());
        return true;
    }
}
