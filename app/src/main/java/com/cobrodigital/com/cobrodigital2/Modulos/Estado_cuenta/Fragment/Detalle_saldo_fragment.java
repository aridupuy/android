package com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Detalle_saldo;
import com.cobrodigital.com.cobrodigital2.Modulos.Estado_cuenta.Tareas_asincronicas.Tarea_Detalle_saldo;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ariel on 14/02/17.
 */

public class Detalle_saldo_fragment extends Fragment {
    public static Detalle_saldo_fragment newInstance(String saldo) {
        Bundle args = new Bundle();
        args.putString("Saldo",saldo);
        Detalle_saldo_fragment fragment = new Detalle_saldo_fragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static Detalle_saldo_fragment newInstance() {
        Detalle_saldo_fragment fragment = new Detalle_saldo_fragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v =inflater.inflate(R.layout.fragment_detalle_saldo,container,false);
        Tarea_Detalle_saldo tarea=new Tarea_Detalle_saldo(this,v);
        tarea.execute();
        return v;
    }


}
