package com.example.blanche.orgevents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Blanche on 3/23/2017.
 */

public class BatteryLevelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferencesActivity.setFrequency(60);
    }
}
