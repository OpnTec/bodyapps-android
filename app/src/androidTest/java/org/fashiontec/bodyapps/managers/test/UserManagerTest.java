/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package org.fashiontec.bodyapps.managers.test;

import android.content.Context;
import android.test.AndroidTestCase;

import org.fashiontec.bodyapps.managers.UserManager;
import org.fashiontec.bodyapps.models.User;

public class UserManagerTest extends AndroidTestCase {
    Context context;
    UserManager um;
    User user;

    public void setUp(){
        context = getContext().getApplicationContext();
        um=UserManager.getInstance(context);
        assertNotNull("User Manger null",um);
        user=new User("test_email","test_name","test_id",true);

    }

    public void testAddUser() throws Exception {
        um.addUser(user);
        assertNotNull(um.isUser(user));
    }

    public void testUnsetCurrent() throws Exception {
        um.unsetCurrent();
        assertNull(um.getCurrent());

    }


    public void testSetCurrent() throws Exception {
        um.setCurrent(user);
        assertNotNull(um.getCurrent());
    }
}
