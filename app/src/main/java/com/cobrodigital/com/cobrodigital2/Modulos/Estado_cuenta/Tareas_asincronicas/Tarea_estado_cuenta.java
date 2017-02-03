package com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Tareas_asincronicas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Estado_cuenta;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * Created by ariel on 27/01/17.
 */

public class Tarea_estado_cuenta extends AsyncTask<Void,Void,View> {
    public Estado_cuenta context;
    public static final String SALDO = "saldo";
    private Double pesos_hoy_ingresos = 0d;
    private Double pesos_semana_ingresos=0d;
    private Double pesos_mes_ingresos=0d;
    private Double pesos_hoy_egresos = 0d;
    private Double pesos_semana_egresos=0d;
    private Double pesos_mes_egresos=0d;
    private String saldo_actual="";
    private Double maxX=0d;

    public Tarea_estado_cuenta(Estado_cuenta context){
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((LinearLayout)context.findViewById(R.id.chart)).setVisibility(View.GONE);
        ((LinearLayout)context.findViewById(R.id.layout_saldo)).setVisibility(View.GONE);
        ((LinearLayout)context.findViewById(R.id.datos_estado_cuenta_ingresos)).setVisibility(View.GONE);
        ((LinearLayout)context.findViewById(R.id.datos_estado_cuenta_egresos)).setVisibility(View.GONE);
        ((ProgressBar)context.findViewById(R.id.progressbar_estado_cuenta)).setVisibility(View.VISIBLE);
    }

    @Override
    protected View doInBackground(Void[] Voids) {
        Looper.prepare();
        try {
            CobroDigital.webservice.webservice_saldo.consultar_saldo();
            if (CobroDigital.webservice.obtener_resultado().equals("1")) {
                Object transicion_saldo[] = CobroDigital.webservice.webservice_saldo.obtener_datos().toArray();
                if (transicion_saldo.length > 0) {
                    JSONObject saldoJson= new JSONObject((String) transicion_saldo[0]);
                    saldo_actual= saldoJson.getString(this.SALDO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            saldo_actual="No se registra saldo en la cuenta.";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MENSAJE","empiezo a dibujar");
        // Now we create the renderer
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.setYTitle("$ x 100");
        mRenderer.setXTitle("Dias");
        XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
        try {
            CobroDigital.webservice.webservice_estadisticas.obtener_estadisticas_mes_ingresos();
            dataset.addSeries(0,this.generar_serie("Ingresos")); //generar serie puede ver los datos accediendo directamente al objeto webservice.
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            CobroDigital.webservice.webservice_estadisticas.obtener_estadisticas_mes_egresos();
            dataset.addSeries(1,this.generar_serie("Egresos")); //generar serie puede ver los datos accediendo directamente al objeto webservice.
        } catch (Exception e) {
            e.printStackTrace();
            publishProgress();
            return null;
        }

        mRenderer.addSeriesRenderer(this.generar_renderer(6,Color.GREEN,PointStyle.POINT));
        mRenderer.addSeriesRenderer(this.generar_renderer(6,Color.BLUE,PointStyle.POINT));
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setLegendTextSize(45);
        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(maxX*2);
        mRenderer.setYAxisMin(10);
        mRenderer.setFitLegend(true);
        mRenderer.setLegendHeight(15);
        mRenderer.setShowAxes(true);
        mRenderer.setShowGridX(true);
        mRenderer.setGridColor(Color.GRAY);
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setPanEnabled(false, false);
        mRenderer.setLabelsTextSize(25);
        mRenderer.setZoomEnabled(true,true);
        mRenderer.setTextTypeface(Typeface.MONOSPACE);
        mRenderer.setYLabelsAlign(Paint.Align.CENTER);
        mRenderer.setAxisTitleTextSize(25);

        mRenderer.setMargins(new int[]{0, 50, 0, 0});
        GraphicalView chartView = ChartFactory.getLineChartView(context, dataset, mRenderer);
        return chartView;
    }

    @Override
    protected void onPostExecute(View chartView) {
        if(chartView==null){
            Gestor_de_mensajes_usuario.mensaje("Ah ocurrido un error",context);
            context.finish();
        }
        ((ProgressBar)context.findViewById(R.id.progressbar_estado_cuenta)).setVisibility(View.GONE);
        ((LinearLayout)context.findViewById(R.id.layout_saldo)).setVisibility(View.VISIBLE);
        ((LinearLayout)context.findViewById(R.id.chart)).setVisibility(View.VISIBLE);
        LinearLayout chartLyt= (LinearLayout) context.findViewById(R.id.chart);
        chartLyt.addView(chartView,0);
        DecimalFormat df = new DecimalFormat("#.##");
        ((TextView)context.findViewById(R.id.hoy)).setText("$"+String.valueOf(df.format(pesos_hoy_ingresos)));
        ((TextView)context.findViewById(R.id.semana)).setText("$"+String.valueOf(df.format(pesos_semana_ingresos)));
        ((TextView)context.findViewById(R.id.mes)).setText("$"+String.valueOf(df.format(pesos_mes_ingresos)));
        ((TextView)context.findViewById(R.id.hoy_egresos)).setText("$"+String.valueOf(df.format(pesos_hoy_egresos)));
        ((TextView)context.findViewById(R.id.semana_egresos)).setText("$"+String.valueOf(df.format(pesos_semana_egresos)));
        ((TextView)context.findViewById(R.id.mes_egresos)).setText("$"+String.valueOf(df.format(pesos_mes_egresos)));
        ((TextView)context.findViewById(R.id.saldo_cuenta)).setText("$"+saldo_actual);
        ((LinearLayout)context.findViewById(R.id.datos_estado_cuenta_ingresos)).setVisibility(View.VISIBLE);
        ((LinearLayout)context.findViewById(R.id.datos_estado_cuenta_egresos)).setVisibility(View.VISIBLE);
    }
    protected XYSeries generar_serie(String titulo){
        XYSeries series = new XYSeries(titulo);
        series.add(0f,0f);
        SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat Formatter=new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat Formatterdia=new SimpleDateFormat("dd");
        Vector<String> datos_egresos=CobroDigital.webservice.obtener_datos();
        Object transicion_egresos[] = datos_egresos.toArray();
        if (transicion_egresos.length > 0) {
            JSONArray dato_egresos = null;
            try {
                dato_egresos = new JSONArray((String) transicion_egresos[0]);
//                series_egresos.add(0f,0f);
                for (int j = 0; (j < dato_egresos.length()); j++) {
                    JSONObject jo_egresos = dato_egresos.getJSONObject(j);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(parser.parse((String) jo_egresos.getString("fecha")));
                    Double monto_total_egresos=jo_egresos.getDouble("monto_total");
                    String tipo=jo_egresos.getString("sentido_transaccion");
                    Calendar ahora = Calendar.getInstance();
                    ahora.setTime(parser.parse((String) jo_egresos.getString("hoy")));
                    series.add(Double.valueOf(Formatterdia.format(parser.parse((String) jo_egresos.getString("fecha")))),(monto_total_egresos/100));
                    if(Double.valueOf(Formatterdia.format(parser.parse((String) jo_egresos.getString("fecha"))))>maxX){
                        this.maxX=Double.valueOf(Formatterdia.format(parser.parse((String) jo_egresos.getString("fecha"))));
                    }
                    if(cal.get(Calendar.DATE)==ahora.get(Calendar.DATE) && cal.get(Calendar.MONTH)==ahora.get(Calendar.MONTH)){
                        if(tipo.equals("egreso"))
                            pesos_hoy_egresos+=monto_total_egresos;
                        else
                            pesos_hoy_ingresos+=monto_total_egresos;
                    }
                    if(cal.get(Calendar.WEEK_OF_MONTH)==ahora.get(Calendar.WEEK_OF_MONTH) && cal.get(Calendar.MONTH)==ahora.get(Calendar.MONTH)){
                        if(tipo.equals("egreso"))
                            pesos_semana_egresos+=monto_total_egresos;
                        else
                            pesos_semana_ingresos+=monto_total_egresos;
                    }
                    if(cal.get(Calendar.MONTH)==ahora.get(Calendar.MONTH)){
                        if(tipo.equals("egreso"))
                            pesos_mes_egresos+=monto_total_egresos;
                        else
                            pesos_mes_ingresos+=monto_total_egresos;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return series;
    }
    protected XYSeriesRenderer generar_renderer(int lineWidth,int color,PointStyle point){
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(lineWidth); //grosor de la linea
        renderer.setColor(color); //color de la linea
        renderer.setDisplayBoundingPoints(true);
        renderer.setGradientEnabled(true);
        renderer.setPointStyle(point);
        return renderer;
    }
}
