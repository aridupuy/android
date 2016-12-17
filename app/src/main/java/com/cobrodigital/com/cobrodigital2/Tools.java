package com.cobrodigital.com.cobrodigital2;

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
}
