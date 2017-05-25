package com.devtonix.amerricard.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.ui.activity.MainActivity;
import com.devtonix.amerricard.utils.Preferences;

import java.util.List;

public class HolidaysNotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent mainIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

        final List<Item> events = Preferences.getInstance().getEvents();

        String notificationMessage = events.get(events.size() - 1).name;

        Notification mNotify = new Builder(this)
                .setContentTitle(getString(R.string.notifications_title))
                .setContentText(notificationMessage + " is celebrated today!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, mNotify);

        stopSelf();
    }

}
