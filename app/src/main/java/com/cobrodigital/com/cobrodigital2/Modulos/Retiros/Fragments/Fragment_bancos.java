package com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Tareas_asincronicas.Tarea_bancos;
import com.cobrodigital.com.cobrodigital2.R;

/**
 * Created by ariel on 17/02/17.
 */

public class Fragment_bancos extends Fragment {
    public static Fragment_bancos newInstance() {
        Fragment_bancos fragment = new Fragment_bancos();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v= inflater.inflate(R.layout.fragment_bancos,container, false);
        ListView lista= (ListView) v.findViewById(R.id.lista_bancos);
        Tarea_bancos tarea= new Tarea_bancos(this,lista,v);
        tarea.execute();
        return v;
    }


}
