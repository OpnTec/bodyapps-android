/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.fashiontec.bodyapps.managers.PersonManager;
import org.fashiontec.bodyapps.models.Measurement;
import org.fashiontec.bodyapps.models.MeasurementListModel;
import org.fashiontec.bodyapps.models.Person;
import org.fashiontec.bodyapps.sync.HDF;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Shows a list of saved measurements in the database.
 */
public class SavedActivity extends ActionBarActivity {

    public static String shownID;
    ProgressDialog progress;
    private static Activity activity;
    static SearchView search;
    static SavedAdapter adapter;
    public static List<MeasurementListModel> measurementsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        progress=new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        setContentView(R.layout.activity_saved);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Go to edit View
     * @param ID
     */
    public void edit(String ID) {
        Measurement editMeasurement = MeasurementManager.getInstance(this.getApplicationContext()).getMeasurement(ID);
        Intent intent = new Intent(SavedActivity.this, MeasurementActivity.class);
        intent.putExtra("measurement", editMeasurement);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getMenuInflater().inflate(R.menu.saved_options_land, menu);
            search = (SearchView) menu.findItem(R.id.saved_search_land).getActionView();
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    adapter.getFilter().filter(s);
                    return true;
                }
            });
            return true;
        }
        getMenuInflater().inflate(R.menu.saved_options, menu);

        search = (SearchView) menu.findItem(R.id.saved_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && shownID!=null) {
            int id = item.getItemId();
            switch (item.getItemId()) {
                case R.id.saved_options_menu_edit:
                    edit(shownID);
                    return true;

            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    /**
     * Manages the HDF getting process
     */
    public void getHDF(){
        Measurement m=MeasurementManager.getInstance(this).getMeasurement(shownID);
        Person p=PersonManager.getInstance(this).getPersonbyID(m.getPersonID());
        if(m.isSynced()==true){
            String path=getPath(p.getName(),shownID);
            new DownloadFileAsync().execute(m.getUserID(),m.getID(),path,p.getName());
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sync needed")
                    .setMessage("You have to sync before getting HDF!")
                    .setIcon(R.drawable.warning)
                    .setCancelable(false)
                    .setNegativeButton("Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();
        }
    }

    /**
     * Returns a file path to save the HDF
     * @param name
     * @param measurementID
     * @return
     */
    public String getPath(String name, String measurementID){
        String out=null;
        String HDF_DIRECTORY_NAME = "BodyApp" + File.separator + name;
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                HDF_DIRECTORY_NAME
        );

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(HDF_DIRECTORY_NAME, "Oops! Failed create "
                        + HDF_DIRECTORY_NAME + " directory");
            }
        }

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + measurementID+".zip");
        return mediaFile.getPath();
    }

    public static void mail(String path){
        File file=new File(path);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "Title");
        i.putExtra(Intent.EXTRA_TEXT, "Content");
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        i.setType("text/plain");
        activity.startActivity(Intent.createChooser(i, "Your email id"));
    }

    /**
     * UI fragment to display a list of measurements.
     */
    public static class SavedList extends Fragment {
        int shownIndex = 0;
        ListView list;
        Boolean dualPane;

        public SavedList() {
        }

        /**
         * Gets a List of measurementLisModel objects from database
         */
        public static List<MeasurementListModel> getDataForListView(
                Context context) {
            return MeasurementManager.getInstance(context).getList();

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_saved,
                    container, false);

            if (savedInstanceState != null) {
                // Restore last state for checked position.
                shownIndex = savedInstanceState.getInt("shownIndex", 0);
            }

            list = (ListView) rootView.findViewById(R.id.listView1);
            registerForContextMenu(list);
            measurementsList = getDataForListView(rootView.getContext().getApplicationContext());
            adapter = new SavedAdapter(rootView.getContext(),
                    measurementsList);
            list.setAdapter(adapter);
            if (measurementsList.size()!=0) {
                shownID = measurementsList.get(shownIndex).getID();
            }
            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View view,
                                        int index, long arg3) {
                    shownIndex = index;
                    shownID=measurementsList.get(index).getID();
                    viewSet(view);
                }
            });
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            super.onActivityCreated(savedInstanceState);

            View viewSaved = getActivity().findViewById(R.id.mesurements);
            dualPane = viewSaved != null
                    && viewSaved.getVisibility() == View.VISIBLE;
            list.setChoiceMode(ListView.CHOICE_MODE_NONE);

            if (dualPane) {
                if(measurementsList.size()!=0) {
                    viewSet(getView());
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            Activity activity = (Activity) v.getContext();
            MenuInflater inflator = activity.getMenuInflater();
            inflator.inflate(R.menu.saved_context, menu);

        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo iteminfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = iteminfo.position;
            String ID = (String) measurementsList.get(index).getID();
            SavedActivity sa = (SavedActivity) list.getContext();
            switch (item.getItemId()) {
                case R.id.saved_cont_menu_edit:
                    sa.edit(ID);
                    return true;
                case R.id.saved_cont_menu_hdf:
                    sa.getHDF();
                    return true;
            }
            return super.onContextItemSelected(item);
        }

        /**
         * Checks if UI has two panes or not. If it has two panes(If device is
         * horizontal) load measurement data in same activity. Otherwise start a
         * new activity to show data.
         */
        public void viewSet(View view) {

            if (dualPane) {
                ViewSavedFragment vsf = (ViewSavedFragment) getActivity()
                        .getFragmentManager().findFragmentById(R.id.container2);
                if (vsf == null) {
                    vsf = new ViewSavedFragment();
                    vsf.setItem(measurementsList.get(shownIndex)
                            .getID());
                    FragmentTransaction ft = getActivity().getFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.mesurements, vsf);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            } else {

                Intent intent = new Intent(view.getContext(),
                        ViewSavedActivity.class);
                if (measurementsList.size()!=0) {
                    intent.putExtra("ID",
                            (CharSequence) measurementsList.get(shownIndex)
                                    .getID()
                    );
                }
                startActivityForResult(intent, 1);
            }
        }

        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            outState.putInt("shownIndex", shownIndex);
        }

    }

    /**
     * Activity to show saved data if UI is in vertical mode.
     */
    public static class ViewSavedActivity extends ActionBarActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_saved);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                finish();
                return;
            }

            if (savedInstanceState == null) {
                android.app.Fragment vsf = new ViewSavedFragment();
                getFragmentManager().beginTransaction()
                        .add(R.id.container2, vsf).commit();
            }

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            // Inflate the menu; this adds items to the action bar if it is
            // present.
            getMenuInflater().inflate(R.menu.view_saved, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            switch (item.getItemId()) {
                case R.id.saved_options_menu_edit:
                    edit(shownID);
                    return true;

            }
            return super.onOptionsItemSelected(item);
        }
        public void edit(String ID) {
            Measurement editMeasurement = MeasurementManager.getInstance(this.getApplicationContext()).getMeasurement(ID);
            Intent intent = new Intent(ViewSavedActivity.this, MeasurementActivity.class);
            intent.putExtra("measurement", editMeasurement);
            startActivity(intent);
        }

    }

    /**
     * UI fragment to show data in a saved measurement record.
     */
    public static class ViewSavedFragment extends Fragment {

        private TextView person_id;
        private String measurementID;
        private Measurement measurement;
        private Person person;


        private TextView person_name;
        private TextView person_email;
        private TextView gender;
        private TextView mid_neck_girth;
        private TextView bust_girth;
        private TextView waist_girth;
        private TextView hip_girth;
        private TextView across_back_shoulder_width;
        private TextView shoulder_drop;
        private TextView shoulder_slope_degrees;
        private TextView arm_length;
        private TextView upper_arm_girth;
        private TextView armscye_girth;
        private TextView height;
        private TextView hip_height;
        private TextView wrist_girth;

        public ViewSavedFragment() {
            super();
        }

        public void setItem(String measurementID) {
            this.measurementID = measurementID;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_saved,
                    container, false);

            person_name = (TextView) rootView.findViewById(R.id.vsa_txt_person_name);
            person_email = (TextView) rootView.findViewById(R.id.vsa_txt_person_email);
            gender = (TextView) rootView.findViewById(R.id.vsa_txt_person_gender);
            mid_neck_girth = (TextView) rootView.findViewById(R.id.vsa_txt_mid_neck_girth);
            across_back_shoulder_width = (TextView) rootView.findViewById(R.id.vsa_txt_across_back_shoulder_width);
            shoulder_drop = (TextView) rootView.findViewById(R.id.vsa_txt_shoulder_drop);
            shoulder_slope_degrees = (TextView) rootView.findViewById(R.id.vsa_txt_shoulder_slope_degrees);
            bust_girth = (TextView) rootView.findViewById(R.id.vsa_txt_bust_girth);
            arm_length = (TextView) rootView.findViewById(R.id.vsa_txt_arm_length);
            armscye_girth = (TextView) rootView.findViewById(R.id.vsa_txt_armscye_girth);
            upper_arm_girth = (TextView) rootView.findViewById(R.id.vsa_txt_upper_arm_girth);
            wrist_girth = (TextView) rootView.findViewById(R.id.vsa_txt_wrist_girth);
            hip_girth = (TextView) rootView.findViewById(R.id.vsa_txt_hip_girth);
            waist_girth = (TextView) rootView.findViewById(R.id.vsa_txt_waist_girth);
            height = (TextView) rootView.findViewById(R.id.vsa_txt_height);
            hip_height = (TextView) rootView.findViewById(R.id.vsa_txt_hip_height);

            final Serializable extra = getActivity().getIntent()
                    .getSerializableExtra("ID");
            String ID = (String) extra;

            if (ID != null) {
                measurementID = ID;
            }
            measurement = MeasurementManager.getInstance(rootView.getContext().getApplicationContext()).getMeasurement(measurementID);
            if (measurement != null) {
                person = PersonManager.getInstance(rootView.getContext().getApplicationContext()).getPersonbyID(measurement.getPersonID());
            }
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (measurement != null) {
                String unit;
                if (measurement.getUnit() == 0) {
                    unit = " cm";
                } else {
                    unit = " inch";
                }
                person_name.setText("Name : " + person.getName());
                person_email.setText("Email : " + person.getEmail());
                String genderSt;
                if (person.getGender() == 0) {
                    genderSt = "fmale";
                } else {
                    genderSt = "male";
                }
                gender.setText("Gender : " + genderSt);
                mid_neck_girth.setText("Mid neck girth : " + measurement.getMid_neck_girth() + unit);
                across_back_shoulder_width.setText("Across back shoulder width : " + measurement.getAcross_back_shoulder_width() + unit);
                shoulder_drop.setText("Shoulder drop : " + measurement.getShoulder_drop() + unit);
                shoulder_slope_degrees.setText("Shoulder slope degrees : " + measurement.getShoulder_slope_degrees() + " degrees");
                bust_girth.setText("Bust girth : " + measurement.getBust_girth() + unit);
                arm_length.setText("Arm length : " + measurement.getArm_length() + unit);
                armscye_girth.setText("Armscye girth : " + measurement.getArmscye_girth() + unit);
                upper_arm_girth.setText("Upper arm girth : " + measurement.getUpper_arm_girth() + unit);
                wrist_girth.setText("Wrist girth : " + measurement.getWrist_girth() + unit);
                hip_girth.setText("Hip girth : " + measurement.getHip_girth() + unit);
                waist_girth.setText("Waist girth : " + measurement.getWaist_girth() + unit);
                height.setText("Height : " + measurement.getHeight() + unit);
                hip_height.setText("Hip height : " + measurement.getHip_height() + unit);


            }

        }

    }

    /**
     * Async task to download the HDF
     */
    private class DownloadFileAsync extends AsyncTask<String, String, String> {
        int result=0;
        String path="Downloads/BodyApp/";
        String actualPath;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }
        @Override
        protected String doInBackground(String... val) {
            path=path+val[3]+"/"+val[1]+".zip";
            actualPath=val[2];
            result=HDF.getHDF(val[0],val[1],val[2]);
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            progress.dismiss();
            Log.d("asynct1","posr exe");
            if(result>0){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Your HDF is saved at "+path+". Do you want to send it now?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mail(actualPath);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
            alert.show();
            Log.d("asynct1","posr exe2");
            }
        }
    }

}
