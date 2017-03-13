package com.cobrodigital.com.cobrodigital2.Modulos.Retiros;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.Navegacion;

public class Retiro_importe extends Navegacion implements View.OnClickListener {
    protected  String cuit,titular,Nombre,saldo_disponible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retiro_importe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRetiro_importe);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_retiro_importe);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_retiro_importe);
        navigationView.setNavigationItemSelectedListener(this);
        Bundle bundle = getIntent().getExtras().getBundle("banco");
        if(bundle==null){
            Log.e("Error","El activity quiere recibir parametros.");
            finish();
        }
        this.Nombre=bundle.getString("Nombre");
        this.titular=bundle.getString("Titular");
        this.cuit=bundle.getString("Cuit");
        this.saldo_disponible=bundle.getString("Disponible");
        ((TextView)findViewById(R.id.Nombre)).setText(this.Nombre);
        ((TextView)findViewById(R.id.Titular)).setText(this.titular);
        ((TextView)findViewById(R.id.Cuit)).setText(this.cuit);
        ((TextView)findViewById(R.id.saldo_disponible)).setText(this.saldo_disponible);
        ((Button)findViewById(R.id.aceptar_retiro)).setOnClickListener(this);
        ((Button)findViewById(R.id.cancelar_retiro)).setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion=new Gestor_de_navegacion(this);
        navegacion.navegar(item.getItemId());
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aceptar_retiro:
                this.validar_datos();
                String plata=((EditText)findViewById(R.id.plata)).getText().toString().replace("$","");
                Intent intent = new Intent(Retiro_importe.this,Retiro_confirmacion.class);
                intent.putExtra("Nombre",this.Nombre);
                intent.putExtra("Titular",this.titular);
                intent.putExtra("Cuit",this.cuit);
                intent.putExtra("Disponible",this.saldo_disponible);
                intent.putExtra("Plata",plata);
                startActivityForResult(intent,1);
                break;
            case R.id.cancelar_retiro:
                finish();
                break;
        }

    }
    public void validar_datos(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        if(resultCode==RESULT_CANCELED){
            setResult(RESULT_CANCELED,intent);
        }
        setResult(RESULT_OK,intent);
        finish();
    }
}
