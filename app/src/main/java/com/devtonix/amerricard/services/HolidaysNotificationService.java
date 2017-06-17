package com.devtonix.amerricard.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.repository.EventRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.activity.MainActivity;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.devtonix.amerricard.utils.RegexDateUtils;
import com.devtonix.amerricard.utils.TimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class HolidaysNotificationService extends Service {

    private static final String TAG = HolidaysNotificationService.class.getSimpleName();
    @Inject
    EventRepository eventRepository;
    @Inject
    SharedHelper sharedHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        ACApplication.getMainComponent().inject(this);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent mainIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        final PendingIntent pIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
        final List<EventItem> events = eventRepository.getEventFromStorage();
        final Calendar currentCalendar = Calendar.getInstance();
        final String currentDate = TimeUtils.calDateToString(currentCalendar);

        final List<EventItem> itemsForDisplay = new ArrayList<>();

        for (EventItem item : events) {
            if (TextUtils.equals(item.getFormattedDate(), currentDate)) {
                itemsForDisplay.add(item);
            }
        }

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Today celebrate:");

        for (EventItem item : itemsForDisplay) {
            inboxStyle.addLine(
                    LanguageUtils.convertLang(item.getName(),
                            sharedHelper.getLanguage()) + " (" + item.getFormattedDate() + ")");
        }

        final List<Contact> contacts = sharedHelper.getContacts();
        final List<Contact> contactsForDisplay = new ArrayList<>();
        for (Contact c : contacts) {
            try {
                Log.d(TAG, "onCreate: contact = " + c.getName());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT.parse(c.getBirthday()));

                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int month = calendar.get(Calendar.MONTH);

                final int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
                final int currentMonth = currentCalendar.get(Calendar.MONTH);

                Log.d(TAG, "onCreate: contact = " + c.getName() + " day=" + day + " month=" + month);

                if (day == currentDay & month == currentMonth) {
                    contactsForDisplay.add(c);
                    Log.d(TAG, "onCreate: contactsForDisplay.add(c) ");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (Contact c : contactsForDisplay) {
            inboxStyle.addLine(c.getName() + " (" + c.getBirthday() + ")");
        }

        final String forDisplay = itemsForDisplay.size() == 0
                ? contacts.get(0).getName() + " happy birthday!"
                : LanguageUtils.convertLang(itemsForDisplay.get(0).getName(), sharedHelper.getLanguage()) + "is celebrated today";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentText(forDisplay)
                .setContentTitle(getString(R.string.notifications_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        builder.setStyle(inboxStyle);

        notificationManager.notify(1, builder.build());

        stopSelf();
    }
}
