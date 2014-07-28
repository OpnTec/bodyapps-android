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

/**
 * Manages the automatic sync of measurements with web API
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;


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
        Log.d("SyncAdapter", "sync happened");
        Measurement measurement;
        while ((measurement = MeasurementManager.getInstance(
                getContext().getApplicationContext()).getMeasurementSync()) != null) {

            Person person = PersonManager.getInstance(getContext().getApplicationContext())
                    .getPersonbyID(measurement.getPersonID());

            String out = SyncMeasurement.sendMeasurement(measurement, person);

            if (out.equals(measurement.getID())) {
                measurement.setSynced(true);
                MeasurementManager.getInstance(getContext().getApplicationContext())
                        .addMeasurement(measurement);
            }
            Log.d("SyncAdapter", out);
        }

        String list[] = SyncMeasurement.getSyncList(UserManager.getInstance(
                getContext().getApplicationContext()).getLastSync());
        boolean syncOK = true;

        if (list!=null) {
            String userID = UserManager.getInstance(getContext().getApplicationContext()).getCurrent();
            for (int i = 0; i < list.length; i++) {
                String out = SyncMeasurement.getMeasurement(list[i],
                        getContext().getApplicationContext(), userID);
                if (out.equals(list[i])) {
                    SyncPic.getPic(list[i], getContext().getApplicationContext(), 1);
                    SyncPic.getPic(list[i], getContext().getApplicationContext(), 2);
                    SyncPic.getPic(list[i], getContext().getApplicationContext(), 3);
                } else {
                    syncOK = false;
                    break;
                }
            }
        }

        if (syncOK) {
            UserManager.getInstance(getContext().getApplicationContext()).setLastSync(new Date().getTime());
            Log.d("SyncAdapter", "syncOK");
        }

    }
}
