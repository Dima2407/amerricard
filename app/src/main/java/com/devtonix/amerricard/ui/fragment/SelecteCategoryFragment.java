package com.devtonix.amerricard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.activity.CategoryActivity;
import com.devtonix.amerricard.ui.adapter.CardAdapter;
import com.devtonix.amerricard.ui.adapter.CategoryAdapter;
import com.devtonix.amerricard.ui.adapter.MainPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SelecteCategoryFragment extends BaseFragment implements CardAdapter.OnFavoriteClickListener{


    @Inject
    CardRepository cardRepository;
    @Inject
    SharedHelper sharedHelper;

    private RecyclerView recyclerView;
    private CardAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selecte_category, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final List<CategoryItem> mainCategories = cardRepository.getCardsFromStorage();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_choise_category);
        adapter = new CardAdapter(getActivity(), mainCategories, sharedHelper.getLanguage(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra(CategoryActivity.POSITION_FOR_CATEGORY, position);
        startActivity(intent);
    }
}
