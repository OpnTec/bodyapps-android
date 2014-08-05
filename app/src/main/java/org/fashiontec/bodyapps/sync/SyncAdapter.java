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

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

        String userID = UserManager.getInstance(getContext().getApplicationContext()).getCurrent();
        String list[] = SyncMeasurement.getSyncList(UserManager.getInstance(
                getContext().getApplicationContext()).getLastSync(), userID);
        boolean syncOK = true;

        if (list!=null) {

            for (int i = 0; i < list.length; i++) {
                String out = SyncMeasurement.getMeasurement(list[i],
                        getContext().getApplicationContext(), userID);
                if (out.equals(list[i])) {
                    Log.d("SyncAdapter", "get happened");
//                    SyncPic.getPic(list[i], getContext().getApplicationContext(), 1);
//                    SyncPic.getPic(list[i], getContext().getApplicationContext(), 2);
//                    SyncPic.getPic(list[i], getContext().getApplicationContext(), 3);
                } else {
                    syncOK = false;
                    break;
                }
            }
        }

        while ((measurement = MeasurementManager.getInstance(
                getContext().getApplicationContext()).getMeasurementSync()) != null) {

            Person person = PersonManager.getInstance(getContext().getApplicationContext())
                    .getPersonbyID(measurement.getPersonID());
            boolean syncedOnce= MeasurementManager.getInstance(getContext().getApplicationContext())
                    .isSyncedOnce(measurement.getID());

            String out = SyncMeasurement.sendMeasurement(measurement, person, syncedOnce, getContext().getApplicationContext());

            if (measurement.getID().equals(out)) {
                measurement.setSynced(true);
                MeasurementManager.getInstance(getContext().getApplicationContext())
                        .addMeasurement(measurement);
                MeasurementManager.getInstance(getContext().getApplicationContext())
                        .setSyncedOnce(measurement.getID());
            }else {
                break;
            }
            Log.d("SyncAdapter", out +"note");
        }

        if (syncOK) {
            DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("gmt"));
            String gmtTime = df.format(new Date());
            UserManager.getInstance(getContext().getApplicationContext()).setLastSync(new Date().getTime()+120000);
            Log.d("SyncAdapter", gmtTime);
        }

    }
}
