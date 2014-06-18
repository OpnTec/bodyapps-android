/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package fossasia.valentina.bodyapp.main.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import fossasia.valentina.bodyapp.main.MainActivity;
import fossasia.valentina.bodyapp.main.R;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

    MainActivity activity;

    public MainActivityTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        activity=getActivity();
    }

    @SmallTest
    public void testButton() throws Exception{
        Button create=(Button)activity.findViewById(R.id.main_btn_create);
        Button saved=(Button)activity.findViewById(R.id.main_btn_saved);
        Button settings=(Button)activity.findViewById(R.id.main_btn_settings);
        Button exit=(Button)activity.findViewById(R.id.main_btn_exit);
        Boolean val=create!=null && saved!=null && settings!=null && exit!=null;
        assertTrue(val);

    }
}
