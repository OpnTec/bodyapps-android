/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.fashiontec.bodyapps.managers.UserManager;

/**
 * Landing Screen of the app. All the functions will start from here.
 */

public class MainActivity extends Activity implements OnClickListener {

    private Button create;
    private Button saved;
    private Button settings;
    private Button exit;
    private static AlertDialog alertDialog;

    public static final String AUTHORITY = "org.fashiontec.bodyapps.sync.provider";
    public static final String ACCOUNT_TYPE = "fashiontec.org";
    public static final String ACCOUNT = "dummyaccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exit = (Button) findViewById(R.id.main_btn_exit);
        exit.setOnClickListener(this);
        saved = (Button) findViewById(R.id.main_btn_saved);
        saved.setOnClickListener(this);
        settings = (Button) findViewById(R.id.main_btn_settings);
        settings.setOnClickListener(this);
        create = (Button) findViewById(R.id.main_btn_create);
        create.setOnClickListener(this);
        //Shows the alert dialog mentioning to sign in
        if (UserManager.getInstance(getBaseContext().getApplicationContext()).getCurrent() == null) {
            dialog("Login", "Go to \"Settings\" and sign in to continue.");
        }
        //Starts sync adapter at the start of the app
        if (UserManager.getInstance(getBaseContext().getApplicationContext()).getAutoSync()) {
            Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
            AccountManager accountManager = (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
            accountManager.addAccountExplicitly(newAccount, null, null);
            ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
            ContentResolver.requestSync(newAccount, "org.fashiontec.bodyapps.sync.provider", Bundle.EMPTY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Disables saved and create buttons if user is not signed in
        if (UserManager.getInstance(getBaseContext().getApplicationContext()).getCurrent() == null) {
            saved.setEnabled(false);
            create.setEnabled(false);
        } else {
            saved.setEnabled(true);
            create.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        //Handles the clicks of every Button
        Intent intent;
        switch (v.getId()) {
            case R.id.main_btn_create:
                intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_saved:
                intent = new Intent(MainActivity.this, SavedActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_exit:
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Shows a alert dialog with given title and message
     *
     * @param title
     * @param message
     */
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
}
