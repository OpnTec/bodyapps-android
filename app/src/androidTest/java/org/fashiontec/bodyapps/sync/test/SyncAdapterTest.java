/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package org.fashiontec.bodyapps.sync.test;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.test.AndroidTestCase;

import junit.framework.Assert;

import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.fashiontec.bodyapps.managers.PersonManager;
import org.fashiontec.bodyapps.models.Measurement;
import org.fashiontec.bodyapps.models.Person;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SyncAdapterTest extends AndroidTestCase {
    Context context;
    public static final String AUTHORITY = "org.fashiontec.bodyapps.sync.provider";
    public static final String ACCOUNT_TYPE = "fashiontec.org";
    public static final String ACCOUNT = "dummyaccount";

    @Override
    public void setUp() throws Exception {
        context = getContext().getApplicationContext();

        Person person=new Person("test_mail","test_name",1);
        PersonManager.getInstance(context).addPerson(person);
        Measurement measurement=new Measurement("test","test2",PersonManager.getInstance(context).getPerson(person),1);
        MeasurementManager.getInstance(context).addMeasurement(measurement);

    }

    public void testSync() throws InterruptedException {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(newAccount, null, null);
        ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
        ContentResolver.requestSync(newAccount, "org.fashiontec.bodyapps.sync.provider", Bundle.EMPTY);
        final CountDownLatch signal = new CountDownLatch(1);
        signal.await(30, TimeUnit.SECONDS);
        assertNull(MeasurementManager.getInstance(context).getMeasurementSync());
    }

}
