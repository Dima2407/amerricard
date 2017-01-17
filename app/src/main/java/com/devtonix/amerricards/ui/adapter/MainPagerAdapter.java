package com.devtonix.amerricards.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.devtonix.amerricards.R;
import com.devtonix.amerricards.model.BaseItem;
import com.devtonix.amerricards.ui.fragment.CalendarFragment;
import com.devtonix.amerricards.ui.fragment.CardFragment;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<BaseItem> items = new ArrayList<>();

    private SparseArray<Fragment> tourFragments = new SparseArray<Fragment>();

    public MainPagerAdapter(Context context, FragmentManager fragmentManager) {
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
            return new CardFragment();
        } else {
            return new CalendarFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.cards);
        } else {
            return context.getString(R.string.calendar);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        tourFragments.put(position, fragment);
        return fragment;
    }
}


