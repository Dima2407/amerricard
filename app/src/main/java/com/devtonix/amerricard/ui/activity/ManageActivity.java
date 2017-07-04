package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.repository.ContactRepository;
import com.devtonix.amerricard.ui.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ManageActivity extends DrawerActivity {

    @Inject
    ContactRepository contactRepository;

    private ContactAdapter adapter;
    private RecyclerView rvHolidays;
    private List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_manage_holidays);
        ACApplication.getMainComponent().inject(this);
        setTitle(getResources().getString(R.string.manage_birthdays));

        rvHolidays = (RecyclerView) findViewById(R.id.rv_holidays);

        adapter = new ContactAdapter(this, new MyOnSwitchClickListener());
        rvHolidays.setLayoutManager(new LinearLayoutManager(ManageActivity.this));
        rvHolidays.setAdapter(adapter);

        contacts = contactRepository.getContactsFromStorage();

        adapter.updateContacts(contacts);
    }

    @Override
    protected void onPause() {
        super.onPause();

        contactRepository.saveContactsToStorage(contacts);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        contactRepository.saveContactsToStorage(contacts);
    }

    private class MyOnSwitchClickListener implements ContactAdapter.OnSwitchClickListener {
        @Override
        public void onItemClicked(int position) {

        }
    }
}
