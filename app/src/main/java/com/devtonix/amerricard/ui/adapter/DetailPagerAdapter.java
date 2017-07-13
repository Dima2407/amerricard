package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
    private SparseArray<Fragment> tourFragments = new SparseArray<>();
    private boolean isFullScreen = false;

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
        return DetailFragment.getInstance(items.get(position), isFullScreen);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(R.string.cards);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        DetailFragment fragment = (DetailFragment) super.instantiateItem(container, position);
        tourFragments.put(position, fragment);
        return fragment;
    }

    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;

        for (int i = 0; i < tourFragments.size(); i++) {
            if (tourFragments.get(i) != null) {
                ((DetailFragment) tourFragments.get(i)).updateFragment(isFullScreen);
            }
        }
    }
}
