package com.devtonix.amerricard.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.Celebrity;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.repository.CelebrityRepository;
import com.devtonix.amerricard.repository.ContactRepository;
import com.devtonix.amerricard.repository.EventRepository;
import com.devtonix.amerricard.services.HolidaysNotificationService;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.utils.RegexDateUtils;
import com.devtonix.amerricard.utils.TimeUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class HolidaysBroadcastReceiver extends BroadcastReceiver {

    @Inject
    EventRepository eventRepository;
    @Inject
    CelebrityRepository celebrityRepository;
    @Inject
    ContactRepository contactRepository;
    @Inject
    SharedHelper sharedHelper;

    private static final String TAG = HolidaysBroadcastReceiver.class.getSimpleName();

    public HolidaysBroadcastReceiver() {
        ACApplication.getMainComponent().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final boolean isNotificationEnabled = sharedHelper.getNotification();
        final Intent notificationIntent = new Intent(context, HolidaysNotificationService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 1234, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Calendar currentCalendar = Calendar.getInstance();

//        boolean isShowNeeded = false;

//        final List<EventItem> events = eventRepository.getEventFromStorage();
//        final String currentDate = TimeUtils.calDateToString(currentCalendar);
//
//        for (EventItem item : events) {
//            if (TextUtils.equals(item.getFormattedDate(), currentDate)) {
//                Log.d(TAG, "(event) isShowNeeded = true for " + item.getName());
//                isShowNeeded = true;
//                break;
//            }
//        }
//
//        final List<Celebrity> celebrities = celebrityRepository.getCelebritiesFromStorage();
//        for (Celebrity c : celebrities) {
//            try {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT.parse(c.getFormattedDate()));
//
//                final int day = calendar.get(Calendar.DAY_OF_MONTH);
//                final int month = calendar.get(Calendar.MONTH);
//
//                final int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
//                final int currentMonth = currentCalendar.get(Calendar.MONTH);
//
//                if (day == currentDay & month == currentMonth) {
//                    Log.d(TAG, "(celebrity) isShowNeeded = true for " + c.getName());
//                    isShowNeeded = true;
//                    break;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        final List<Contact> contacts = contactRepository.getContactsFromStorage();
//        for (Contact c : contacts) {
//            try {
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
//                    Log.d(TAG, "(contact) isShowNeeded = true for " + c.getName());
//                    isShowNeeded = true;
//                    break;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

        alarmManager.cancel(pendingIntent);

        if (isNotificationEnabled) {

            final String timeForNotification = sharedHelper.getNotificationsTime();
            final Calendar triggeredCalendar = getNotificationTriggeredCalendar(timeForNotification);
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
        calendar.set(Calendar.MINUTE, TimeUtils.getMinute(time));
        calendar.set(Calendar.HOUR_OF_DAY, TimeUtils.getHour(time));
        return calendar;
    }

    private void setNotificationAlarm(PendingIntent pendingIntent, AlarmManager alarmManager, long intendedTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(intendedTime);
        Log.e(TAG, "Setting intended time to " + TimeUtils.calDateToString(calendar));
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                intendedTime,
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }
}
