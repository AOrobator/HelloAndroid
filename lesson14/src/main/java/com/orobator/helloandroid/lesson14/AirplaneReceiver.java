package com.orobator.helloandroid.lesson14;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AirplaneReceiver extends BroadcastReceiver {
    private static final String TAG = "AirplaneReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("state")) {
            boolean state = intent.getBooleanExtra("state", false);
            Log.i(TAG, "Airplane Mode: " + state);
        }
        Log.i(TAG, "Airplane Mode");
    }
}
