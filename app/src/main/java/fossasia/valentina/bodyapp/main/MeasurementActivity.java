/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package fossasia.valentina.bodyapp.main;

import java.io.Serializable;

import fossasia.valentina.bodyapp.managers.MeasurementManager;
import fossasia.valentina.bodyapp.managers.PersonManager;
import fossasia.valentina.bodyapp.models.Measurement;
import fossasia.valentina.bodyapp.models.Person;
import fossasia.valentina.bodyapp.sync.SyncMeasurement;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This is the view for adding data to created measurement. This view consist of
 * 13 fragments and supporting activity "ItemActivity" One fragment handles the
 * icon grid. Others are for each measurement group.
 */
public class MeasurementActivity extends Activity {
	private static Measurement measurement;
	private static Person person;
	private static String mID;
	private static Context context;
	private static ProgressDialog progress;
	private static GridView gridView;

	private final static String HEAD_FILL = "/0";
	private final static String NECK_FILL = "/1";
	private final static String SHOULDER_FILL = "/3";
	private final static String CHEST_FILL = "/1";
	private final static String ARM_FILL = "/4";
	private final static String HAND_FILL = "/0";
	private final static String HIP_AND_WAIST_FILL = "/2";
	private final static String LEG_FILL = "/0";
	private final static String FOOT_FILL = "/0";
	private final static String TRUNK_FILL = "/0";
	private final static String HEIGHTS_FILL = "/2";
	private final static String PICS_FILL = "/3";
	private static String[] filledFields = { "0/0", "0/1", "0/3", "0/1", "0/4",
			"0/0", "0/2", "0/0", "0/0", "0/0", "0/2", "0/3" };

	private static EditText mid_neck_girth;
	private static EditText bust_girth;
	private static EditText waist_girth;
	private static EditText hip_girth;
	private static EditText across_back_shoulder_width;
	private static EditText shoulder_drop;
	private static EditText shoulder_slope_degrees;
	private static EditText arm_length;
	private static EditText upper_arm_girth;
	private static EditText armscye_girth;
	private static EditText height;
	private static EditText hip_height;
	private static EditText wrist_girth;

	// Constants to separate measurement fragments at switch
	private final static int HEAD = 0;
	private final static int NECK = 1;
	private final static int SHOULDER = 2;
	private final static int CHEST = 3;
	private final static int ARM = 4;
	private final static int HAND = 5;
	private final static int HIP_AND_WAIST = 6;
	private final static int LEG = 7;
	private final static int FOOT = 8;
	private final static int TRUNK = 9;
	private final static int HEIGHTS = 10;
	private final static int PICS = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Serializable extra = this.getIntent().getSerializableExtra(
				"measurement");
		measurement = (Measurement) extra;
		setContentView(R.layout.activity_measurement);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.measurement, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

	/**
	 * Fragment which handles the icon grid and related functionality.
	 */
	public static class GridFragment extends Fragment {

		Boolean dualPane;// gets true if the device is on horizontal mode
		int shownIndex = 0;// gets the index of shown measurement set

		private Button btnSave;
		private Button btnSaveSync;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_measurement,
					container, false);
			gridView = (GridView) rootView.findViewById(R.id.grid_view);
			filledSet();
			GridAdapter ga=new GridAdapter(rootView.getContext());
			ga.result=filledFields;
			gridView.setAdapter(ga);

			// Restore last state for checked position
			if (savedInstanceState != null) {
				shownIndex = savedInstanceState.getInt("shownIndex", 0);
			}

			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int index, long id) {

					shownIndex = index;
					viewSet(view);
				}
			});

			btnSave = (Button) rootView.findViewById(R.id.measurement_btn_save);
			btnSave.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DBSaver(v.getContext().getApplicationContext());
					Activity host = (Activity) v.getContext();
					host.finish();
				}
			});

			btnSaveSync = (Button) rootView
					.findViewById(R.id.measurement_btn_save_sync);
			btnSaveSync.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DBSaver(v.getContext().getApplicationContext());
					person = PersonManager.getInstance(v.getContext().getApplicationContext())
							.getPersonbyID(measurement.getPersonID());
					System.out.println(person.getName());
					progress.show();
					context = v.getContext();
					new HttpAsyncTaskMeasurement()
							.execute("http://192.168.1.2:8020/user/measurements");

				}
			});

			// creates the progress dialog
			progress = new ProgressDialog(rootView.getContext());
			progress.setTitle("Synchronizing");
			progress.setMessage("Please wait...");
			progress.setCanceledOnTouchOutside(false);

			context = rootView.getContext();

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			View measurementView = getActivity().findViewById(
					R.id.measurement_frame);
			dualPane = measurementView != null
					&& measurementView.getVisibility() == View.VISIBLE;

			if (dualPane) {
				viewSet(getView());
			}

			if (measurement.getUserID().equals("NoID")
					|| measurement.getUserID().equals("NoUser")) {
				btnSaveSync.setEnabled(false);
			}
		}

		/**
		 * Sets the view according to the orientation of the device
		 * 
		 * @param view
		 */
		public void viewSet(View view) {

			if (dualPane) {
				Fragment item = (Fragment) getActivity().getFragmentManager()
						.findFragmentById(R.id.item_container);
				if (item == null) {
					item = chooseView(shownIndex);
					FragmentTransaction ft = getActivity().getFragmentManager()
							.beginTransaction();
					ft.replace(R.id.measurement_frame, item);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.commit();
				}
			} else {
				System.out.println("else");
				Intent editnote = new Intent(view.getContext(),
						ItemActivity.class);
				editnote.putExtra("item", shownIndex);
				startActivity(editnote);
			}
		}

		public static void filledSet() {
			int neck = 0;
			int shoulders = 0;
			int head = 0;
			int chest = 0;
			int arm = 0;
			int hand = 0;
			int hipAndWaist = 0;
			int leg = 0;
			int foot = 0;
			int trunk = 0;
			int heights = 0;
			int pics = 0;
			System.out.println(measurement.getMid_neck_girth()+"*");

			if (!measurement.getMid_neck_girth().equals("")) {
				neck += 1;
			}
			System.out.println("chk2");
			if (!measurement.getAcross_back_shoulder_width().equals("")) {
				shoulders += 1;
			}
			if (!measurement.getShoulder_drop().equals("")) {
				shoulders += 1;
			}
			if (!measurement.getShoulder_slope_degrees().equals("")) {
				shoulders += 1;
			}
			
			if (!measurement.getBust_girth().equals("")) {
				chest+=1;
			}
			
			if (!measurement.getArm_length().equals("")) {
				arm+=1;
			}
			if (!measurement.getUpper_arm_girth().equals("")) {
				arm+=1;
			}
			if (!measurement.getArmscye_girth().equals("")) {
				arm+=1;
			}
			if (!measurement.getWrist_girth().equals("")) {
				arm+=1;
			}
			
			if (!measurement.getHip_girth().equals("")) {
				hipAndWaist+=1;
			}
			if (!measurement.getWaist_girth().equals("")) {
				hipAndWaist+=1;
			}
			
			if (!measurement.getHeight().equals("")) {
				heights+=1;
			}
			if (!measurement.getHip_height().equals("")) {
				heights+=1;
			}
			
			filledFields[HEAD] = head+ HEAD_FILL;
			filledFields[HAND] = hand+ HAND_FILL;
			filledFields[NECK] = neck+ NECK_FILL;
			filledFields[LEG] = leg+ LEG_FILL;
			filledFields[FOOT] = foot+ FOOT_FILL;
			filledFields[TRUNK] = trunk+ TRUNK_FILL;
			filledFields[PICS] = pics+ PICS_FILL;
			filledFields[CHEST] = chest+ CHEST_FILL;
			filledFields[ARM] = arm+ ARM_FILL;
			filledFields[HIP_AND_WAIST] = hipAndWaist + HIP_AND_WAIST_FILL;
			filledFields[SHOULDER] = shoulders + SHOULDER_FILL;
			filledFields[HEIGHTS] = heights + HEIGHTS_FILL;
			

		}

		/**
		 * Choose which fragment to load according to the given index.
		 * 
		 * @param index
		 * @return
		 */
		public static Fragment chooseView(int index) {
			Fragment fragment = null;
			switch (index) {
			case HEAD:
				fragment = new Head();
				return fragment;
			case NECK:
				fragment = new Neck();
				return fragment;
			case SHOULDER:
				fragment = new Shoulder();
				return fragment;
			case CHEST:
				fragment = new Chest();
				return fragment;
			case ARM:
				fragment = new Arm();
				return fragment;
			case HAND:
				fragment = new Hand();
				return fragment;
			case HIP_AND_WAIST:
				fragment = new HipAndWaist();
				return fragment;
			case LEG:
				fragment = new Leg();
				return fragment;
			case FOOT:
				fragment = new Foot();
				return fragment;
			case TRUNK:
				fragment = new Trunk();
				return fragment;
			case HEIGHTS:
				fragment = new Heights();
				return fragment;
			case PICS:
				fragment = new Pics();
				return fragment;

			}
			return fragment;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			// saves the shown index
			outState.putInt("shownIndex", shownIndex);
		}

		/**
		 * Saves the measurement to the database
		 * 
		 * @param context
		 * @return
		 */
		public boolean DBSaver(Context context) {
			MeasurementManager.getInstance(context).addMeasurement(measurement);
			return true;
		}

	}

	/**
	 * Supporting activity to load measurement group fragments when device in
	 * portrait mode
	 */
	public static class ItemActivity extends Activity {

		Button btnPrev;
		Button btnNxt;
		private int extra;
		private Fragment fragment;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_item);
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				finish();
				return;
			}

			extra = (Integer) this.getIntent().getSerializableExtra("item");

			fragment = GridFragment.chooseView(extra);
			if (savedInstanceState == null) {
				getFragmentManager().beginTransaction()
						.add(R.id.item_container, fragment).commit();
			}

			btnPrev = (Button) findViewById(R.id.item_btn_prev);
			btnPrev.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (extra > 0) {
						extra -= 1;
					} else {
						extra = 11;
					}
					fragment = GridFragment.chooseView(extra);
					getFragmentManager().beginTransaction()
							.replace(R.id.item_container, fragment).commit();
				}
			});

			btnNxt = (Button) findViewById(R.id.item_btn_next);
			btnNxt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (extra < 11) {
						extra += 1;
					} else {
						extra = 0;
					}
					fragment = GridFragment.chooseView(extra);
					getFragmentManager().beginTransaction()
							.replace(R.id.item_container, fragment).commit();
				}
			});

		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {

			// Inflate the menu; this adds items to the action bar if it is
			// present.
			getMenuInflater().inflate(R.menu.item, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		
		@Override
		public void onBackPressed() {
			//alertDialog=null;
			finish();
		}

	}

	/**
	 * Fragments for measurement groups
	 */

	public static class Head extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.head, container, false);
			return rootView;
		}

	}

	public static class Neck extends Fragment {

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.neck, container, false);

			mid_neck_girth = (EditText) rootView
					.findViewById(R.id.mid_neck_girth);

			mid_neck_girth.setText(measurement.getMid_neck_girth());
			return rootView;
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			int filled = 0;
			Log.d("measurement", "onDestroy");
			measurement.setMid_neck_girth(mid_neck_girth.getText().toString());
			if (!mid_neck_girth.getText().toString().equals("")) {
				filled += 1;
			}
			GridAdapter ga = new GridAdapter(context);
			filledFields[NECK] = filled + NECK_FILL;
			ga.result = filledFields;
			gridView.setAdapter(ga);
		}

	}

	public static class Shoulder extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.shoulder, container,
					false);

			across_back_shoulder_width = (EditText) rootView
					.findViewById(R.id.across_back_shoulder_width);
			shoulder_drop = (EditText) rootView
					.findViewById(R.id.shoulder_drop);
			shoulder_slope_degrees = (EditText) rootView
					.findViewById(R.id.shoulder_slope_degrees);

			across_back_shoulder_width.setText(measurement
					.getAcross_back_shoulder_width());
			shoulder_drop.setText(measurement.getShoulder_drop());
			shoulder_slope_degrees.setText(measurement
					.getShoulder_slope_degrees());

			return rootView;
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			int filled = 0;

			measurement
					.setAcross_back_shoulder_width(across_back_shoulder_width
							.getText().toString());
			measurement.setShoulder_drop(shoulder_drop.getText().toString());
			measurement.setShoulder_slope_degrees(shoulder_slope_degrees
					.getText().toString());

			if (!across_back_shoulder_width.getText().toString().equals("")) {
				filled += 1;
			}
			if (!shoulder_drop.getText().toString().equals("")) {
				filled += 1;
			}
			if (!shoulder_slope_degrees.getText().toString().equals("")) {
				filled += 1;
			}

			GridAdapter ga = new GridAdapter(context);
			filledFields[SHOULDER] = filled + SHOULDER_FILL;
			ga.result = filledFields;
			gridView.setAdapter(ga);
		}

	}

	public static class Chest extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.chest, container, false);
			bust_girth = (EditText) rootView.findViewById(R.id.bust_girth);

			bust_girth.setText(measurement.getBust_girth());
			return rootView;
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			int filled = 0;
			measurement.setBust_girth(bust_girth.getText().toString());
			if (!bust_girth.getText().toString().equals("")) {
				filled += 1;
			}
			GridAdapter ga = new GridAdapter(context);
			filledFields[CHEST] = filled + CHEST_FILL;
			ga.result = filledFields;
			gridView.setAdapter(ga);
		}

	}

	public static class Arm extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.arm, container, false);

			arm_length = (EditText) rootView.findViewById(R.id.arm_length);
			upper_arm_girth = (EditText) rootView
					.findViewById(R.id.upper_arm_girth);
			armscye_girth = (EditText) rootView
					.findViewById(R.id.armscye_girth);
			wrist_girth = (EditText) rootView.findViewById(R.id.wrist_girth);

			arm_length.setText(measurement.getArm_length());
			upper_arm_girth.setText(measurement.getUpper_arm_girth());
			armscye_girth.setText(measurement.getArmscye_girth());
			wrist_girth.setText(measurement.getWrist_girth());
			return rootView;
		}

		@Override
		public void onDestroy() {

			super.onDestroy();
			int filled = 0;

			measurement.setArm_length(arm_length.getText().toString());
			measurement
					.setUpper_arm_girth(upper_arm_girth.getText().toString());
			measurement.setArmscye_girth(armscye_girth.getText().toString());
			measurement.setWrist_girth(wrist_girth.getText().toString());

			if (!arm_length.getText().toString().equals("")) {
				filled += 1;
			}
			if (!upper_arm_girth.getText().toString().equals("")) {
				filled += 1;
			}
			if (!armscye_girth.getText().toString().equals("")) {
				filled += 1;
			}
			if (!wrist_girth.getText().toString().equals("")) {
				filled += 1;
			}
			GridAdapter ga = new GridAdapter(context);
			filledFields[ARM] = filled + ARM_FILL;
			ga.result = filledFields;
			gridView.setAdapter(ga);
		}

	}

	public static class Hand extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.hand, container, false);
			return rootView;
		}
	}

	public static class HipAndWaist extends Fragment {

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.hip_and_waist, container,
					false);

			hip_girth = (EditText) rootView.findViewById(R.id.hip_girth);
			waist_girth = (EditText) rootView.findViewById(R.id.waist_girth);

			hip_girth.setText(measurement.getHip_girth());
			waist_girth.setText(measurement.getWaist_girth());
			return rootView;
		}

		@Override
		public void onDestroy() {

			super.onDestroy();
			int filled = 0;

			measurement.setHip_girth(hip_girth.getText().toString());
			measurement.setWaist_girth(waist_girth.getText().toString());

			if (!hip_girth.getText().toString().equals("")) {
				filled += 1;
			}
			if (!waist_girth.getText().toString().equals("")) {
				filled += 1;
			}
			GridAdapter ga = new GridAdapter(context);
			filledFields[HIP_AND_WAIST] = filled + HIP_AND_WAIST_FILL;
			ga.result = filledFields;
			gridView.setAdapter(ga);
		}
	}

	public static class Leg extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.leg, container, false);
			return rootView;
		}
	}

	public static class Foot extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.foot, container, false);
			return rootView;
		}
	}

	public static class Trunk extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.trunk, container, false);
			return rootView;
		}
	}

	public static class Heights extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater
					.inflate(R.layout.heights, container, false);

			height = (EditText) rootView.findViewById(R.id.height);
			hip_height = (EditText) rootView.findViewById(R.id.hip_height);

			height.setText(measurement.getHeight());
			hip_height.setText(measurement.getHip_height());
			return rootView;
		}

		@Override
		public void onDestroy() {

			super.onDestroy();
			int filled = 0;

			measurement.setHeight(height.getText().toString());
			measurement.setHip_height(hip_height.getText().toString());

			if (!height.getText().toString().equals("")) {
				filled += 1;
			}
			if (!hip_height.getText().toString().equals("")) {
				filled += 1;
			}
			GridAdapter ga = new GridAdapter(context);
			filledFields[HEIGHTS] = filled + HEIGHTS_FILL;
			ga.result = filledFields;
			gridView.setAdapter(ga);
		}
	}

	public static class Pics extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.pics, container, false);
			return rootView;
		}
	}

	public static void postUser(String url) {

		mID = SyncMeasurement.sendMeasurement(measurement, person);
	}

	/**
	 * Async task to send measurement to web application
	 */
	private static class HttpAsyncTaskMeasurement extends
			AsyncTask<String, Void, String> {

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Log.d("settings", "dataSent");
			// Insert the user to the DataBase
			if (mID != null) {
				Log.d("settings", "done");
				progress.dismiss();
				Activity host = (Activity) context;
				host.finish();
			} else {
				Log.d("settings", "cannot");
			}
		}

		@Override
		protected String doInBackground(String... urls) {

			postUser(urls[0]);
			System.out.println(mID + "async");
			return mID;
		}
	}

}
