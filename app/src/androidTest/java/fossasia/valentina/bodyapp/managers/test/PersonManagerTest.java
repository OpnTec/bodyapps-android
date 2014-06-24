/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package fossasia.valentina.bodyapp.managers.test;

import android.content.Context;
import android.test.AndroidTestCase;

import fossasia.valentina.bodyapp.managers.PersonManager;
import fossasia.valentina.bodyapp.models.Person;

public class PersonManagerTest extends AndroidTestCase {
    Context context;
    Person person;
    PersonManager pm;

    public void setUp() throws Exception {
        context = getContext().getApplicationContext();
        pm=PersonManager.getInstance(context);
        assertNotNull("Person Manager null",pm);
        person=new Person("test_mail","test_name",1);
    }

    public void testAddPerson() throws Exception {
        pm.addPerson(person);
        assertNotNull(pm.getPerson(person));
    }

    public void testGetPersonByID() throws Exception {
        int id=pm.getPerson(person);
        assertFalse("No person",id==-1);
        Person p=pm.getPersonbyID(id);
        assertTrue(person.getEmail().equals(p.getEmail()));
    }

}
