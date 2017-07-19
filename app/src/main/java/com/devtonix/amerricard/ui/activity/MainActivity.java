package com.devtonix.amerricard.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.receivers.HolidaysBroadcastReceiver;
import com.devtonix.amerricard.ui.adapter.MainPagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.view.View.GONE;

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
        if (!sharedHelper.isVipOrPremium()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(GONE);
        }

        //todo perhaps, needs check isAppFirstLaunch
        startNotificationReceiver();
        saveDislayWidth();
    }

    private void saveDislayWidth() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        sharedHelper.saveDisplayWidth(screenWidth);
    }

    private void startNotificationReceiver() {
        Log.e(TAG, "startNotificationReceiver @(^_^)@");
        Intent startReceiver = new Intent(this, HolidaysBroadcastReceiver.class);
        getApplicationContext().sendBroadcast(startReceiver);
    }

    @Override
    public void onBackPressed() {

        if (getDrawer().isDrawerOpen(GravityCompat.START)) {
            getDrawer().closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.exit_message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finishAffinity();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.show();
        }
    }
}
