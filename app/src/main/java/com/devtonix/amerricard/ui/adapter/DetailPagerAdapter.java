package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.ui.fragment.DetailFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<CardItem> items = new ArrayList<>();

    public DetailPagerAdapter(Context context, FragmentManager fragmentManager, List<CardItem> items) {
        super(fragmentManager);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.getInstance(items.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(R.string.cards);
    }

}
