package com.devtonix.amerricard.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.receivers.HolidaysBroadcastReceiver;
import com.devtonix.amerricard.ui.adapter.MainPagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends DrawerActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainPagerAdapter adapter;
    private TabLayout tab;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.main_view_pager);
        adapter = new MainPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        tab = (TabLayout) findViewById(R.id.main_tab_layout);
        tab.setTabTextColors(getResources().getColor(R.color.tabGray), getResources().getColor(android.R.color.white));
        tab.setSelectedTabIndicatorColor(Color.WHITE);
        tab.setupWithViewPager(pager);

        int position = getIntent().getIntExtra("position", 0);
        pager.setCurrentItem(position);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //todo perhaps, needs check isAppFirstLaunch
        startNotificationReceiver();
    }

    private void startNotificationReceiver() {
        Log.d(TAG, "startNotificationReceiver @(^_^)@");
        Intent startReceiver = new Intent(this, HolidaysBroadcastReceiver.class);
        getApplicationContext().sendBroadcast(startReceiver);
    }
}
