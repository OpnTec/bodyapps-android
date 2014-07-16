package org.fashiontec.bodyapps.sync;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class HDF extends Sync{
    private static String URL = "http://192.168.1.2:8020/users/";
    private static String result;

    public static int getHDF(String userID, String measurementID, String name){
        String HDF_DIRECTORY_NAME = "BodyApp" + File.separator + name;
        URL=URL+userID+"/measurements/"+measurementID;
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                HDF_DIRECTORY_NAME
        );

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(HDF_DIRECTORY_NAME, "Oops! Failed create "
                        + HDF_DIRECTORY_NAME + " directory");
                return -1;
            }
        }

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + measurementID+".rar");

        HDF hdf = new HDF();
        return hdf.GET(URL,mediaFile.getPath());
    }
}
