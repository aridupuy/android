package com.cobrodigital.com.cobrodigital2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.Tareas_asincronicas.Tarea_transacciones;
import com.cobrodigital.com.cobrodigital2.Tools;
import com.cobrodigital.com.cobrodigital2.Transacciones;
import com.j256.ormlite.field.types.ByteArrayType;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.zip.Inflater;
public class Transacciones_fragment extends Fragment {

    private static final String FECHA_DESDE = "fecha_desde";
    private static final String FECHA_HASTA = "fecha_hasta";
    private static final String CONTAINER= "container";

    private static int cantidad_transacciones_a_mostrar=5;
    private View vista= null;
    private String f_desde;
    private String f_hasta;
    private ViewGroup Container;
    public Transacciones_fragment() {
        //constructor vacio obligatorio en fragments usar new instance
    }

    public static Transacciones_fragment newInstance(String desde, String hasta,ViewGroup Container,int cantidad_transacciones_a_mostrar) throws IOException {
        Transacciones_fragment fragment = new Transacciones_fragment();
        Bundle args = new Bundle();
        args.putString(FECHA_DESDE, desde);
        args.putString(FECHA_HASTA, hasta);
        args.putByteArray(CONTAINER, Tools.object2Bytes(Container));
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
            byte[] array=getArguments().getByteArray(CONTAINER);
            cantidad_transacciones_a_mostrar=getArguments().getInt(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
            try {
                Container=(ViewGroup) Tools.bytes2Object(array);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        try {
            vista = listar(R.layout.fragment_transacciones,getActivity().getLayoutInflater(),container,savedInstanceState);
            Log.wtf("app","Ejecutando");
            TextView vermas=(TextView)vista.findViewById(R.id.transascompletas);
            if(vermas!=null)
                vermas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int anterior=cantidad_transacciones_a_mostrar;
                        cantidad_transacciones_a_mostrar=0;
                        Intent intent = getActivity().getIntent();
                        intent.putExtra(Transacciones.CAMPO_RECIBIDO,cantidad_transacciones_a_mostrar);
                        startActivity(intent);
                        cantidad_transacciones_a_mostrar=anterior;
                        getActivity().finish();
                }
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return vista;
    }


    private  View listar(final int vista, LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) throws ParseException {
        View view=inflater.inflate(vista, container, false);
        Tarea_transacciones tarea = new Tarea_transacciones(view,savedInstanceState,this);
        tarea.execute("","",""+cantidad_transacciones_a_mostrar);
        return view;
    }


}
