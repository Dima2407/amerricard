package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.ui.adapter.CategoryAdapter;
import com.devtonix.amerricard.ui.fragment.CategoryFragment;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.devtonix.amerricard.utils.SharedHelper;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends BaseActivity {

//    private TabLayout tab;
//    public List<Item> categories;
//    private CategoryAdapter adapter;
//    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        List<Item> items = SharedHelper.getInstance().getCards();
//
//        if (savedInstanceState != null) {
//            position = savedInstanceState.getInt("pos");
//        } else {
//            position = getIntent().getIntExtra("position", 0);
//        }
//        Item item = items.get(position);
//
//
//        setTitle(LanguageUtils.cardNameWrapper(item.getName()));
//
//
//        categories = new ArrayList<>();
//        if (item.data != null && item.data.size()!=0) {
//            categories = item.data;
//        }
//
//        int containsCategory = 0;
//        for (Item i: categories) {
//            if (i.isItemCategory()) {
//                containsCategory++;
//            }
//        }
//
//        if (containsCategory>0) {
//            findViewById(R.id.multiple_fragment).setVisibility(View.VISIBLE);
//            findViewById(R.id.single_fragment).setVisibility(View.GONE);
//
//            ViewPager pager = (ViewPager) findViewById(R.id.category_view_pager);
//
//            adapter = new CategoryAdapter(this, getSupportFragmentManager(), categories);
//            pager.setAdapter(adapter);
//
//            RecyclerTabLayout recyclerTabLayout = (RecyclerTabLayout)
//                    findViewById(R.id.category_tab_layout);
//            recyclerTabLayout.setUpWithViewPager(pager);
////            tab = (TabLayout) findViewById(R.id.category_tab_layout);
////            tab.setTabTextColors(getResources().getColor(R.color.tabGray), getResources().getColor(android.R.color.white));
////            tab.setSelectedTabIndicatorColor(Color.WHITE);
////            tab.setupWithViewPager(pager);
//
//        } else {
//            findViewById(R.id.single_fragment).setVisibility(View.VISIBLE);
//            findViewById(R.id.multiple_fragment).setVisibility(View.GONE);
//
//            FragmentManager fragMan = getSupportFragmentManager();
//            FragmentTransaction fragTransaction = fragMan.beginTransaction();
//
//            Fragment categoryFragment = CategoryFragment.getInstance(-1);
//
//            fragTransaction.add(R.id.single_fragment, categoryFragment , "single");
//            fragTransaction.commit();
//        }
    }

//    @Override
//    public void onItemClicked(int position) {
//        Item item = categories.get(position);
//
//        Intent intent = new Intent(this, DetailActivity.class);
//        intent.putExtra("item", item);
//        startActivity(intent);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    public List<Item> getCategories(int position) {
//        if (position==-1) {
//            return categories;
//        }
//        return categories.get(position).data;
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("pos", position);
    }
}
