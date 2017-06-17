package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.devtonix.amerricard.model.CategoryItemSecondLevel;
import com.devtonix.amerricard.ui.fragment.CategoryFragment;
import com.devtonix.amerricard.utils.LanguageUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context context;
    private int positionForCategoryFirstLvl;
    private List<CategoryItemSecondLevel> categories = new ArrayList<>();
    private String lang;

    private SparseArray<Fragment> categoryFragments = new SparseArray<Fragment>();

    public CategoryAdapter(Context context, FragmentManager fragmentManager, List<CategoryItemSecondLevel> categories, int positionForCategoryFirstLvl, String language) {
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
        return CategoryFragment.getInstance(position, positionForCategoryFirstLvl);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return LanguageUtils.convertLang(categories.get(position).getNameJsonEl(), lang);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        categoryFragments.put(position, fragment);
        return fragment;
    }

}



