package com.minhagasosa;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by silva on 16/05/2016.
 */
public class MyReceiver extends BroadcastReceiver {
    int MID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Minha gasosa dicas").setContentText("dica de hoje é bla... bla, bla...");

        notificationManager.notify(MID, builder.build());
        MID++;
    }
}
