package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.api.NetworkService;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.ui.adapter.HolidaysAdapter;
import com.devtonix.amerricard.utils.Preferences;

import java.util.List;

public class ManageActivity extends DrawerActivity {

    private HolidaysAdapter adapter;
    private RecyclerView rvHolidays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_manage_holidays);
        setTitle(getResources().getString(R.string.manage_birthdays));

        rvHolidays = (RecyclerView) findViewById(R.id.rv_holidays);
        adapter = new HolidaysAdapter(this, new MyOnSwitchClickListener());
        rvHolidays.setLayoutManager(new LinearLayoutManager(ManageActivity.this));
        rvHolidays.setAdapter(adapter);
    }

    @Override
    protected void handleSuccessEvent(String message) {
        super.handleSuccessEvent(message);

    }

    @Override
    protected void handleEventSuccessEvent(List<Item> holidays) {
        super.handleEventSuccessEvent(holidays);

        adapter.updateData(holidays);
    }

    @Override
    protected void handleFailureFound(String message) {
        super.handleFailureFound(message);

//        adapter.updateData(Preferences.getInstance().getEvents());
    }

    @Override
    protected void onResume() {
        super.onResume();

        NetworkService.getEvents(this);
    }

    private class MyOnSwitchClickListener implements HolidaysAdapter.OnSwitchClickListener {
        @Override
        public void onItemClicked(int position) {

        }
    }
}
