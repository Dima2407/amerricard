package com.devtonix.amerricard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.ui.activity.CategoryActivity;
import com.devtonix.amerricard.ui.adapter.CardAdapter;
import com.devtonix.amerricard.ui.callback.CardGetCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CardFragment extends Fragment implements CardAdapter.OnFavoriteClickListener {

    @Inject
    CardRepository cardRepository;

    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);

        cardRepository.getCards(new MyCardGetCallback());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.card_empty_text);

        adapter = new CardAdapter(getActivity(), new ArrayList<CategoryItemFirstLevel>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        manageVisible(false);

        return view;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra(CategoryActivity.POSITION_FOR_CATEGORY, position);
        startActivity(intent);
    }

    public void updateData(List<CategoryItemFirstLevel> items) {
        if (items == null || items.size()==0) {
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
        public void onSuccess(List<CategoryItemFirstLevel> data) {
            updateData(data);
        }

        @Override
        public void onError() {

        }

        @Override
        public void onRetrofitError(String message) {

        }
    }
}
