package com.minhagasosa;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * Created by elyervesson on 22/05/17.
 */

public class MyNotificationManager {

    private Context context;

    public static final int NOTIFICATION_ID = 234;

    public MyNotificationManager(Context context) {
        this.context = context;
    }

    public void showNotification(String from, String notification, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notificationFCM = builder
                .setSmallIcon(R.mipmap.ic_gas_station_black_24dp)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(from)
                .setContentText(notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_gas_station_black_24dp))
                .build();

        notificationFCM.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationFCM);
    }
}
