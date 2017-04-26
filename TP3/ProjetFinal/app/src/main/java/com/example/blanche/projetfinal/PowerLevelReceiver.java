package com.example.blanche.projetfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;

public class PowerLevelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BATTERY_LOW)) {
            Toast.makeText(context, "LOW", Toast.LENGTH_SHORT);
        }
        else if (intent.getAction().equals(ACTION_BATTERY_OKAY))
            Toast.makeText(context, "OKAY", Toast.LENGTH_SHORT);
        else
            Toast.makeText(context, "AUCUN", Toast.LENGTH_LONG);
    }
}
