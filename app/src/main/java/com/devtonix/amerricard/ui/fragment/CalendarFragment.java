package com.devtonix.amerricard.ui.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.repository.EventRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.activity.CreateBirthdayActivity;
import com.devtonix.amerricard.ui.adapter.CalendarAdapter;
import com.devtonix.amerricard.ui.callback.EventGetCallback;
import com.devtonix.amerricard.utils.RegexDateUtils;
import com.devtonix.amerricard.utils.SystemUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class CalendarFragment extends BaseFragment implements CalendarAdapter.OnCalendarItemClickListener {

    @Inject
    SharedHelper sharedHelper;
    @Inject
    EventRepository eventRepository;

    private static final String TAG = CalendarFragment.class.getSimpleName();
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private ContentResolver contentResolver;
    private List<Contact> contacts = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);

        eventRepository.getEvents(new MyEventGetCallback());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.card_empty_text);

        adapter = new CalendarAdapter(getActivity(), sharedHelper.getLanguage(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        contentResolver = getActivity().getContentResolver();

        manageVisible(false);

        if (SystemUtils.isPermissionGranted(getActivity())) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1001);
        } else {
            contacts = getContactsWithBirthday();
            updateEvents(null);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabCalendar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateBirthdayActivity.class));
            }
        });
    }

    private void manageVisible(boolean isListVisible) {
        if (isListVisible) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    public void updateEvents(List<EventItem> events) {
        final List<Object> objects = new ArrayList<>();

        if (events != null) {
            objects.addAll(events);
        }
        objects.addAll(contacts);

        if (objects.size() == 0) {
            manageVisible(false);
        } else {
            manageVisible(true);
        }

        final List<EventItem> eventForHide = sharedHelper.getEventsForHide();

        adapter.updateData(objects, eventForHide);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contacts = getContactsWithBirthday();
                updateEvents(null);
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we can not display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<Contact> getContactsWithBirthday() {
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
            if (TextUtils.equals(formatterBirthday, "01.01.1970")){
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

        sharedHelper.saveContacts(contactsAndBirthdays);

        Log.d(TAG, "getContactsWithBirthday: selection time = " + (System.currentTimeMillis() - startQuery) + " ms");
        return contactsAndBirthdays;
    }

    @Override
    public void onItemClicked(int position) {

    }

    private class MyEventGetCallback implements EventGetCallback {
        @Override
        public void onSuccess(List<EventItem> events) {

            for (EventItem event : events){
                Log.d(TAG, "onSuccess: event="+event.getName().getEn());
            }
            updateEvents(events);
        }

        @Override
        public void onError() {

        }

        @Override
        public void onRetrofitError(String message) {

        }
    }
}
