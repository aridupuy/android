package com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Model.Banco;
import com.cobrodigital.com.cobrodigital2.R;

import java.util.Vector;

/**
 * Created by ariel on 21/02/17.
 */

public class Lista_bancos_adapter extends ArrayAdapter {
    Vector<Banco> items;
    Fragment context;
    private LayoutInflater inflater;
    private int resource;

    public Lista_bancos_adapter(Fragment context ,int resource ,Vector<Banco> items){
        super(context.getActivity().getApplicationContext() ,resource ,items);
        this.items=items;
        this.context=context;
        inflater=LayoutInflater.from(context.getContext());
        this.resource=resource;
    }
    public Object getItem(int position){
        return items.get(position);
    }
    @NonNull
    public View getView(int i, View view_item, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banco, parent, false);
        ((TextView) v.findViewById(R.id.Titular)).setText(items.get(i).getTitular());
        ((TextView) v.findViewById(R.id.Nombre)).setText(items.get(i).getNombre());
        ((TextView) v.findViewById(R.id.Cuit)).setText(items.get(i).getCuit().toString());
        return v;
    }


}
