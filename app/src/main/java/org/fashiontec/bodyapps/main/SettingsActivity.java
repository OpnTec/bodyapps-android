/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.main;

import java.io.InputStream;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.fashiontec.bodyapps.managers.UserManager;
import org.fashiontec.bodyapps.models.User;
import org.fashiontec.bodyapps.sync.SyncUser;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for settings. This activity handles user authentication via Google
 * play services and obtains user ID from web application. Then user gets added
 * to the DB
 */
public class SettingsActivity extends ActionBarActivity implements
		OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

	private static final int RC_SIGN_IN = 0;
	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	private boolean mSignInClicked;

	private ConnectionResult mConnectionResult;

	private SignInButton btnSignIn;
	private Button btnSignOut;
	private Button btnRevoke;
	private ImageView imgProfilePic;
	private TextView txtName;
	private TextView txtEmail;
	private TextView txtConnected;
	private LinearLayout llProfileLayout;
    private CheckBox chkAutoSync;
	private static ProgressDialog progress;
	private static AlertDialog alertDialog;
	private static AlertDialog alertDialog2;
	private String email;
	private String personName;
	private String userID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		btnSignIn = (SignInButton) findViewById(R.id.settings_btn_signin);
		btnSignOut = (Button) findViewById(R.id.settings_btn_signout);
		btnRevoke = (Button) findViewById(R.id.settings_btn_reovke);
		imgProfilePic = (ImageView) findViewById(R.id.settings_img_profile);
		txtName = (TextView) findViewById(R.id.settings_txt_name);
		txtEmail = (TextView) findViewById(R.id.settings_txt_email);
		txtConnected = (TextView) findViewById(R.id.settings_txt_connected);
		llProfileLayout = (LinearLayout) findViewById(R.id.settings_layout);

        chkAutoSync=(CheckBox)findViewById(R.id.settings_chk_autosync);
        chkAutoSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    UserManager.getInstance(getBaseContext().getApplicationContext()).setAutoSync(true);
                }else{
                    UserManager.getInstance(getBaseContext().getApplicationContext()).setAutoSync(false);
                }
            }
        });

		// Button click listeners
		btnSignIn.setOnClickListener(this);
		btnSignOut.setOnClickListener(this);
		btnRevoke.setOnClickListener(this);
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_PROFILE).build();

		progress = new ProgressDialog(this);
		progress.setTitle("Loading");
		progress.setMessage("Wait while loading...");
		progress.setCanceledOnTouchOutside(false);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Error Connecting")
				.setMessage("Error occured while connecting to Web application")
				.setIcon(R.drawable.warning)
				.setCancelable(false)
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		alertDialog = builder.create();

		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder.setTitle("Error Getting personal info")
				.setMessage("Could not get personal info from Google")
				.setIcon(R.drawable.warning)
				.setCancelable(false)
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		alertDialog2 = builder2.create();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Sign in the user to Google
	 */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	private void resolveSignInError() {
		if (mConnectionResult == null) {
			return;
		}

		if (mConnectionResult.hasResolution()) {

			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	/**
	 * Sign out user from Google
	 */
	private void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			UserManager.getInstance(getBaseContext().getApplicationContext()).unsetCurrent();
			updateUI(false);
		}
	}

	/**
	 * Revoking access from google
	 * */
	private void revokeGplusAccess() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
					.setResultCallback(new ResultCallback<Status>() {
						@Override
						public void onResult(Status arg0) {
							Log.e("settings", "User access revoked!");
							mGoogleApiClient.connect();
							updateUI(false);
						}

					});
		}
		UserManager.getInstance(getBaseContext().getApplicationContext()).unsetCurrent();
		reload();
	}

	/**
	 * Update the UI according to isSign in
	 * 
	 * @param isSignedIn
	 */
	private void updateUI(boolean isSignedIn) {
		if (isSignedIn) {
			btnSignIn.setVisibility(View.GONE);
			btnSignOut.setVisibility(View.VISIBLE);
			btnRevoke.setVisibility(View.VISIBLE);
			llProfileLayout.setVisibility(View.VISIBLE);
		} else {
			btnSignIn.setVisibility(View.VISIBLE);
			btnSignOut.setVisibility(View.GONE);
			btnRevoke.setVisibility(View.GONE);
			llProfileLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settings_btn_signin:
			// Signin button clicked
			signInWithGplus();
			break;
		case R.id.settings_btn_signout:
			// Signout button clicked
			signOutFromGplus();
			break;
		case R.id.settings_btn_reovke:
			// Revoke access button clicked
			revokeGplusAccess();
			break;
		}

	}

	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);

				personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				Log.e("settings", "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Image: " + personPhotoUrl);

				txtName.setText(personName);
				txtEmail.setText(email);

				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
				email = null;
				personName = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			email = null;
			personName = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

		// Get user's information
		getProfileInformation();
		Log.d("settings", "here");

		if (email != null && personName != null) {
			
			// creates a new user and check if he exists on db
			User user = new User(email, personName, userID, false);
			String isUser = UserManager.getInstance(getBaseContext().getApplicationContext()).isUser(
					user);

			if (isUser == null) {
				// if user is not in DB a post goes to web app and gets a ID for
				// user.
				// Then he will be added to the DB and set as current user.
				progress.show();
				new HttpAsyncTaskUser().execute();
			} else {

				if (isUser.equals("NoID")) {
					// if user exists in DB and doesn't have a ID, try to get ID
					progress.show();
					new HttpAsyncTaskUser()
							.execute();
				} else {
					// if user exists in DB and has a ID just sets him current
					// user
					UserManager.getInstance(getBaseContext().getApplicationContext()).setCurrent(user);
					txtConnected.setText("User connected");
					userID = isUser;
                    chkAutoSync.setChecked(UserManager.getInstance(getBaseContext().getApplicationContext()).getAutoSync());
                }
			}
			// Update the UI after sign in
			updateUI(true);
		} else {
			alertDialog2.show();
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);
	}

	public void reload() {

		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();

		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	/**
	 * Async task to get profile image from google.
	 */
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	/**
	 * Call to syncUser method
	 */
	public void postUser() {

		userID = SyncUser.getUserID(email, personName);
	}

	/**
	 * Async task to send user data to web app and get user ID.
	 */
	private class HttpAsyncTaskUser extends AsyncTask<String, Void, String> {

		@Override
		protected void onPostExecute(String result) {
			Log.d("settings", "dataSent");

			if (userID != "") {

				User user = new User(email, personName, userID, true);
				String isUser = UserManager.getInstance(getBaseContext().getApplicationContext())
						.isUser(user);
				if (isUser == null) {
					// adds the user to the DB
					UserManager.getInstance(getBaseContext().getApplicationContext()).addUser(user);

				} else {
					UserManager.getInstance(getBaseContext().getApplicationContext()).setID(user);
                    UserManager.getInstance(getBaseContext().getApplicationContext()).setAutoSync(true);
					UserManager.getInstance(getBaseContext().getApplicationContext()).setCurrent(user);
				}
				//if user added measurements before sign in, those measurements will be added to the signed in user
				MeasurementManager.getInstance(getBaseContext().getApplicationContext()).setUserID(userID);
				txtConnected.setText("User connected");
                chkAutoSync.setChecked( UserManager.getInstance(getBaseContext().getApplicationContext()).getAutoSync());
			} else {
				Log.d("settings", "cannot");
				// adds the user to the DB, but without the ID if the user
				// currently not on db
				User user = new User(email, personName, "NoID", false);
				String isUser = UserManager.getInstance(getBaseContext().getApplicationContext())
						.isUser(user);
				if (isUser == null) {
					UserManager.getInstance(getBaseContext().getApplicationContext()).addUser(user);
					MeasurementManager.getInstance(getBaseContext().getApplicationContext()).setUserID("NoID");
				}
				UserManager.getInstance(getBaseContext().getApplicationContext()).setCurrent(user);
				MeasurementManager.getInstance(getBaseContext().getApplicationContext()).setUserID("NoID");
				alertDialog.show();
				txtConnected.setText("User not connected");
                chkAutoSync.setChecked(false);
                chkAutoSync.setEnabled(false);
            }
			progress.dismiss();
		}

		@Override
		protected String doInBackground(String... urls) {

			postUser();
			return userID;
		}
	}
}
