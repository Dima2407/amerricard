package com.devtonix.amerricard.ui.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.ui.adapter.CalendarAdapter;
import com.devtonix.amerricard.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnCalendarItemClickListener {

    private static final String TAG = CalendarFragment.class.getSimpleName();
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private ContentResolver contentResolver;
    private List<Contact> contacts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.card_empty_text);

        adapter = new CalendarAdapter(getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        contentResolver = getActivity().getContentResolver();

        manageVisible(false);

        if (SystemUtils.isPermissionGranted(getActivity())) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1001);
        } else {
            contacts = getContactsWithBirthday();
        }

        return view;
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

    public void updateData(List<Item> items) {
        final List<Object> objects = new ArrayList<>();

        objects.addAll(items);
        objects.addAll(contacts);

        if (objects.size() == 0) {
            manageVisible(false);
        } else {
            manageVisible(true);
        }

        adapter.updateData(objects);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contacts = getContactsWithBirthday();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we can not display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<Contact> getContactsWithBirthday() {
        final long startQuery = System.currentTimeMillis();
        final List<Contact> contactsAndBirthdays = new ArrayList<>(50);
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
            final Contact contact = new Contact();
                    contact.setName(cursor.getString(contactNameColumn));
                    contact.setBirthday(cursor.getString(birthdayColumn));
                    contact.setPhotoUri(cursor.getString(photoUriColumn));

            contactsAndBirthdays.add(contact);
        }

        cursor.close();

        Log.d(TAG, "getContactsWithBirthday: selection time = " + (System.currentTimeMillis() - startQuery) + " ms");
        return contactsAndBirthdays;
    }

    @Override
    public void onItemClicked(int position) {

    }
}
