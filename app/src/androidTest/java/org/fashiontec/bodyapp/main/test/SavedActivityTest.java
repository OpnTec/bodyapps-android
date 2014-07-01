/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package org.fashiontec.bodyapp.main.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import org.fashiontec.bodyapp.main.R;
import org.fashiontec.bodyapp.main.SavedActivity;

public class SavedActivityTest extends ActivityInstrumentationTestCase2<SavedActivity> {
    SavedActivity activity;
    ListView list;

    public SavedActivityTest() {
        super(SavedActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity=getActivity();
        list=(ListView)activity.findViewById(R.id.listView1);
    }

    public void testList() throws Exception {
        assertNotNull("Listview null in Saved activity",list);
    }
}
