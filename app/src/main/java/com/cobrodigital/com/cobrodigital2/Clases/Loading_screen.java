package com.cobrodigital.com.cobrodigital2.Clases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.cobrodigital.com.cobrodigital2.R;

import java.util.Objects;

/**
 * Created by ariel on 26/09/16.
 */
public class Loading_screen extends AsyncTask <Object,Void,String> {
    Context context;
    public Loading_screen(Context context){
        this.context=context;
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.setContentView(R.layout.pantalla_espera);
        ImageView img = (ImageView)activity.findViewById(R.id.loadingView);
        img.setBackgroundResource(R.drawable.loading_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.setOneShot(false);
        frameAnimation.start();
        int a=R.layout.pantalla_espera;
        System.out.println(a);
    }
    @Override
    protected String doInBackground(final Object... objects) {
        new Thread(){
            @Override
            public void run() {
                AppCompatActivity app = (AppCompatActivity)context;
                app.startActivity((Intent) objects[0]);
                super.run();
            }
        }.run();
        return null;
    }
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Activity activity = (Activity) context;
        activity.finish();

    }
}
