package com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.R;

import java.util.List;
import java.util.Vector;

/**
 * Created by ariel on 15/12/16.
 */

public class Lista_transaccion_adapter extends ArrayAdapter {
    private List<Transaccion> items;

    TextView fecha;
    TextView bol;
    TextView neto;
    TextView info;
    TextView nombre;
    TextView concepto;
    private Context context;
    private int resource;
    private LayoutInflater inflater;
    public Lista_transaccion_adapter(Context context ,int resource ,Vector<Transaccion> items){
        super(context,resource,items);
        this.items=items;
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.resource=resource;
    }
    public View getView(int i,View view_item, ViewGroup parent) {
        view_item = (LinearLayout) inflater.inflate( resource, null );
        Transaccion transaccion=(Transaccion) getItem(i);
        fecha= (TextView) view_item.findViewById(R.id.fecha);
        fecha.setText(transaccion.getFecha());
        bol= (TextView) view_item.findViewById(R.id.nombre);
        bol.setText(transaccion.getNombre());
        neto= (TextView) view_item.findViewById(R.id.neto);
        neto.setText("$"+String.format("%.2f",transaccion.getNeto()));
        info= (TextView) view_item.findViewById(R.id.info);
        info.setText(transaccion.getInfo());
        return view_item;
    }
    public void addItems(Vector<Transaccion> datos){
        for (Transaccion dato:datos) {
            items.add(dato);
        }
    }
    public List<Transaccion> getItems(){
        return items;
    }

}
