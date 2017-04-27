package com.devtonix.amerricard.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.devtonix.amerricard.ui.adapter.MainPagerAdapter;
import com.devtonix.amerricard.R;

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
        tab.setupWithViewPager(pager);
    }
}
