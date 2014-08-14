/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.managers.test;

import android.content.Context;
import android.test.AndroidTestCase;

import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.fashiontec.bodyapps.models.Measurement;

public class MeasurementManagerTest extends AndroidTestCase {
    Context context;
    Measurement measurement;
    MeasurementManager mm;

    @Override
    public void setUp() throws Exception {
        context = getContext().getApplicationContext();
        measurement = new Measurement("test", "test2", 1, 1);
        mm = MeasurementManager.getInstance(context);
    }

    public void testAddMeasurement() {
        mm.addMeasurement(measurement);
        assertNotNull("measurement not added to db", mm.getMeasurement("test"));
    }

    public void testDelMeasurement() throws Exception {
        mm.delMeasurement("test", 2);
        assertNull("measurement not deleted from db", mm.getMeasurement("test"));
    }
}
