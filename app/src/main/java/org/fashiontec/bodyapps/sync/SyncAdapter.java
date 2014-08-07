/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.fashiontec.bodyapps.managers.PersonManager;
import org.fashiontec.bodyapps.managers.UserManager;
import org.fashiontec.bodyapps.models.Measurement;
import org.fashiontec.bodyapps.models.Person;

import java.util.Date;
import java.util.List;

/**
 * Manages the automatic sync of measurements with web API
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    static final String TAG = SyncAdapter.class.getName();


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {

        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {
        Log.d(TAG, "sync happened");
        Measurement measurement;

        String userID = UserManager.getInstance(getContext().getApplicationContext()).getCurrent();
        List<String> delList=MeasurementManager.getInstance(getContext().getApplicationContext()).getDelList();

        for(String ID: delList){
            boolean out=SyncMeasurement.delMeasurement(ID, userID);
            Log.d(TAG, ID);
            if(out){
                MeasurementManager.getInstance(getContext().getApplicationContext()).removeDelEntry(ID);
            }
        }

        String list[] = SyncMeasurement.getSyncList(UserManager.getInstance(
                getContext().getApplicationContext()).getLastSync(), userID);
        boolean syncOK = true;

        if (list != null) {

            for (int i = 0; i < list.length; i++) {
                String out = SyncMeasurement.getMeasurement(list[i],
                        getContext().getApplicationContext(), userID);
                if (!out.equals(list[i])) {
                    syncOK = false;
                    break;
                }
            }
        }

        while ((measurement = MeasurementManager.getInstance(
                getContext().getApplicationContext()).getMeasurementSync()) != null) {

            Person person = PersonManager.getInstance(getContext().getApplicationContext())
                    .getPersonbyID(measurement.getPersonID());
            boolean syncedOnce = MeasurementManager.getInstance(getContext().getApplicationContext())
                    .isSyncedOnce(measurement.getID());

            String out = SyncMeasurement.sendMeasurement(measurement, person, syncedOnce, getContext().getApplicationContext());

            if (measurement.getID().equals(out)) {
                measurement.setSynced(true);
                MeasurementManager.getInstance(getContext().getApplicationContext())
                        .addMeasurement(measurement);
                MeasurementManager.getInstance(getContext().getApplicationContext())
                        .setSyncedOnce(measurement.getID());
            } else {
                break;
            }
            Log.d(TAG, out);
        }

        if (syncOK) {
            UserManager.getInstance(getContext().getApplicationContext()).setLastSync(new Date().getTime() + 120000);
        }

    }
}
