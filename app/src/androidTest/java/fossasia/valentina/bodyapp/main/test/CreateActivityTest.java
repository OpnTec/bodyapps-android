/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package fossasia.valentina.bodyapp.main.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import fossasia.valentina.bodyapp.main.CreateActivity;
import fossasia.valentina.bodyapp.main.MeasurementActivity;
import fossasia.valentina.bodyapp.main.R;

public class CreateActivityTest extends ActivityInstrumentationTestCase2<CreateActivity> {
    CreateActivity activity;
    Button cancel;
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
        cancel=(Button)activity.findViewById(R.id.create_btn_cancel);
        create=(Button)activity.findViewById(R.id.create_btn_create);
        email=(EditText)activity.findViewById(R.id.create_txt_gmail);
        name=(EditText)activity.findViewById(R.id.create_txt_name);
        unit=(Spinner)activity.findViewById(R.id.create_spn_unit);
        gender=(Spinner)activity.findViewById(R.id.create_spn_gender);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });
    }

    public void testUIComponents() throws Exception {
        assertNotNull(cancel);
        assertNotNull(create);
        assertNotNull(email);
        assertNotNull(name);
        assertNotNull(unit);
        assertNotNull(gender);
    }

    public void testCreateButton() throws Exception {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MeasurementActivity.class.getName(), null, false);
        TouchUtils.clickView(this, create);
        MeasurementActivity startedActivity = (MeasurementActivity) monitor.waitForActivityWithTimeout(1000);
        assertNull(startedActivity);
        this.sendKeys(KeyEvent.KEYCODE_BACK);
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
        activity.closer();
        MeasurementActivity startedActivity = (MeasurementActivity) monitor.waitForActivityWithTimeout(3000);
        assertNotNull("incorrect",startedActivity);
//        Button save=(Button)startedActivity.findViewById(R.id.measurement_btn_save);
//        assertNotNull(save);
//        TouchUtils.clickView(this, save);
        this.sendKeys(KeyEvent.KEYCODE_BACK);
        this.sendKeys(KeyEvent.KEYCODE_BACK);

    }
}
