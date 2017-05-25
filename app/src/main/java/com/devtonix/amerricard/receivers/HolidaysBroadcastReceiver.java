package com.devtonix.amerricard.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.devtonix.amerricard.services.HolidaysNotificationService;
import com.devtonix.amerricard.utils.Preferences;

import java.util.Calendar;

public class HolidaysBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = HolidaysBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final boolean isNotificationEnabled = Preferences.getInstance().getNotification();
        final Intent notificationIntent = new Intent(context, HolidaysNotificationService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 1234, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

        if (isNotificationEnabled) {

            final String timeForNotification = Preferences.getInstance().getNotificationsTime();
            final Calendar triggeredCalendar = getNotificationTriggeredCalendar(timeForNotification);
            final Calendar currentCalendar = Calendar.getInstance();
            long intendedTime = triggeredCalendar.getTimeInMillis();
            final long currentTime = currentCalendar.getTimeInMillis();

            if (intendedTime >= currentTime) {
                setNotificationAlarm(pendingIntent, alarmManager, intendedTime);
            } else {
                triggeredCalendar.add(Calendar.DAY_OF_MONTH, 1);
                intendedTime = triggeredCalendar.getTimeInMillis();
                setNotificationAlarm(pendingIntent, alarmManager, intendedTime);
            }
        }
    }

    private Calendar getNotificationTriggeredCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, getMinute(time));
        calendar.set(Calendar.HOUR_OF_DAY, getHour(time));
        return calendar;
    }

    private void setNotificationAlarm(PendingIntent pendingIntent, AlarmManager alarmManager, long intendedTime) {

        Log.d(TAG, "Setting intended time to " + intendedTime);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                intendedTime,
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    public static int getHour(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[0]);
    }

    public static int getMinute(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[1]);
    }
}
