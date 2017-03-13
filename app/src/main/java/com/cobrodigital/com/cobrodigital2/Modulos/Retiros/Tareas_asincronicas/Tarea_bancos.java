package com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Tareas_asincronicas;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.cobrodigital.com.cobrodigital2.Gestores.Gestor_de_mensajes_usuario;
import com.cobrodigital.com.cobrodigital2.Model.Banco;
import com.cobrodigital.com.cobrodigital2.Modulos.Retiros.Adapters.Lista_bancos_adapter;
import com.cobrodigital.com.cobrodigital2.R;
import com.cobrodigital.com.cobrodigital2.core.CobroDigital;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ariel on 17/02/17.
 */

public class Tarea_bancos extends AsyncTask<Void,Void,Vector<Banco>> {
    Fragment context;
    ListView lista;
    View view;
    public Tarea_bancos(Fragment context, ListView lista,View v){
        this.context=context;
        this.lista=lista;
        this.view=v;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        view.findViewById(R.id.bancos_content).setVisibility(View.GONE);
    }

    @Override
    protected Vector<Banco> doInBackground(Void... voids) {
        Vector<Banco> bancos=new Vector<Banco>();
        try {
            CobroDigital.webservice.webservice_cuentas_bancarias.obtener_cuentas_bancarias();
            if (CobroDigital.webservice.obtener_resultado().equals("1")) {
                Object transicion[] = CobroDigital.webservice.obtener_datos().toArray();
                if (transicion.length > 0) {
                    for (Object dato: transicion) {
                        Banco banco = new Banco((String) dato);
                        bancos.add(banco);
                    }
                } else {
                    Log.e("Tarea_bancos",CobroDigital.webservice.obtener_log().toString());
                    Gestor_de_mensajes_usuario.mensaje("Debe ingresar Cuentas Bancarias para solicitar un retiro.",context.getContext());
                    return bancos;
                }
            }
        }catch (javax.net.ssl.SSLHandshakeException ex){
            Gestor_de_mensajes_usuario.dialogo("No podemos procesar la solicitud, intente mas tarde",context.getActivity());
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("bancos","termine");
        return bancos;
    }
    @Override
    protected void onPostExecute(Vector<Banco> bancos) {
        if (bancos.size()<=0){
            view.findViewById(R.id.bancos_content).setVisibility(View.VISIBLE);
            view.findViewById(R.id.mensaje_bancos).setVisibility(View.VISIBLE);
            lista.setVisibility(View.GONE);
            return;
        }
        Lista_bancos_adapter adapter=new Lista_bancos_adapter(context, R.layout.item_transacciones, bancos);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(adapter);
        view.findViewById(R.id.bancos_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.mensaje_bancos).setVisibility(View.GONE);
    }
}
