/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.fashiontec.bodyapps.managers.PersonManager;
import org.fashiontec.bodyapps.managers.UserManager;
import org.fashiontec.bodyapps.models.Measurement;
import org.fashiontec.bodyapps.models.Person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CreateActivity extends Activity {
    private Button btnCreate;
    private Spinner spnUnits;
    private Spinner spnGender;
    private TextView txtEmail;
    private TextView txtName;
    private Person person;
    private Measurement measurement;
    Context context;
    private static AlertDialog alertDialog;
    static final String TAG = CreateActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        btnCreate = (Button) findViewById(R.id.create_btn_create);
        btnCreate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setData();
            }
        });

        spnUnits = (Spinner) findViewById(R.id.create_spn_unit);
        ArrayAdapter<CharSequence> uitsAdapter = ArrayAdapter
                .createFromResource(this, R.array.units_array,
                        android.R.layout.simple_spinner_item);
        spnUnits.setAdapter(uitsAdapter);

        spnGender = (Spinner) findViewById(R.id.create_spn_gender);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter
                .createFromResource(this, R.array.gender_array,
                        android.R.layout.simple_spinner_item);
        spnGender.setAdapter(genderAdapter);
        context = this;

    }

    public boolean setData() {
        txtEmail = (TextView) findViewById(R.id.create_txt_gmail);
        txtName = (TextView) findViewById(R.id.create_txt_name);
        String name;
        String email;
        email = txtEmail.getText().toString();
        if (email.equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            dialog("Error", "Please enter valid email address");
            return false;
        }
        name = txtName.getText().toString();
        if (name.equals("") || name.equals(" ")) {
            dialog("Error", "Please enter valid name");
            return false;
        }
        setMeasurement(name, email);
        return true;
    }

    private void setMeasurement(String name, String email) {
        person = new Person(email, name, spnGender.getSelectedItemPosition());
        // adds the person to DB and gets his ID
        int personID = PersonManager.getInstance(this.getApplicationContext()).getPerson(person);
        if (personID == -1) {
            PersonManager.getInstance(this).addPerson(person);
            personID = PersonManager.getInstance(this.getApplicationContext()).getPerson(person);
        }

        person.setID(personID);

        String userID = UserManager.getInstance(this).getCurrent();
        if (userID != null) {

            measurement = new Measurement(getID(), userID, person.getID(),
                    spnUnits.getSelectedItemPosition());
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String dateText = "";
            try {
                Date date = new Date();
                dateText = dateformat.format(date);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            measurement.setCreated(dateText);
            closer();

        } else {
            measurement = new Measurement(getID(), "NoUser", person.getID(),
                    spnUnits.getSelectedItemPosition());

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String dateText = "";
            try {
                Date date = new Date();
                dateText = dateformat.format(date);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            measurement.setCreated(dateText);
            closer();
        }


    }

    /**
     * Gets a UUID for measurement
     *
     * @return
     */
    public String getID() {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }

    public void closer() {
        Intent intent = new Intent(CreateActivity.this,
                MeasurementActivity.class);
        intent.putExtra("measurement", measurement);
        startActivity(intent);
        Activity host = (Activity) context;
        host.finish();
    }

    public void dialog(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.warning)
                .setCancelable(false)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        }
                );

        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
