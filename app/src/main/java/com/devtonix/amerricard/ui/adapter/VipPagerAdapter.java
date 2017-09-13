package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.ui.fragment.PremiumFragment;
import com.devtonix.amerricard.ui.fragment.VipAndPremiumAbstractFragment;
import com.devtonix.amerricard.ui.fragment.VipFragment;

public class VipPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private VipAndPremiumAbstractFragment vipFragment;
    private VipAndPremiumAbstractFragment premiumFragment;
    public VipPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        return getActiveFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.vip);
        } else {
            return context.getString(R.string.premium);
        }
    }

    public VipAndPremiumAbstractFragment getActiveFragment(int position){
        if (position == 0) {
            if(vipFragment == null){
                vipFragment = new VipFragment();
            }
            return vipFragment;
        } else {
            if(premiumFragment == null){
                premiumFragment = new PremiumFragment();
            }
            return premiumFragment;
        }
    }
}


