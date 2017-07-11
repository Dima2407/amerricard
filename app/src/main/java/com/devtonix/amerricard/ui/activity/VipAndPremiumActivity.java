package com.devtonix.amerricard.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.ui.adapter.VipPagerAdapter;

public class VipAndPremiumActivity extends DrawerActivity {

    public static final String TAB_POSITION = "tab_position";
    public static final String SHOW_VIP_ACTION = "action_vip";
    public static final String SHOW_PREMIUM_ACTION = "action_premium";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_vip_and_premium);

        setTitle(getString(R.string.become_vip_title));

        ViewPager pager = (ViewPager) findViewById(R.id.vip_view_pager);
        VipPagerAdapter adapter = new VipPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        TabLayout tab = (TabLayout) findViewById(R.id.vip_tab_layout);
        tab.setTabTextColors(getResources().getColor(R.color.tabGray), getResources().getColor(android.R.color.white));
        tab.setSelectedTabIndicatorColor(Color.WHITE);
        tab.setupWithViewPager(pager);

        final int tabPosition = getIntent().getIntExtra(TAB_POSITION, 0);
        pager.setCurrentItem(tabPosition);

        if (getIntent().getAction() != null){
            switch (getIntent().getAction()) {
                case SHOW_VIP_ACTION:
                    pager.setCurrentItem(0);
                    break;
                case SHOW_PREMIUM_ACTION:
                    pager.setCurrentItem(1);
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
