package com.devtonix.amerricard.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.ui.adapter.VipPagerAdapter;

public class VipActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_vip);

        setTitle(getString(R.string.become_vip));

        ViewPager pager = (ViewPager) findViewById(R.id.vip_view_pager);
        VipPagerAdapter adapter = new VipPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        TabLayout tab = (TabLayout) findViewById(R.id.vip_tab_layout);
        tab.setTabTextColors(getResources().getColor(R.color.tabGray), getResources().getColor(android.R.color.white));
        tab.setSelectedTabIndicatorColor(Color.WHITE);
        tab.setupWithViewPager(pager);
    }
}
