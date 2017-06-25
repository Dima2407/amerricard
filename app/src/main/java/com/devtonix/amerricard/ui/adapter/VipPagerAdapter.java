package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.ui.fragment.PremiumFragment;
import com.devtonix.amerricard.ui.fragment.VipFragment;

public class VipPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private SparseArray<Fragment> vipFragments = new SparseArray<Fragment>();

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
        if (position == 0) {
            return new VipFragment();
        } else {
            return new PremiumFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.vip);
        } else {
            return context.getString(R.string.premium);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        vipFragments.put(position, fragment);
        return fragment;
    }
}


