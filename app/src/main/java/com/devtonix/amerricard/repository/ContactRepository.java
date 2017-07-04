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
                    final long dateInMillis = RegexDateUtils.verifyDateFormat(cursor.getString(birthdayColumn));
                    final String formatterBirthday = RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT.format(new Date(dateInMillis));
                    if (TextUtils.equals(formatterBirthday, "01.01.1970")) {
                        continue;
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


//                List<Contact> oldContacts = new ArrayList<Contact>();
//                oldContacts = getContactsFromStorage();
//                if (oldContacts.size() > 0){
//                    for (Contact contact : oldContacts ){
//                        if (contact.equals())
//                    }
//                }
                saveContactsToStorage(contactsAndBirthdays);
            }
        });
    }

    public void saveContactsToStorage(List<Contact> contacts) {
        sharedHelper.saveContacts(contacts);
    }

    public List<Contact> getContactsFromStorage(){
        return sharedHelper.getContacts();
    }
}
