/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package fossasia.valentina.bodyapp.managers.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.view.KeyEvent;

import fossasia.valentina.bodyapp.db.DatabaseHandler;
import fossasia.valentina.bodyapp.main.MainActivity;
import fossasia.valentina.bodyapp.managers.UserManager;
import fossasia.valentina.bodyapp.models.User;

public class UserManagerTest extends AndroidTestCase {
    Context context;
    UserManager um;
    User user;

    public void setUp(){
        context = getContext().getApplicationContext();
        um=UserManager.getInstance(context);
        assertNotNull("User Manger null",um);
        user=new User("test_email","test_name","test_id");

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
