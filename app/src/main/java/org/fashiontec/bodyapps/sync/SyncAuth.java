package org.fashiontec.bodyapps.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.Authenticator;

public class SyncAuth extends Service {
    private Auth authenticator;
    @Override
    public void onCreate() {
        authenticator = new Auth(this);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
