/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package fossasia.valentina.bodyapp.main.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import fossasia.valentina.bodyapp.main.CreateActivity;
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
    }
}
