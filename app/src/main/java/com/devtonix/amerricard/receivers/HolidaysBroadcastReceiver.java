package com.devtonix.amerricard.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.services.HolidaysNotificationService;
import com.devtonix.amerricard.utils.SharedHelper;
import com.devtonix.amerricard.utils.RegexDateUtils;
import com.devtonix.amerricard.utils.TimeUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class HolidaysBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = HolidaysBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        final boolean isNotificationEnabled = SharedHelper.getInstance().getNotification();
//        final Intent notificationIntent = new Intent(context, HolidaysNotificationService.class);
//        final PendingIntent pendingIntent = PendingIntent.getService(context, 1234, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        final Calendar currentCalendar = Calendar.getInstance();
//
//        boolean isShowNeeded = false;
//
//        final List<Item> events = SharedHelper.getInstance().getEvents();
//        final String currentDate = TimeUtils.calDateToString(currentCalendar);
//
//        for (Item item : events) {
//            if (TextUtils.equals(item.getDate(), currentDate)) {
//                isShowNeeded = true;
//                break;
//            }
//        }
//
//        final List<Contact> contacts = SharedHelper.getInstance().getContacts();
//
//        for (Contact c : contacts) {
//            try {
//                Log.d(TAG, "onCreate: contact = " + c.getName());
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT.parse(c.getBirthday()));
//
//                final int day = calendar.get(Calendar.DAY_OF_MONTH);
//                final int month = calendar.get(Calendar.MONTH);
//
//                final int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
//                final int currentMonth = currentCalendar.get(Calendar.MONTH);
//
//                if (day == currentDay & month == currentMonth) {
//                    isShowNeeded = true;
//                    break;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        alarmManager.cancel(pendingIntent);
//
//        if (isNotificationEnabled & isShowNeeded) {
//
//            final String timeForNotification = SharedHelper.getInstance().getNotificationsTime();
//            final Calendar triggeredCalendar = getNotificationTriggeredCalendar(timeForNotification);
//            long intendedTime = triggeredCalendar.getTimeInMillis();
//            final long currentTime = currentCalendar.getTimeInMillis();
//
//            if (intendedTime >= currentTime) {
//                setNotificationAlarm(pendingIntent, alarmManager, intendedTime);
//            } else {
//                triggeredCalendar.add(Calendar.DAY_OF_MONTH, 1);
//                intendedTime = triggeredCalendar.getTimeInMillis();
//                setNotificationAlarm(pendingIntent, alarmManager, intendedTime);
//            }
//        }
    }

    private Calendar getNotificationTriggeredCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, TimeUtils.getMinute(time));
        calendar.set(Calendar.HOUR_OF_DAY, TimeUtils.getHour(time));
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
}
