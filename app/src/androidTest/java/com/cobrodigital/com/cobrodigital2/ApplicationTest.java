package com.cobrodigital.com.cobrodigital2;

import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.test.ApplicationTestCase;

import com.cobrodigital.com.cobrodigital2.Services.serviceBoot;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        serviceBoot service=new serviceBoot();
        Intent intent = new Intent(String.valueOf(serviceBoot.class));
        service.onReceive(getContext(),intent);

    }
}