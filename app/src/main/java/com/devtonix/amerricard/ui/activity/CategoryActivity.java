package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.adapter.CategoryAdapter;
import com.devtonix.amerricard.ui.fragment.CategoryFragment;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class CategoryActivity extends BaseActivity {

    private static final String POSITION_FOR_SAVE_INSTANCE_STATE = "position_for_save_instance_state";
    public static final String POSITION_FOR_CATEGORY = "position_for_category";
    public static final String ACTION_FROM_EVENTS = "action_from_events";
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";

    @Inject
    CardRepository cardRepository;
    @Inject
    SharedHelper sharedHelper;

    private List<CategoryItem> categoriesSecondLvl = new ArrayList<>();
    private CategoryAdapter adapter;
    private int positionForCategory;
    private List<CategoryItem> mainCategories;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ACApplication.getMainComponent().inject(this);

        initToolbar();

        if (savedInstanceState != null) {
            positionForCategory = savedInstanceState.getInt(POSITION_FOR_SAVE_INSTANCE_STATE);
        } else {
            positionForCategory = getIntent().getIntExtra(POSITION_FOR_CATEGORY, 0);
        }

        mainCategories = cardRepository.getCardsFromStorage();
        Collections.sort(mainCategories, new Comparator<CategoryItem>() {
            @Override
            public int compare(CategoryItem o1, CategoryItem o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });

        final CategoryItem currentCategory = mainCategories.get(positionForCategory);
        final String title = LanguageUtils.convertLang(currentCategory.getName(), sharedHelper.getLanguage());

        setTitle(title);

        if (currentCategory.getData() != null && currentCategory.getData().size() != 0) {
            categoriesSecondLvl = currentCategory.getCategoryItems();
        }

        if (TextUtils.equals(getIntent().getAction(), ACTION_FROM_EVENTS)) {
            //for display content of only one category (list of card)
            findViewById(R.id.multiple_fragment).setVisibility(View.GONE);
            findViewById(R.id.single_fragment).setVisibility(View.VISIBLE);

            final String currLang = sharedHelper.getLanguage();

            categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, 0);

            for (CategoryItem category : mainCategories) {
                if (category.getId() == categoryId) {
                    final String categoryName = LanguageUtils.convertLang(category.getName(), currLang);
                    setTitle(categoryName);
                } else {
                    final List<CategoryItem> innerCategories = category.getCategoryItems();
                    for (CategoryItem innerCategory : innerCategories) {
                        if (innerCategory.getId() == categoryId) {
                            final String categoryName = LanguageUtils.convertLang(innerCategory.getName(), currLang);
                            setTitle(categoryName);
                        } else {
                            // there is no required cards
                        }
                    }
                }
            }

            Fragment fragment = CategoryFragment.getInstanceForCategoryId(categoryId);
            getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment, fragment, "category").commit();
        } else if (categoriesSecondLvl.size() > 0) {
            //for display categories with cards
            findViewById(R.id.multiple_fragment).setVisibility(View.VISIBLE);
            findViewById(R.id.single_fragment).setVisibility(View.GONE);

            final ViewPager pager = (ViewPager) findViewById(R.id.category_view_pager);
            adapter = new CategoryAdapter(this, getSupportFragmentManager(), categoriesSecondLvl, positionForCategory, sharedHelper.getLanguage());
            pager.setAdapter(adapter);
            pager.setOffscreenPageLimit(1);
            RecyclerTabLayout recyclerTabLayout = (RecyclerTabLayout) findViewById(R.id.category_tab_layout);
            recyclerTabLayout.setUpWithViewPager(pager);

        } else {
            //for display cards only
            findViewById(R.id.multiple_fragment).setVisibility(View.GONE);
            findViewById(R.id.single_fragment).setVisibility(View.VISIBLE);

            Fragment fragment = CategoryFragment.getInstance(positionForCategory);
            getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment, fragment, "category").commit();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<CardItem> getCategories(int position) {

        //if user click to some event then need to show list of card selected category
        if (TextUtils.equals(getIntent().getAction(), ACTION_FROM_EVENTS)) {
            for (CategoryItem category : mainCategories) {
                if (category.getId() == categoryId) {
                    return category.getCardItems();
                } else {
                    final List<CategoryItem> innerCategories = category.getCategoryItems();
                    for (CategoryItem innerCategory : innerCategories) {
                        if (innerCategory.getId() == categoryId) {
                            return innerCategory.getCardItems();
                        } else {
                            // there is no required cards
                        }
                    }
                }
            }
        }

        //if it is not category so this is cards

        if (categoriesSecondLvl.size() == 0) {
            return mainCategories.get(positionForCategory).getCardItems();
        }

        return categoriesSecondLvl.get(position).getCardItems();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_FOR_SAVE_INSTANCE_STATE, positionForCategory);
    }
}
