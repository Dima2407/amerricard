package com.devtonix.amerricard.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.callback.GetContactBirthdayCallback;
import com.devtonix.amerricard.utils.RegexDateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class ContactRepository {

    private static final String TAG = ContactRepository.class.getSimpleName();
    private static final String ZERO_DATE = "01.01.1970";

    @Inject
    SharedHelper sharedHelper;

    private Context context;
    private Handler handler;

    public ContactRepository(Context context) {
        ACApplication.getMainComponent().inject(this);
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
    }

    public void getContactsWithBirthday(final ContentResolver contentResolver, final GetContactBirthdayCallback callback) {

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                final long startQuery = System.currentTimeMillis();
                final List<Contact> contactsAndBirthdays = new ArrayList<>();
                final Uri uri = ContactsContract.Data.CONTENT_URI;

                final String[] projection = new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Event.PHOTO_URI,
                        ContactsContract.CommonDataKinds.Event.START_DATE
                };

                final String where = ContactsContract.Data.MIMETYPE + "= ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;

                final String[] selection = new String[]{ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};

                final String order = null;

                Cursor cursor = contentResolver.query(uri, projection, where, selection, order);

                final int birthdayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                final int contactNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int photoUriColumn = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);

                while (cursor.moveToNext()) {
                    Log.d(TAG, " getContactsWithBirthday name = " + cursor.getString(contactNameColumn));
                    String dateStr = cursor.getString(birthdayColumn);
                    final long dateInMillis = RegexDateUtils.verifyDateFormat(dateStr);
                    String formatterBirthday = RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT.format(new Date(dateInMillis));
                    if (TextUtils.equals(formatterBirthday, ZERO_DATE)) {
                        formatterBirthday = parseDate(dateStr);
                        if (TextUtils.equals(formatterBirthday, ZERO_DATE)) {
                            continue;
                        }
                    }
                    final Contact contact = new Contact();
                    contact.setName(cursor.getString(contactNameColumn));
                    contact.setPhotoUri(cursor.getString(photoUriColumn));
                    contact.setBirthday(formatterBirthday);

                    contactsAndBirthdays.add(contact);

                    Log.d(TAG, "getContactsWithBirthday: dateInMillis = " + dateInMillis + " date = " + formatterBirthday);
                }

                cursor.close();

                Log.d(TAG, "getContactsWithBirthday: selection time = " + (System.currentTimeMillis() - startQuery) + " ms");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(contactsAndBirthdays);

                    }
                });

                saveContactsToStorage(contactsAndBirthdays);
            }
        });
    }

    private String parseDate(String date) {
        String month;
        if (TextUtils.isEmpty(date)) {
            return ZERO_DATE;
        }
        String[] dates = date.split("[ -]");
        if (dates.length < 3) {
            return ZERO_DATE;
        }
        String dayStr = dates[0];
        String monthStr = dates[1];
        String yearStr = dates[2];

        if (monthStr.contains("янв")) {
            month = "01";
        } else if (monthStr.contains("фев")) {
            month = "02";
        } else if (monthStr.contains("мар")) {
            month = "03";
        } else if (monthStr.contains("апр")) {
            month = "04";
        } else if (monthStr.contains("май") || monthStr.contains("мая")) {
            month = "05";
        } else if (monthStr.contains("июн")) {
            month = "06";
        } else if (monthStr.contains("июл")) {
            month = "07";
        } else if (monthStr.contains("авг")) {
            month = "08";
        } else if (monthStr.contains("сен")) {
            month = "09";
        } else if (monthStr.contains("окт")) {
            month = "10";
        } else if (monthStr.contains("ноя")) {
            month = "11";
        } else if (monthStr.contains("дек")) {
            month = "12";
        } else {
            return ZERO_DATE;
        }

        try {
            if (Integer.valueOf(dayStr) < 1 || Integer.valueOf(dayStr) > 31) {
                return ZERO_DATE;
            } else if (yearStr.length() == 4) {
                int year = Integer.valueOf(yearStr);
            } else {
                return ZERO_DATE;
            }
        } catch (NumberFormatException e) {
            return ZERO_DATE;
        }

        return dayStr.concat(".").concat(month).concat(".").concat(yearStr);
    }

    public void saveContactsToStorage(final List<Contact> contacts) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sharedHelper.saveContacts(contacts);
            }
        }).start();

    }

    public List<Contact> getContactsFromStorage() {
        return sharedHelper.getContacts();
    }
}
