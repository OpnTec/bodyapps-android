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
    public static int getHDF(String userID, String measurementID, String path) {
        String URL = "http://192.168.1.2:8020/users/";
        URL = URL + userID + "/measurements/" + measurementID;
        System.out.println("URL = " + URL);
        HDF hdf = new HDF();
        return hdf.download(URL, path);
    }
}
