package com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_navegacion;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Estado_cuenta extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_cuenta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEstadoCuenta);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_estado_cuenta);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_estado_cuenta);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        try {
            dibujar();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    protected void dibujar() throws JSONException, ParseException {
        Log.d("MENSAJE","empiezo a dibujar");
        XYSeries series = new XYSeries("Grafico de tenencias");
        int hour = 0;
        try {
            CobroDigital.webservice.webservice_estadisticas.obtener_estadisticas_mes_ingresos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vector<String> datos=CobroDigital.webservice.obtener_datos();
        SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat Formatter=new SimpleDateFormat("dd-MM-yyyy");
        Object transicion[] = datos.toArray();
        if (transicion.length > 0) {
            JSONArray dato = new JSONArray((String) transicion[0]);
            for (int i = 0; (i < dato.length()); i++) {
                JSONObject jo = dato.getJSONObject(i);
                Date fecha = parser.parse((String) jo.getString("fecha"));
                Double monto_total=jo.getDouble("monto_total");
                series.add((double)fecha.getDay(),monto_total);
                series.addAnnotation(Formatter.format(fecha),fecha.getDay(),monto_total);
            }
        }
        XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
        dataset.addSeries(0,series);
        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(6); //grosor de la linea
        renderer.setColor(Color.RED); //color de la linea
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);
        // we add point markers
        renderer.setPointStyle(PointStyle.POINT);
        renderer.setPointStrokeWidth(3);
        renderer.setAnnotationsTextSize(40);
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        // Disable Pan on two axis
        mRenderer.setPanEnabled(true, false);
//        mRenderer.setYAxisMax(35);
//        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(false);
        mRenderer.setAxisTitleTextSize(10);
        mRenderer.setZoomEnabled(false,true);
        GraphicalView chartView = ChartFactory.
                getLineChartView(this, dataset, mRenderer);
        LinearLayout chartLyt= (LinearLayout) findViewById(R.id.chart);
        chartLyt.addView(chartView,0);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Gestor_de_navegacion navegacion = new Gestor_de_navegacion(getApplicationContext());
        return navegacion.navegar(item.getItemId());
    }
}
