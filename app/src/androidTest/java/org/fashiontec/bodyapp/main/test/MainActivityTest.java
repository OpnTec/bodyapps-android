/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package org.fashiontec.bodyapp.main.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.widget.Button;

import org.fashiontec.bodyapp.main.CreateActivity;
import org.fashiontec.bodyapp.main.MainActivity;
import org.fashiontec.bodyapp.main.R;
import org.fashiontec.bodyapp.main.SavedActivity;
import org.fashiontec.bodyapp.main.SettingsActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;
    Button create;
    Button saved;
    Button settings;
    Button exit;

    public MainActivityTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        create = (Button) activity.findViewById(R.id.main_btn_create);
        saved = (Button) activity.findViewById(R.id.main_btn_saved);
        settings = (Button) activity.findViewById(R.id.main_btn_settings);
        exit = (Button) activity.findViewById(R.id.main_btn_exit);
    }

    @SmallTest
    public void testButtons() throws Exception {
        Boolean val = create != null && saved != null && settings != null && exit != null;
        assertTrue(val);
    }

    public void testSavedActivity() throws Exception {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SavedActivity.class.getName(), null, false);
        TouchUtils.clickView(this, saved);
        SavedActivity startedActivity = (SavedActivity) monitor.waitForActivityWithTimeout(1000);
        assertNotNull(startedActivity);
        this.sendKeys(KeyEvent.KEYCODE_BACK);
    }

    public void testSettingsActivity() throws Exception {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);
        TouchUtils.clickView(this, settings);
        SettingsActivity startedActivity = (SettingsActivity) monitor.waitForActivityWithTimeout(1000);
        assertNotNull(startedActivity);
        this.sendKeys(KeyEvent.KEYCODE_BACK);
    }

    public void testCreateActivity() throws Exception {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(CreateActivity.class.getName(), null, false);
        TouchUtils.clickView(this, create);
        CreateActivity startedActivity = (CreateActivity) monitor.waitForActivityWithTimeout(1000);
        assertNotNull(startedActivity);
        this.sendKeys(KeyEvent.KEYCODE_BACK);
    }

}
