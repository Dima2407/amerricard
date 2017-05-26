package com.devtonix.amerricard.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.ui.activity.MainActivity;
import com.devtonix.amerricard.utils.Preferences;
import com.devtonix.amerricard.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HolidaysNotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent mainIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        final PendingIntent pIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
        final List<Item> events = Preferences.getInstance().getEvents();
        final Calendar currentCalendar = Calendar.getInstance();
        final String currentDate = TimeUtils.calDateToString(currentCalendar);

        final List<Item> itemsForDisplay = new ArrayList<>();

        for (Item item : events) {
            if (TextUtils.equals(item.getDate(), currentDate)) {
                itemsForDisplay.add(item);
            }
        }

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Today celebrate:");

        for (Item item : itemsForDisplay) {
            inboxStyle.addLine(item.name + " (" + item.getDate() + ")");
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentText(itemsForDisplay.get(0).name + " is celebrated today!")
                .setContentTitle(getString(R.string.notifications_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                ;

        builder.setStyle(inboxStyle);

        notificationManager.notify(1, builder.build());

        stopSelf();
    }
}
