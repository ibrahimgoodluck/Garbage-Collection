package com.team.green;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Notification extends Service {
    public Notification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
