/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.managers.test;

import android.content.Context;
import android.test.AndroidTestCase;

import org.fashiontec.bodyapps.managers.PersonManager;
import org.fashiontec.bodyapps.models.Person;

public class PersonManagerTest extends AndroidTestCase {
    Context context;
    Person person;
    PersonManager pm;

    public void setUp() throws Exception {
        context = getContext().getApplicationContext();
        pm=PersonManager.getInstance(context);
        assertNotNull("Person Manager null",pm);
        person=new Person("test_mail","test_name",1, 1407640921L);
    }

    public void testAddPerson() throws Exception {
        pm.addPerson(person);
        assertNotNull(pm.getPerson(person));
    }

    public int testGetPersonByID() throws Exception {
        int id=pm.getPerson(person);
        assertFalse("No person",id==-1);
        Person p=pm.getPersonbyID(id);
        assertTrue(person.getEmail().equals(p.getEmail()));
        return id;
    }

}
