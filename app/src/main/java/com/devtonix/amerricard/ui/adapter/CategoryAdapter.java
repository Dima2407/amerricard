package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.model.CategoryItemSecondLevel;
import com.devtonix.amerricard.ui.fragment.CategoryFragment;
import com.devtonix.amerricard.utils.LanguageUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context context;
    private int positionForCategoryFirstLvl;
    private List<CategoryItem> categories = new ArrayList<>();
    private String lang;

  //  private SparseArray<Fragment> categoryFragments = new SparseArray<Fragment>();

    public CategoryAdapter(Context context, FragmentManager fragmentManager, List<CategoryItem> categories, int positionForCategoryFirstLvl, String language) {
        super(fragmentManager);
        this.context = context;
        this.categories = categories;
        this.positionForCategoryFirstLvl = positionForCategoryFirstLvl;
        this.lang = language;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CategoryFragment.getInstance(getRealPosition(position), positionForCategoryFirstLvl);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return LanguageUtils.convertLang(categories.get(getRealPosition(position)).getName(), lang);
    }

    private int getRealPosition(int position) {
        int realPosition;
        if (categories.size() > 2) {
            if (position == categories.size() - 1) {
                realPosition = 3;
            } else if (position == categories.size() - 2) {
                realPosition = 2;
            } else if (position == 0) {
                realPosition = categories.size() - 4;
            } else if (position == 1) {
                realPosition = categories.size() - 3;
            } /*else
                realPosition = position;*/
        } /*else
            realPosition = position;
        return realPosition - 2;*/
        return position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, getRealPosition(position));
        //categoryFragments.put(getRealPosition(position), fragment);
        return fragment;
    }

}



