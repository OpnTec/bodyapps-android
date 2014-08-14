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
    MeasurementManager measurementMgr;
    PersonManager personMgr;
    UserManager userMgr;

    static final String TAG = SyncAdapter.class.getName();

    public SyncAdapter(Context context, boolean autoInitialize) {
        this(context, autoInitialize, false);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();

        measurementMgr = MeasurementManager.getInstance(context.getApplicationContext());
        personMgr = PersonManager.getInstance(context.getApplicationContext());
        userMgr = UserManager.getInstance(context.getApplicationContext());
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle bundle,
                              String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {

        Log.d(TAG, "sync happened");

        SyncMeasurement sync = new SyncMeasurement();
        String userID = userMgr.getCurrent();
        Context appContext = getContext().getApplicationContext();

        //Gets a list of measurements deleted from server side and deletes them from device too
        String[] delListServer = sync.getDelList(userID);

        if (delListServer != null) {
            for (String aDelListServer : delListServer) {
                int personID = measurementMgr.getMeasurement(aDelListServer).getPersonID();
                measurementMgr.delMeasurement(aDelListServer, personID);
            }
        }

        //Gets a list of measurements deleted from device and sent it to server
        List<String> delList = measurementMgr.getDelList();

        for (String ID : delList) {
            Log.d(TAG, ID);
            boolean out = sync.delMeasurement(ID, userID);
            if (out) {
                measurementMgr.removeDelEntry(ID);
            }
        }

        //Gets a list of measurements updated from server side since last sync and
        // get them to device.
        String list[] = sync.getSyncList(userMgr.getLastSync(), userID);
        boolean syncOK = true;

        if (list != null) {
            for (String aList : list) {
                String out = sync.getMeasurement(aList, appContext, userID);
                if (!out.equals(aList)) {
                    syncOK = false;
                    break;
                }
            }
        }

        Measurement measurement;
        Person person;

        //sends all the changed measurements to server
        while ((measurement = measurementMgr.getMeasurementSync()) != null) {

            person = personMgr.getPersonbyID(measurement.getPersonID());
            boolean syncedOnce = measurementMgr.isSyncedOnce(measurement.getID());

            String out = sync.sendMeasurement(measurement, person, syncedOnce,
                    getContext().getApplicationContext());

            if (measurement.getID().equals(out)) {
                measurement.setSynced(true);
                measurementMgr.addMeasurement(measurement);
                measurementMgr.setSyncedOnce(measurement.getID());
            } else {
                break;
            }
            Log.d(TAG, out);
        }

        if (syncOK) {
            userMgr.setLastSync(new Date().getTime() + 120000);
        }
    }
}
