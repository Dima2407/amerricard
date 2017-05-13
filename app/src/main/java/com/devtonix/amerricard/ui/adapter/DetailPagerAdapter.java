package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.ui.fragment.DetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksii on 11.05.17.
 */
public class DetailPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Item> items = new ArrayList<>();
    private boolean isFullScreen = false;

    private SparseArray<Fragment> tourFragments = new SparseArray<Fragment>();

    public DetailPagerAdapter(Context context, FragmentManager fragmentManager, List<Item> items) {
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
        return DetailFragment.getInstance((int) items.get(position).id, isFullScreen);
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

        for (int i=0; i<tourFragments.size(); i++) {
            if (tourFragments.get(i) != null) {
                ((DetailFragment) tourFragments.get(i)).updateFragment(isFullScreen);
            }
        }
    }

    public ImageView getImage(int position) {
        return ((DetailFragment)tourFragments.get(position)).getImageWithPic();
    }
}
