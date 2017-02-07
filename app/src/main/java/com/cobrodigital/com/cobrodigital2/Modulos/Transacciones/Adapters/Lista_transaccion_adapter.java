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

public class Lista_transaccion_adapter extends RecyclerView.Adapter {
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
//        super(context,resource,items);
        super();
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
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        View view_item = (LinearLayout) inflater.inflate( resource, null );
        if(this.holder==null){
            this.holder = new ViewHolder(view_item);
            view_item.setTag(this.holder);
        }
        else
            holder=(ViewHolder)view_item.getTag();
        final Transaccion transaccion = (Transaccion) getItem(position);
        if(position<=items.size()-1) {
            this.holder.fecha.setText(transaccion.getFecha());
            this.holder.bol.setText(transaccion.getNombre());
            this.holder.neto.setText("$" + String.format("%.2f", transaccion.getNeto()));
            this.holder.info.setText(transaccion.getInfo());
        }
        else{
            View footer = inflater.inflate(R.layout.footer_loader, (ViewGroup) view_item);
            ((ViewGroup) view_item).addView(footer);
//            this.holder=new ViewHolder(footer);
        }

//        Animation animation= AnimationUtils.loadAnimation(context,R.anim.scale);
//        view_item.startAnimation(animation);
//        try {
//            Thread.holdsLock(this);
//            Thread.currentThread().wait(animation.getDuration());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                try{
                    Intent detalle= new Intent(view.getContext().getApplicationContext(), Detalle_transacciones.class);
                    detalle.putExtra("Fecha",transaccion.getFecha());
                    detalle.putExtra("Nro_Boleta",transaccion.getNro_boleta());
                    detalle.putExtra("Nombre",transaccion.getNombre());
                    detalle.putExtra("Identificacion",transaccion.getIdentificacion());
                    detalle.putExtra("Info",transaccion.getInfo());
                    detalle.putExtra("Concepto",transaccion.getConcepto());
                    detalle.putExtra("Bruto",transaccion.getBruto());
                    detalle.putExtra("Comision",transaccion.getComision());
                    detalle.putExtra("Neto",transaccion.getNeto());
                    detalle.putExtra("Saldo_acumulado",transaccion.getSaldo_acumulado());
                    if(!Transacciones.es_doble_display)
                        view.getContext().startActivity(detalle);
                    else{
                        FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
                        fm.beginTransaction()
                                .replace(R.id.include_detalle_transacciones, Detalle_transacciones_fragment.newInstance(detalle.getExtras(),context))
                                .commit();
                    }
                }catch (Exception ex){
                    Log.wtf("Error",ex.getMessage());
                    return;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
