/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.fashiontec.bodyapps.managers.UserManager;

/**
 * 
 * Landing Screen of the app. All the functions will start from here.
 *
 */

public class MainActivity extends Activity implements OnClickListener{

	private Button create;
	private Button saved;
	private Button settings;
	private Button exit;

    public static final String AUTHORITY = "org.fashiontec.bodyapps.sync.provider";
    public static final String ACCOUNT_TYPE = "fashiontec.org";
    public static final String ACCOUNT = "dummyaccount";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		exit=(Button)findViewById(R.id.main_btn_exit);
		exit.setOnClickListener(this);
		saved=(Button)findViewById(R.id.main_btn_saved);
		saved.setOnClickListener(this);
		settings=(Button)findViewById(R.id.main_btn_settings);
		settings.setOnClickListener(this);
		create=(Button)findViewById(R.id.main_btn_create);
		create.setOnClickListener(this);
        if(UserManager.getInstance(getBaseContext().getApplicationContext()).getAutoSync()) {
            System.out.println("MainActivity.onCreate");
            Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
            AccountManager accountManager = (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
            accountManager.addAccountExplicitly(newAccount, null, null);
            ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
            ContentResolver.requestSync(newAccount, "org.fashiontec.bodyapps.sync.provider", Bundle.EMPTY);
        }
	}

	@Override
	public void onClick(View v) {
		//Handles the clicks of every Button
		Intent intent;
		switch (v.getId()) {
		case R.id.main_btn_create:
			intent=new Intent(MainActivity.this, CreateActivity.class);
			startActivity(intent);
			break;
		case R.id.main_btn_saved:
			intent=new Intent(MainActivity.this, SavedActivity.class);
			startActivity(intent);
			break;
		case R.id.main_btn_settings:
			intent=new Intent(MainActivity.this, SettingsActivity.class);
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


}
