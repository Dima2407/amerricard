package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;
import com.devtonix.amerricard.model.CategoryItemSecondLevel;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.adapter.CategoryAdapter;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CategoryActivity extends BaseActivity {

    private static final String POSITION_FOR_SAVE_INSTANCE_STATE = "position_for_save_instance_state";
    public static final String POSITION_FOR_CATEGORY = "position_for_category";

    @Inject
    CardRepository cardRepository;
    @Inject
    SharedHelper sharedHelper;

    private List<CategoryItemSecondLevel> categoriesSecondLvl = new ArrayList<>();
    private CategoryAdapter adapter;
    private int positionForCategoryFirstLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ACApplication.getMainComponent().inject(this);

        initToolbar();

        if (savedInstanceState != null) {
            positionForCategoryFirstLvl = savedInstanceState.getInt(POSITION_FOR_SAVE_INSTANCE_STATE);
        } else {
            positionForCategoryFirstLvl = getIntent().getIntExtra(POSITION_FOR_CATEGORY, 0);
        }

        final List<CategoryItemFirstLevel> items = cardRepository.getCardsFromStorage();
        final CategoryItemFirstLevel item = items.get(positionForCategoryFirstLvl);
        final String title = LanguageUtils.convertLang(item.getName(), sharedHelper.getLanguage());

        setTitle(title);

        if (item.getData() != null && item.getData().size() != 0) {
            categoriesSecondLvl = item.getData();
        }

        findViewById(R.id.multiple_fragment).setVisibility(View.VISIBLE);
        findViewById(R.id.single_fragment).setVisibility(View.GONE);

        ViewPager pager = (ViewPager) findViewById(R.id.category_view_pager);

        adapter = new CategoryAdapter(this, getSupportFragmentManager(), categoriesSecondLvl, positionForCategoryFirstLvl, sharedHelper.getLanguage());
        pager.setAdapter(adapter);

        RecyclerTabLayout recyclerTabLayout = (RecyclerTabLayout) findViewById(R.id.category_tab_layout);
        recyclerTabLayout.setUpWithViewPager(pager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

//    @Override
//    public void onItemClicked(int positionForCategoryFirstLvl) {
//        Item item = categories.get(positionForCategoryFirstLvl);
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
        return categoriesSecondLvl.get(position).getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_FOR_SAVE_INSTANCE_STATE, positionForCategoryFirstLvl);
    }
}
