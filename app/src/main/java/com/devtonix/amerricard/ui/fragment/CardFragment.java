package com.devtonix.amerricard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.activity.CategoryActivity;
import com.devtonix.amerricard.ui.adapter.CardAdapter;
import com.devtonix.amerricard.ui.callback.CardGetCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class CardFragment extends BaseFragment implements CardAdapter.OnFavoriteClickListener {

    @Inject
    CardRepository cardRepository;
    @Inject
    SharedHelper sharedHelper;

    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private SwipeRefreshLayout srlContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.card_empty_text);
        srlContainer = (SwipeRefreshLayout) view.findViewById(R.id.srlContainer);

        adapter = new CardAdapter(getActivity(), new ArrayList<CategoryItem>(), sharedHelper.getLanguage(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        manageVisible(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //if this is a first time when app launch, I want to get cards from network
        //after that I load cards from storage (shared prefs)

        Log.d("MyCatLog", "isFirstLaunchApplication: " + sharedHelper.isFirstLaunchApplication());

        if (sharedHelper.isFirstLaunchApplication()) {
            srlContainer.setRefreshing(true);
            cardRepository.getCards(new MyCardGetCallback());
            Log.d("MyCatLog", "onViewCreated: -");
            sharedHelper.setFirstLaunchApplication(false);
        } else {
            Log.d("MyCatLog", "onViewCreated: +");
            final List<CategoryItem> mainCategories = cardRepository.getCardsFromStorage();
            updateData(mainCategories);
            Collections.sort(mainCategories, new Comparator<CategoryItem>() {
                @Override
                public int compare(CategoryItem o1, CategoryItem o2) {
                    return o1.getOrder() - o2.getOrder();
                }
            });
        }

        srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlContainer.setRefreshing(true);
                cardRepository.getCards(new MyCardGetCallback());
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra(CategoryActivity.POSITION_FOR_CATEGORY, position);
        startActivity(intent);
    }

    public void updateData(List<CategoryItem> items) {
        if (items == null || items.size() == 0) {
            manageVisible(false);
        } else {
            manageVisible(true);
        }
        adapter.updateData(items);
    }

    private void manageVisible(boolean isListVisible) {
        if (isListVisible) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    private class MyCardGetCallback implements CardGetCallback {
        @Override
        public void onSuccess(List<CategoryItem> data) {
            updateData(data);

            srlContainer.setRefreshing(false);
        }

        @Override
        public void onError() {
            srlContainer.setRefreshing(false);
        }

        @Override
        public void onRetrofitError(String message) {
            srlContainer.setRefreshing(false);
        }
    }
}
