package com.devtonix.amerricard.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HolidaysNotificationService extends Service {

    private static final String TAG = HolidaysNotificationService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
//        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        final Intent mainIntent = new Intent(this.getApplicationContext(), MainActivity.class);
//        final PendingIntent pIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
//        final List<Item> events = SharedHelper.getInstance().getEvents();
//        final Calendar currentCalendar = Calendar.getInstance();
//        final String currentDate = TimeUtils.calDateToString(currentCalendar);
//
//        final List<Item> itemsForDisplay = new ArrayList<>();
//
//        for (Item item : events) {
//            if (TextUtils.equals(item.getDate(), currentDate)) {
//                itemsForDisplay.add(item);
//            }
//        }
//
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.setBigContentTitle("Today celebrate:");
//
//        for (Item item : itemsForDisplay) {
//            inboxStyle.addLine(item.getNameJsonEl() + " (" + item.getDate() + ")");
//        }
//
//        final List<Contact> contacts = SharedHelper.getInstance().getContacts();
//        final List<Contact> contactsForDisplay = new ArrayList<>();
//        for (Contact c : contacts) {
//            try {
//                Log.d(TAG, "onCreate: contact = " + c.getNameJsonEl());
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT.parse(c.getBirthday()));
//
//                final int day = calendar.get(Calendar.DAY_OF_MONTH);
//                final int month = calendar.get(Calendar.MONTH);
//
//                final int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
//                final int currentMonth = currentCalendar.get(Calendar.MONTH);
//
//                Log.d(TAG, "onCreate: contact = " + c.getNameJsonEl() + " day=" + day + " month=" + month);
//
//                if (day == currentDay & month == currentMonth) {
//                    contactsForDisplay.add(c);
//                    Log.d(TAG, "onCreate: contactsForDisplay.add(c) ");
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (Contact c : contactsForDisplay) {
//            inboxStyle.addLine(c.getNameJsonEl() + " (" + c.getBirthday() + ")");
//        }
//
//        final String forDisplay = itemsForDisplay.size()==0
//                ? contacts.get(0).getNameJsonEl() + " happy birthday!"
//                : itemsForDisplay.get(0).getNameJsonEl() + "is celebrated today";
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setContentText(forDisplay)
//                .setContentTitle(getString(R.string.notifications_title))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pIntent)
//                .setAutoCancel(true);
//
//        builder.setStyle(inboxStyle);
//
//        notificationManager.notify(1, builder.build());
//
//        stopSelf();
    }
}
