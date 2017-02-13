package com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Detalle_transacciones;
import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Tareas_asincronicas.Tarea_transacciones;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Transacciones;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Transacciones_fragment extends Fragment {

    private static final String FECHA_DESDE = "fecha_desde";
    private static final String FECHA_HASTA = "fecha_hasta";
    private static final String TIPO= "tipo";
    public static final String TAG = "fragmentTransacciones";

    private static int cantidad_transacciones_a_mostrar=5;
    private View vista= null;
    private View footerView;
    private String f_desde;
    private String f_hasta;
    private String tipo;

    private ViewGroup Container;
    private SimpleDateFormat dateFormatter;
    private int offset=-cantidad_transacciones_a_mostrar;
    private int limit=0;

    public Transacciones_fragment() {
        //constructor vacio obligatorio en fragments usar new instance
    }

    public static Transacciones_fragment newInstance(String desde, String hasta,String tipo) throws IOException {
        Transacciones_fragment fragment = new Transacciones_fragment();
        Bundle args = new Bundle();
        args.putString(FECHA_DESDE, desde);
        args.putString(FECHA_HASTA, hasta);
        args.putString(TIPO, tipo);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            f_desde= getArguments().getString(FECHA_DESDE,"");
            f_hasta = getArguments().getString(FECHA_HASTA,"");
            tipo= getArguments().getString(TIPO,"");
        }
        
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        onCreate(savedInstanceState);
        footerView= ((LayoutInflater) this.getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_loader, null, false);
        vista=inflater.inflate(R.layout.fragment_transacciones, container, false);
            Toolbar toolbar= (Toolbar) vista.findViewById(R.id.card_toolbar);
            toolbar.inflateMenu(R.menu.tr_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
                @Override
                public boolean onMenuItemClick(MenuItem menuItem)
                {
                    Intent intent = getActivity().getIntent();
                    dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
                    Calendar fecha_actual=Calendar.getInstance();
                    fecha_actual.setTime(new Date());
                    Bundle bundle=new Bundle();
                    switch (menuItem.getItemId()){
                        case R.id.vermas:
                            bundle=new Bundle();
                            bundle.putInt("Offset",0);
                            bundle.putInt("Limit",cantidad_transacciones_a_mostrar);
                            intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                            bundle.putString("tipo","");
                            intent.putExtra("filtros",bundle);
                            break;
                        case R.id.verultimomes:
                            intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                            bundle=new Bundle();
                            bundle.putInt("Offset",0);
                            bundle.putInt("Limit",cantidad_transacciones_a_mostrar);
                            f_hasta=dateFormatter.format(fecha_actual.getTime());
                            fecha_actual.add(Calendar.MONTH,-1);
                            f_desde=dateFormatter.format(fecha_actual.getTime());
                            bundle.putString("tipo","");
                            bundle.putString("desde",f_desde);
                            bundle.putString("hasta",f_hasta);
                            intent.putExtra("filtros",bundle);
                            break;
                        case  R.id.veringresos:
                            bundle=new Bundle();
                            bundle.putInt("Offset",0);
                            bundle.putInt("Limit",cantidad_transacciones_a_mostrar);
                            f_hasta=dateFormatter.format(fecha_actual.getTime());
                            fecha_actual.add(Calendar.MONTH,-3);
                            f_desde=dateFormatter.format(fecha_actual.getTime());
                            intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                            Gestor_de_mensajes_usuario.mensaje("Esta operacion puede demorar",getContext());
                            bundle.putString("tipo","ingresos");
                            bundle.putString("desde",f_desde);
                            bundle.putString("hasta",f_hasta);
                            intent.putExtra("filtros",bundle);
                            break;
                        case  R.id.vercredito:
                            bundle=new Bundle();
                            bundle.putInt("Offset",0);
                            bundle.putInt("Limit",cantidad_transacciones_a_mostrar);
                            f_hasta=dateFormatter.format(fecha_actual.getTime());
                            fecha_actual.add(Calendar.MONTH,-3);
                            f_desde=dateFormatter.format(fecha_actual.getTime());
                            intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                            Gestor_de_mensajes_usuario.mensaje("Esta operacion puede demorar",getContext());
                            bundle.putString("tipo","tarjeta_credito");
                            bundle.putString("desde",f_desde);
                            bundle.putString("hasta",f_hasta);
                            intent.putExtra("filtros",bundle);
                            break;
                        case  R.id.veregresos:
                            bundle=new Bundle();
                            bundle.putInt("Offset",0);
                            bundle.putInt("Limit",cantidad_transacciones_a_mostrar);
                            f_hasta=dateFormatter.format(fecha_actual.getTime());
                            fecha_actual.add(Calendar.MONTH,-3);
                            f_desde=dateFormatter.format(fecha_actual.getTime());
                            Gestor_de_mensajes_usuario.mensaje("Esta operacion puede demorar",getContext());
                            intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                            bundle.putString("tipo","egresos");
                            bundle.putString("desde",f_desde);
                            bundle.putString("hasta",f_hasta);
                            intent.putExtra("filtros",bundle);
                            break;
                        case  R.id.verdebitos:
                            bundle=new Bundle();
                            bundle.putInt("Offset",0);
                            bundle.putInt("Limit",cantidad_transacciones_a_mostrar);
                            f_hasta=dateFormatter.format(fecha_actual.getTime());
                            fecha_actual.add(Calendar.MONTH,-3);
                            f_desde=dateFormatter.format(fecha_actual.getTime());
                            intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                            Gestor_de_mensajes_usuario.mensaje("Esta operacion puede demorar",getContext());
                            bundle.putString("tipo","debito_automatico");
                            bundle.putString("desde",f_desde);
                            bundle.putString("hasta",f_hasta);
                            intent.putExtra("filtros",bundle);
                            break;
                    }
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
            });
        ((View)vista.findViewById(R.id.loading_principal_transacciones)).setVisibility(View.VISIBLE);
        getActivity().getIntent().putExtra("Offset",offset+cantidad_transacciones_a_mostrar);
        getActivity().getIntent().putExtra("Limit",limit+cantidad_transacciones_a_mostrar);
        ListView lista= (ListView) vista.findViewById(R.id.lista);
        try{
            lista.addFooterView(footerView);
            final Tarea_transacciones tarea = new Tarea_transacciones(vista,footerView,getActivity().getIntent().getExtras(),this,lista);
            lista.setOnItemClickListener(null);
            tarea.execute(f_desde,f_hasta,tipo,String.valueOf(cantidad_transacciones_a_mostrar));
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                    try{
                        Intent detalle= new Intent(getActivity().getApplicationContext(), Detalle_transacciones.class);
                        detalle.putExtra("Fecha",tarea.transacciones.get(i).getFecha());
                        detalle.putExtra("Nro_Boleta",tarea.transacciones.get(i).getNro_boleta());
                        detalle.putExtra("Nombre",tarea.transacciones.get(i).getNombre());
                        detalle.putExtra("Identificacion",tarea.transacciones.get(i).getIdentificacion());
                        detalle.putExtra("Info",tarea.transacciones.get(i).getInfo());
                        detalle.putExtra("Concepto",tarea.transacciones.get(i).getConcepto());
                        detalle.putExtra("Bruto",tarea.transacciones.get(i).getBruto());
                        detalle.putExtra("Comision",tarea.transacciones.get(i).getComision());
                        detalle.putExtra("Neto",tarea.transacciones.get(i).getNeto());
                        detalle.putExtra("Saldo_acumulado",tarea.transacciones.get(i).getSaldo_acumulado());
                        if(!Transacciones.es_doble_display)
                            startActivity(detalle);
                        else{
                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction()
                                    .replace(R.id.include_detalle_transacciones, Detalle_transacciones_fragment.newInstance(detalle.getExtras(),getActivity().getApplicationContext()))
                                    .commit();
                        }
                    }catch (Exception ex){
                        Log.wtf("Error",ex.getMessage());
                        return;
                    }
                }
            });
        }catch (IOException ex){
            Log.e("Error",ex.getMessage());
            return null;
        }catch (Exception ex){
            Log.wtf("Error",ex.getMessage());
            return null;
        }
        return vista;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



}
