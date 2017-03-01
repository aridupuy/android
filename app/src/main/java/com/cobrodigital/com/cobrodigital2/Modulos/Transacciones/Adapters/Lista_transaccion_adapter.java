package com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.R;

import java.util.List;
import java.util.Vector;

/**
 * Created by ariel on 15/12/16.
 */

public class Lista_transaccion_adapter extends ArrayAdapter{
    private List<Transaccion> items;
    private Context context;
    private int resource;
    private LayoutInflater inflater;
    private int lastposition=-1;
    public Lista_transaccion_adapter(Context context ,int resource ,Vector<Transaccion> items){
        super(context ,resource ,items);
//        super(context,resource,items);
        this.items=items;
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.resource=resource;
    }
    public Object getItem(int position){
        return items.get(position);
    }
    @NonNull
    public View getView(int i, View view_item, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transacciones, parent, false);
        ((TextView) v.findViewById(R.id.fecha)).setText(items.get(i).getFecha());
        ((TextView) v.findViewById(R.id.nombre)).setText(items.get(i).getNro_boleta());
        ((TextView) v.findViewById(R.id.neto)).setText(items.get(i).getNeto().toString());
        ((TextView) v.findViewById(R.id.info)).setText(items.get(i).getInfo());
        return v;
    }
    public void addItems(Vector<Transaccion> datos){
        for (Transaccion dato:datos) {
            items.add(dato);
        }
    }
}
