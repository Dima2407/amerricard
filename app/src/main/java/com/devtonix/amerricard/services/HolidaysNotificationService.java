package com.devtonix.amerricard.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.Celebrity;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.repository.CelebrityRepository;
import com.devtonix.amerricard.repository.ContactRepository;
import com.devtonix.amerricard.repository.EventRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.activity.MainActivity;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.devtonix.amerricard.utils.RegexDateUtils;
import com.devtonix.amerricard.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class HolidaysNotificationService extends Service {

    private static final String TAG = HolidaysNotificationService.class.getSimpleName();
    @Inject
    EventRepository eventRepository;
    @Inject
    ContactRepository contactRepository;
    @Inject
    CelebrityRepository celebrityRepository;
    @Inject
    SharedHelper sharedHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate");
        ACApplication.getMainComponent().inject(this);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent mainIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("position", 1);
        final PendingIntent pIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

        Date today = new Date();
        SimpleDateFormat dateFormat = RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT;

        final List<EventItem> eventsForDisplay = getTodayEvents(today, dateFormat);

        final List<Celebrity> celebrities = celebrityRepository.getCelebritiesFromStorage();
        final List<Celebrity> celebritiesForDisplay = getTodayCelebrities(today, dateFormat);
        final List<Contact> contactsForDisplay = getTodayContacts(today, dateFormat);

        if (eventsForDisplay.size() > 0 || contactsForDisplay.size() > 0 || celebritiesForDisplay.size() > 0) {

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Today celebrate:");

            for (Contact c : contactsForDisplay) {
                inboxStyle.addLine(c.getName() + " (" + c.getBirthday() + ")");
            }

            if (sharedHelper.getCelebritiesInSettings()) {
                for (Celebrity c : celebritiesForDisplay) {
                    inboxStyle.addLine(c.getName() + " (" + c.getFormattedDate() + ")");
                }
            }

            for (EventItem item : eventsForDisplay) {
                inboxStyle.addLine(
                        LanguageUtils.convertLang(item.getName(),
                                sharedHelper.getLanguage()) + " (" + item.getFormattedDate() + ")");
            }

            StringBuilder forDisplay = new StringBuilder();

            if (contactsForDisplay.size() != 0) {
                forDisplay.append(contactsForDisplay.get(0).getName()).append(" happy birthday!");
            } else if (eventsForDisplay.size() != 0) {
                forDisplay.append(LanguageUtils.convertLang(eventsForDisplay.get(0).getName(), sharedHelper.getLanguage())).append(" is celebrated today");
            } else if (celebritiesForDisplay.size() != 0 && sharedHelper.getCelebritiesInSettings()) {
                forDisplay.append(celebrities.get(0).getName()).append(" happy birthday!");
            }

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentText(forDisplay)
                    .setContentTitle(getString(R.string.notifications_title))
                    .setSmallIcon(R.drawable.ic_logo_cropped_2)
                    .setContentIntent(pIntent)
                    .setSound(alarmSound)
                    .setAutoCancel(true);

            builder.setStyle(inboxStyle);

            notificationManager.notify(1, builder.build());
        }

        stopSelf();
    }

    @NonNull
    private List<EventItem> getTodayEvents(Date today, SimpleDateFormat dateFormat) {
        final List<EventItem> events = eventRepository.getEventFromStorage();
        final List<EventItem> eventsForDisplay = new ArrayList<>();
        for (EventItem item : events) {
            try {

                Date bDay = dateFormat.parse(item.getFormattedDate());
                if (TimeUtils.isSameDay(bDay, today)) {
                    eventsForDisplay.add(item);
                    Log.d(TAG, "onCreate: eventForDisplay.add(" + item.getName() + ")");
                }
            } catch (ParseException e) {
                Log.e(TAG, "getTodayEvents: ", e );
            }
        }
        return eventsForDisplay;
    }

    @NonNull
    private List<Celebrity> getTodayCelebrities(Date today, SimpleDateFormat dateFormat){
        final List<Celebrity> celebrities = celebrityRepository.getCelebritiesFromStorage();
        final List<Celebrity> celebritiesForDisplay = new ArrayList<>();
        for (Celebrity c : celebrities) {
            try {
                Log.d(TAG, "onCreate: celebrity = " + c.getName());

                Date bDay = dateFormat.parse(c.getFormattedDate());

                if (TimeUtils.isSameDay(bDay, today)) {
                    celebritiesForDisplay.add(c);
                    Log.d(TAG, "onCreate: celebritiesForDisplay.add(" + c.getName() + ") ");
                }
            } catch (ParseException e) {
                Log.e(TAG, "getTodayCelebrities: ", e);
            }
        }
        return celebritiesForDisplay;
    }

    @NonNull
    private List<Contact> getTodayContacts(Date today, SimpleDateFormat dateFormat){
        final List<Contact> contacts = contactRepository.getContactsFromStorage();
        final List<Contact> contactsForDisplay = new ArrayList<>();
        for (Contact c : contacts) {
            try {
                Log.d(TAG, "onCreate: contact = " + c.getName());
                Date bDay = dateFormat.parse(c.getBirthday());


                if (TimeUtils.isSameDay(bDay, today) && !sharedHelper.getContactsForHide().contains(c)) {
                    contactsForDisplay.add(c);
                    Log.d(TAG, "onCreate: contactsForDisplay.add(" + c.getName() + ") ");
                }
            } catch (ParseException e) {
                Log.e(TAG, "getTodayContacts: ", e);
            }
        }
        return contactsForDisplay;
    }
}
