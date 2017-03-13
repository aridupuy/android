package com.cobrodigital.com.cobrodigital2.Gestores.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
public class Dialogo_alerta extends DialogFragment {
    private String mensaje;
    private String titulo;
    private String titulo_boton;
    public Dialogo_alerta(String mensaje,String titulo,String titulo_boton){
        this.mensaje=mensaje;
        this.titulo=titulo;
        this.titulo_boton=titulo_boton;
    }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setMessage(this.mensaje)
                    .setTitle(this.titulo)
                    .setPositiveButton(this.titulo_boton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
//                            dialog.cancel();
                        }
                    });

            return builder.create();
        }
    }
