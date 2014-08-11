/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync.test;

import android.test.AndroidTestCase;

import org.fashiontec.bodyapps.managers.UserManager;
import org.fashiontec.bodyapps.models.User;
import org.fashiontec.bodyapps.sync.SyncUser;

public class SyncUserTest extends AndroidTestCase {
    String userID;
    UserManager um;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        um= UserManager.getInstance(getContext());
    }

    public void testGetUserID() throws Exception {
        SyncUser su=new SyncUser();
        userID=su.getUserID("rand","test_name");
        assertNotNull(userID,userID);

    }
}
