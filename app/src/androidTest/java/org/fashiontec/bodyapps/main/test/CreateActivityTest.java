/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package org.fashiontec.bodyapps.main.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.fashiontec.bodyapps.main.CreateActivity;
import org.fashiontec.bodyapps.main.MeasurementActivity;
import org.fashiontec.bodyapps.main.R;

public class CreateActivityTest extends ActivityInstrumentationTestCase2<CreateActivity> {
    CreateActivity activity;
    Button create;
    EditText email;
    EditText name;
    Spinner unit;
    Spinner gender;

    public CreateActivityTest(){
        super(CreateActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        create=(Button)activity.findViewById(R.id.create_btn_create);
        email=(EditText)activity.findViewById(R.id.create_txt_gmail);
        name=(EditText)activity.findViewById(R.id.create_txt_name);
        unit=(Spinner)activity.findViewById(R.id.create_spn_unit);
        gender=(Spinner)activity.findViewById(R.id.create_spn_gender);
        InputMethodManager imm = (InputMethodManager)activity.getBaseContext().getSystemService(
                activity.getBaseContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
    }

    public void testUIComponents() throws Exception {
        assertNotNull(create);
        assertNotNull(email);
        assertNotNull(name);
        assertNotNull(unit);
        assertNotNull(gender);
    }

    public void testMeasurementActivity() throws Exception {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                name.setText("test_name");
            }
        });

        getInstrumentation().waitForIdleSync();
        assertEquals("Name incorrect", "test_name", name.getText().toString());

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                email.setText("test_email");
            }
        });

        getInstrumentation().waitForIdleSync();
        assertEquals("Email incorrect", "test_email", email.getText().toString());
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MeasurementActivity.class.getName(), null, false);
        activity.setData();
//        activity.closer();
        MeasurementActivity startedActivity = (MeasurementActivity) monitor.waitForActivityWithTimeout(3000);
        assertNotNull("incorrect",startedActivity);

        this.sendKeys(KeyEvent.KEYCODE_BACK);
        this.sendKeys(KeyEvent.KEYCODE_BACK);
    }
}
