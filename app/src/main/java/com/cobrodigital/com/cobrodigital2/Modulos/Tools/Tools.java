package com.cobrodigital.com.cobrodigital2.Modulos.Tools;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by ariel on 15/12/16.
 */
public class Tools {
    private static Tools ourInstance = new Tools();

    public static Tools getInstance() {
        return ourInstance;
    }

    private Tools() {
    }

    @SuppressLint("LongLogTag")
    public static void developerLog(String texto){
        Log.d("MENSAJE_DEL_DESARROLLADOR",texto);
    }
    static public Object bytes2Object( byte raw[] ) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object o = ois.readObject();
        return o;
    }
    static public byte[] object2Bytes( Object o ) throws IOException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
//        oos.writeObject( o );
        return baos.toByteArray();
    }

}
