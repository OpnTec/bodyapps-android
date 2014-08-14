/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

public class HDF extends Sync {

    private static String result;

    /**
     * Gets HDF file from server
     *
     * @param userID
     * @param measurementID
     * @param path
     * @return
     */
    public int getHDF(String userID, String measurementID, String path) {
        String URL = baseURL + "/users/" + userID + "/measurements/" + measurementID;
        HDF hdf = new HDF();
        return hdf.download(URL, path);
    }
}
