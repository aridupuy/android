package com.cobrodigital.com.cobrodigital2.Modulos.Transacciones.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cobrodigital.com.cobrodigital2.R;
public class Detalle_transacciones_fragment extends Fragment {
    private Bundle bundle;
    private Context context;
    public Detalle_transacciones_fragment() {
    }
    public static Detalle_transacciones_fragment newInstance(Bundle bundle,Context context) {
        Detalle_transacciones_fragment fragment = new Detalle_transacciones_fragment();
        fragment.setArguments(bundle);
        fragment.context=context;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = new View(getActivity().getApplicationContext());
        try {
            if (bundle != null && bundle.containsKey("Fecha")) {
                view = inflater.inflate(R.layout.fragment_detalle_transacciones_fragment, container, false);
                ((TextView) view.findViewById(R.id.fecha)).setText(bundle.getString("Fecha"));
                ((TextView) view.findViewById(R.id.nro_boleta)).setText(bundle.getString("Nro_boleta"));
                ((TextView) view.findViewById(R.id.identificacion)).setText(bundle.getString("Identificacion"));
                ((TextView) view.findViewById(R.id.nombre)).setText(bundle.getString("Nombre"));
                ((TextView) view.findViewById(R.id.info)).setText(bundle.getString("Info"));
                ((TextView) view.findViewById(R.id.concepto)).setText(bundle.getString("Concepto", ""));
                ((TextView) view.findViewById(R.id.bruto)).setText("$" + bundle.getString("Bruto", ""));
                ((TextView) view.findViewById(R.id.comision)).setText("$" + bundle.getString("Comision", ""));
                ((TextView) view.findViewById(R.id.neto)).setText("$" + bundle.getDouble("Neto", 0.00D));
                ((TextView) view.findViewById(R.id.saldo_acumulado)).setText("$" + bundle.getString("Saldo_acumulado"));
            }
            return view;
        }
        catch(Exception e){
            Log.e("Error",e.getMessage());
            return view;
        }
    }

}
