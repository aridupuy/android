package com.cobrodigital.com.cobrodigital2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.Tareas_asincronicas.Tarea_transacciones;
import com.cobrodigital.com.cobrodigital2.Transacciones;
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
    public Transacciones_fragment() {
        //constructor vacio obligatorio en fragments usar new instance
    }

    public static Transacciones_fragment newInstance(String desde, String hasta,String tipo,int cantidad_transacciones_a_mostrar) throws IOException {
        Transacciones_fragment fragment = new Transacciones_fragment();
        Bundle args = new Bundle();
        args.putString(FECHA_DESDE, desde);
        args.putString(FECHA_HASTA, hasta);
        args.putString(TIPO, tipo);
        args.putInt(Transacciones.CAMPO_RECIBIDO, cantidad_transacciones_a_mostrar);

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
            cantidad_transacciones_a_mostrar=getArguments().getInt(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
        }
        
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        onCreate(savedInstanceState);
        footerView= ((LayoutInflater) this.getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_loader, null, false);
        vista=inflater.inflate(R.layout.fragment_transacciones, container, false);
            Toolbar toolbar= (Toolbar) vista.findViewById(R.id.card_toolbar);
            toolbar.inflateMenu(R.menu.tr_menu);
            int anterior=cantidad_transacciones_a_mostrar;
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = getActivity().getIntent();
//                cantidad_transacciones_a_mostrar=0;
                dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
                Calendar fecha_actual=Calendar.getInstance();
                fecha_actual.setTime(new Date());
                switch (menuItem.getItemId()){
                    case R.id.vermas:
                        Bundle bundle=new Bundle();
                        intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                        bundle.putString("tipo","");
                        intent.putExtra("filtros",bundle);
                        break;
                    case R.id.verultimomes:
                        intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                        bundle=new Bundle();
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
        Tarea_transacciones tarea = new Tarea_transacciones(vista,footerView,getActivity().getIntent().getExtras(),this);
        tarea.execute(f_desde,f_hasta,tipo,String.valueOf(cantidad_transacciones_a_mostrar));

//        cantidad_transacciones_a_mostrar=anterior;
        return vista;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



}
