package com.example.galfo.prueba_combos;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by galfo on 12/1/2017.
 */

public class BroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarma corriendo de nuevo luego de 10 segs", Toast.LENGTH_SHORT).show();

    }
}
