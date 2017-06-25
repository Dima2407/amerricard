package com.devtonix.amerricard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.ui.adapter.FavoriteCardAdapter;
import com.devtonix.amerricard.ui.callback.CardDeleteFromFavoriteCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FavoriteActivity extends DrawerActivity implements FavoriteCardAdapter.OnFavoriteClickListener {

    private static final String TAG = FavoriteActivity.class.getSimpleName();
    @Inject
    CardRepository cardRepository;

    private FavoriteCardAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private List<CardItem> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_favorite);

        ACApplication.getMainComponent().inject(this);

        setTitle(getString(R.string.favorites));

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        emptyText = (TextView) findViewById(R.id.card_empty_text);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                manageRecycler();
            }
        });

    }

    private void manageRecycler() {
        cards = cardRepository.getFavoriteCardsFromStorage();
        if (cards != null && cards.size() != 0) {
            manageVisible(true);

            int width = (recyclerView.getWidth()) / 2;
            int height = (int) (width * 1.6);
            adapter = new FavoriteCardAdapter(
                    FavoriteActivity.this,
                    cards,
                    FavoriteActivity.this,
                    width,
                    height);
            recyclerView.setAdapter(adapter);
        } else {
            manageVisible(false);
        }
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putParcelableArrayListExtra(DetailActivity.PARCELABLE_CARDS, new ArrayList<CardItem>(cards));
        intent.putExtra(DetailActivity.POSITION_FOR_FAVORITE_CARD, position);
        intent.setAction(DetailActivity.ACTION_SHOW_FAVORITE_CARDS);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClicked(int position, CardItem item) {

        progressDialog.show();

        cardRepository.removeCardFromFavorites(item);
        cardRepository.sendDeleteFavoriteCardRequest(item.getId(), new MyCardDeleteFromFavoriteCallback());

        final List<CardItem> freshFavoritesCards = cardRepository.getFavoriteCardsFromStorage();
        adapter.setItems(freshFavoritesCards);

        if (cards != null && cards.size() != 0) {
            manageVisible(true);
        } else {
            manageVisible(false);
        }
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

    private class MyCardDeleteFromFavoriteCallback implements CardDeleteFromFavoriteCallback {
        @Override
        public void onSuccess() {
            progressDialog.dismiss();
        }

        @Override
        public void onError() {
            progressDialog.dismiss();
        }

        @Override
        public void onRetrofitError(String message) {
            progressDialog.dismiss();
        }
    }
}
