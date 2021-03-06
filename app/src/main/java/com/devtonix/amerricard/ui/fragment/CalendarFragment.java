package com.devtonix.amerricard.ui.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.BaseEvent;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.model.Celebrity;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.model.Setting;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.repository.CelebrityRepository;
import com.devtonix.amerricard.repository.ContactRepository;
import com.devtonix.amerricard.repository.EventRepository;
import com.devtonix.amerricard.repository.SettingsRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.activity.CategoryActivity;
import com.devtonix.amerricard.ui.adapter.CalendarAdapterNew;
import com.devtonix.amerricard.ui.callback.CelebritiesGetCallback;
import com.devtonix.amerricard.ui.callback.EventGetCallback;
import com.devtonix.amerricard.ui.callback.GetContactBirthdayCallback;
import com.devtonix.amerricard.ui.callback.SettingsGetCallback;
import com.devtonix.amerricard.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CalendarFragment extends BaseFragment {

    @Inject
    SharedHelper sharedHelper;
    @Inject
    EventRepository eventRepository;
    @Inject
    CelebrityRepository celebrityRepository;
    @Inject
    ContactRepository contactRepository;
    @Inject
    SettingsRepository settingsRepository;
    @Inject
    CardRepository cardRepository;

    private static final String TAG = CalendarFragment.class.getSimpleName();
    private static final int REQUEST_CODE_FOR_CREATING_CONTACT = 4455;
    private static int AMOUNT_OF_EVENT_TYPES;

    private RecyclerView recyclerView;
    private TextView emptyText;
    private SwipeRefreshLayout srlContainer;
    private ContentResolver contentResolver;
    private List<BaseEvent> baseEvents = new ArrayList<>();
    private CalendarAdapterNew calendarAdapterNew;
    private FloatingActionButton fabCalendar;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabRefresh;
    private boolean isFabMenuOpen = false;
    private LinearLayout linearFabCalendar;
    private LinearLayout linearFabRefresh;
    private int amountOfUpdate = 0;
    private ImageView imgWhiteBackground;
    private Setting settings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
        contentResolver = getActivity().getContentResolver();

        AMOUNT_OF_EVENT_TYPES = sharedHelper.getCelebritiesInSettings() ? 3 : 2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.card_empty_text);
        srlContainer = (SwipeRefreshLayout) view.findViewById(R.id.srlContainer);

        calendarAdapterNew = new CalendarAdapterNew(sharedHelper.getLanguage(), getContext(), new MyOnCalendarItemClickListener(), sharedHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(calendarAdapterNew);

        fill();

        settingsRepository.getSettings(new MyGetSettingsCallback());

        manageVisible(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        fabRefresh = (FloatingActionButton) view.findViewById(R.id.fabRefresh);
        fabCalendar = (FloatingActionButton) view.findViewById(R.id.fabCalendar);

        linearFabCalendar = (LinearLayout) view.findViewById(R.id.linear_fab_calendar);
        linearFabRefresh = (LinearLayout) view.findViewById(R.id.linear_fab_refresh);

        imgWhiteBackground = (ImageView) view.findViewById(R.id.img_white_background);

        imgWhiteBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabMenu();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFabMenuOpen) {
                    showFabMenu();
                } else {
                    closeFabMenu();
                }
            }
        });

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srlContainer.setRefreshing(true);
                fill();
                closeFabMenu();
            }
        });

        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE_FOR_CREATING_CONTACT);
                closeFabMenu();
            }
        });

        srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlContainer.setRefreshing(true);

                fill();
            }
        });

    }

    private void showFabMenu() {
        isFabMenuOpen = true;
        fabAdd.setImageResource(R.drawable.ic_cancel);
        imgWhiteBackground.setVisibility(View.VISIBLE);
        linearFabCalendar.setVisibility(View.VISIBLE);
        linearFabRefresh.setVisibility(View.VISIBLE);
    }

    private void closeFabMenu() {
        isFabMenuOpen = false;
        fabAdd.setImageResource(R.drawable.ic_add_something_white);
        imgWhiteBackground.setVisibility(View.GONE);
        linearFabCalendar.setVisibility(View.GONE);
        linearFabRefresh.setVisibility(View.GONE);
    }

    private void fill() {

        baseEvents.clear();

        eventRepository.getEvents(new MyEventGetCallback());
        if (sharedHelper.getCelebritiesInSettings()) {
            celebrityRepository.getCelebrities(new MyCelebritiesGetCallback());
        }
        if (SystemUtils.isPermissionNotGranted(getActivity())) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1001);
        } else {
            contactRepository.getContactsWithBirthday(contentResolver, new MyGetContactBirthdayCallback());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_CREATING_CONTACT) {

            if (SystemUtils.isPermissionNotGranted(getActivity())) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1001);
            } else {

                fill();
                amountOfUpdate--;
                updateEventsNew();

            }
        }
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

    private void controlVisibility() {
        if (baseEvents.size() == 0) {
            manageVisible(false);
        } else {
            manageVisible(true);
        }
    }

    public void updateEventsNew() {
        amountOfUpdate++;
        controlVisibility();
        if (amountOfUpdate == AMOUNT_OF_EVENT_TYPES) {
            calendarAdapterNew.updateAdapter(baseEvents);
            amountOfUpdate = 0;
        }

        recyclerView.scrollToPosition(calendarAdapterNew.getNearestDatePosition());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fill();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we can not display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyEventGetCallback implements EventGetCallback {
        @Override
        public void onSuccess(List<EventItem> events) {
            baseEvents.addAll(events);
            updateEventsNew();
            srlContainer.setRefreshing(false);
        }

        @Override
        public void onError() {
            updateEventsNew();
            srlContainer.setRefreshing(false);
        }

        @Override
        public void onRetrofitError(String message) {
            updateEventsNew();
            srlContainer.setRefreshing(false);
        }
    }

    private class MyCelebritiesGetCallback implements CelebritiesGetCallback {
        @Override
        public void onSuccess(List<Celebrity> celebrities) {
            baseEvents.addAll(celebrities);
            updateEventsNew();
            srlContainer.setRefreshing(false);
        }

        @Override
        public void onError() {
            updateEventsNew();
            srlContainer.setRefreshing(false);
        }

        @Override
        public void onRetrofitError(String message) {
            updateEventsNew();
            srlContainer.setRefreshing(false);
        }
    }

    private class MyGetContactBirthdayCallback implements GetContactBirthdayCallback {
        @Override
        public void onSuccess(List<Contact> contacts) {
            baseEvents.addAll(contacts);
            updateEventsNew();
        }
    }

    private class MyGetSettingsCallback implements SettingsGetCallback {

        @Override
        public void onSucces(Setting settings) {
            CalendarFragment.this.settings = settings;
            Log.d(TAG, "MyGetSettingsCallback onSucces = " + settings.getBirthdayCategoryId());
        }

        @Override
        public void onError() {
            CalendarFragment.this.settings = null;
            Log.d(TAG, "MyGetSettingsCallback onError");
        }

        @Override
        public void onRetrofitError(String message) {
            CalendarFragment.this.settings = null;
            Log.d(TAG, "MyGetSettingsCallback onRetrofitError = " + message);
        }
    }

    private class MyOnCalendarItemClickListener implements CalendarAdapterNew.OnCalendarItemClickListener {
        @Override
        public void onItemClicked(int position) {

            final BaseEvent baseEvent = calendarAdapterNew.getItem(position);
            switch (baseEvent.getEventType()) {
                case BaseEvent.TYPE_EVENT:
                    final int categoryId = ((EventItem) baseEvent).getCategoryId();
                    Intent intent = new Intent(getActivity(), CategoryActivity.class);
                    intent.setAction(CategoryActivity.ACTION_FROM_EVENTS);
                    intent.putExtra(CategoryActivity.EXTRA_CATEGORY_ID, categoryId);
                    startActivity(intent);
                    break;
                case BaseEvent.TYPE_CONTACT:
                    final int birthdayCategoryId = settingsRepository.getCategoryIdFromStorage();
                    Intent intentForContact = new Intent(getActivity(), CategoryActivity.class);
                    intentForContact.setAction(CategoryActivity.ACTION_FROM_CONTACTS);
                    intentForContact.putExtra(CategoryActivity.EXTRA_CATEGORY_ID, birthdayCategoryId);
                    startActivity(intentForContact);
                    break;
                case BaseEvent.TYPE_CELEBRITY:
                    //todo implement this action ASAP

                    break;
            }
        }
    }
}
