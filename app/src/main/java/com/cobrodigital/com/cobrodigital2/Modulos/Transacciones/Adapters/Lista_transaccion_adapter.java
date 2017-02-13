package com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Detalle_transacciones;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.Transacciones;
import com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.fragment.Detalle_transacciones_fragment;
import com.cobrodigital.com.cobrodigital2.R;

import java.util.List;
import java.util.Vector;

/**
 * Created by ariel on 15/12/16.
 */

public class Lista_transaccion_adapter extends ArrayAdapter{
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
    private int lastposition=-1;
    protected static ViewHolder holder;
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fecha, bol, neto,info;
        RelativeLayout row;
        public ViewHolder(View v){
            super(v);
            this.fecha=(TextView) v.findViewById(R.id.fecha);
            this.bol= (TextView) v.findViewById(R.id.nombre);
            this.neto= (TextView) v.findViewById(R.id.neto);
            this.info= (TextView) v.findViewById(R.id.info);
            Lista_transaccion_adapter.holder=this;
        }
    }
    @NonNull
    public View getView(int i, View view_item, ViewGroup parent) {

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

    public Lista_transaccion_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transacciones, parent, false);
        Lista_transaccion_adapter.ViewHolder vh = new Lista_transaccion_adapter.ViewHolder(v);
        return vh;
    }
}
