package com.devtonix.amerricard.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.api.NetworkService;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.ui.adapter.MainPagerAdapter;
import com.devtonix.amerricard.ui.fragment.CardFragment;
import com.devtonix.amerricard.utils.Preferences;

import java.util.List;

public class MainActivity extends DrawerActivity {

    private MainPagerAdapter adapter;
    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.main_view_pager);
        adapter = new MainPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        tab = (TabLayout) findViewById(R.id.main_tab_layout);
        tab.setTabTextColors(Color.WHITE, Color.WHITE);
        tab.setSelectedTabIndicatorColor(Color.WHITE);
        tab.setupWithViewPager(pager);

        NetworkService.getCards(this);
        NetworkService.getEvents(this);

        int position = getIntent().getIntExtra("position", 0);
        pager.setCurrentItem(position);
    }

    @Override
    protected void handleCardSuccessEvent(List<Item> items) {
        Log.d("handleCardSuccessEvent", "data "+items.size());
        Preferences.getInstance().saveCards(items);
        ((CardFragment) adapter.getCardFragment()).updateData(items);
    }

    @Override
    protected void handleEventSuccessEvent(List<EventItem> item) {
//        Preferences.getInstance().saveCards(items);
//        ((CalendarFragment) adapter.getCalendarFragment()).updateData(items);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
