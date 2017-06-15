package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;
import com.devtonix.amerricard.model.CategoryItemSecondLevel;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.ui.adapter.CategoryAdapter;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CategoryActivity extends BaseActivity {

    @Inject
    CardRepository cardRepository;

    private TabLayout tab;
    public List<CategoryItemSecondLevel> categories = new ArrayList<>();
    private CategoryAdapter adapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ACApplication.getMainComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("pos");
        } else {
            position = getIntent().getIntExtra("position", 0);
        }

        final List<CategoryItemFirstLevel> items = cardRepository.getCardsFromStorage();
        final CategoryItemFirstLevel item = items.get(position);
        final String title = LanguageUtils.convertLang(item.getName(), CategoryActivity.this);

        setTitle(title);

        if (item.getData() != null && item.getData().size() != 0) {
            categories = item.getData();
        }

//        if (categories.size() > 0) {
        findViewById(R.id.multiple_fragment).setVisibility(View.VISIBLE);
        findViewById(R.id.single_fragment).setVisibility(View.GONE);

        ViewPager pager = (ViewPager) findViewById(R.id.category_view_pager);

        adapter = new CategoryAdapter(this, getSupportFragmentManager(), categories);
        pager.setAdapter(adapter);

        RecyclerTabLayout recyclerTabLayout = (RecyclerTabLayout) findViewById(R.id.category_tab_layout);
        recyclerTabLayout.setUpWithViewPager(pager);
//        } else {
//            findViewById(R.id.single_fragment).setVisibility(View.VISIBLE);
//            findViewById(R.id.multiple_fragment).setVisibility(View.GONE);
//
//            FragmentManager fragMan = getSupportFragmentManager();
//            FragmentTransaction fragTransaction = fragMan.beginTransaction();
//
//            Fragment categoryFragment = CategoryFragment.getInstance(-1);
//
//            fragTransaction.add(R.id.single_fragment, categoryFragment, "single");
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

    public List<CardItem> getCategories(int position) {
//        if (position == -1) {
//            return categories;
//        }
        return categories.get(position).getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("pos", position);
    }
}
