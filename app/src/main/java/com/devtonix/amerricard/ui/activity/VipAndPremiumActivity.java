package com.devtonix.amerricard.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.devtonix.amerricard.R;

import com.devtonix.amerricard.ui.adapter.VipPagerAdapter;

public class VipAndPremiumActivity extends DrawerActivity {


    public static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzLiI3hjLJQjyFbj1JK8U6QM46jX7ZEW7xsKAqGgrVuXejydWOi+f0tyAVyRM6UPQOEygJWfnVNr5Fabg43KacO6HMPytpJt83wL3s0hwcx8jicqj/L3WIQfGPEYrvLXDubtzwDT4Wqc6YtY17BQNyEXDOEZ6PXMQBiIxNDvYALGpVXChS8xNmadeTlbkiUeBrvl75eJUs6CMc/wzKiB2P5JH1geHMk2dg6/+p+v7UdtRDzkfWiPKAJZ5Vm6h7WPL4VdeRB+KlIHD6+2AAxqKLbyicUbe27NL3ihTcr9+YTMuvgB0acWmPejMrICGOz7XG5WHSqo4anV2L62iJVUjqQIDAQAB";
    public static final String TAB_POSITION = "tab_position";
    public static final String SHOW_VIP_ACTION = "action_vip";
    public static final String SHOW_PREMIUM_ACTION = "action_premium";

    private LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_vip_and_premium);
        //setContentView(R.layout.activity_vip_and_premium);

        setTitle(getString(R.string.become_vip_title));

        ViewPager pager = (ViewPager) findViewById(R.id.vip_view_pager);
        VipPagerAdapter adapter = new VipPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        TabLayout tab = (TabLayout) findViewById(R.id.vip_tab_layout);
        tab.setTabTextColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(android.R.color.white));
        tab.setSelectedTabIndicatorColor(Color.TRANSPARENT);
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
}
