package com.devtonix.amerricard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.ui.activity.CategoryActivity;
import com.devtonix.amerricard.ui.activity.DetailActivity;
import com.devtonix.amerricard.ui.adapter.CategoryGridAdapter;
import com.devtonix.amerricard.ui.callback.CardAddToFavoriteCallback;
import com.devtonix.amerricard.ui.callback.CardDeleteFromFavoriteCallback;

import java.util.List;

import javax.inject.Inject;

public class CategoryFragment extends BaseFragment implements CategoryGridAdapter.OnFavoriteClickListener {

    @Inject
    CardRepository cardRepository;

    private static final String TAG = CategoryFragment.class.getSimpleName();
    private CategoryGridAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private int positionForCategory;
    private int positionForCategoryFirstLvl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    public static CategoryFragment getInstance(int positionForCategorySecondLvl, int positionForCategoryFirstLvl) {
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setPositionForCategorySecondLvl(positionForCategorySecondLvl);
        categoryFragment.setPositionForCategoryFirstLvl(positionForCategoryFirstLvl);
        return categoryFragment;
    }

    private void setPositionForCategorySecondLvl(int positionForCategorySecondLvl) {
        this.positionForCategory = positionForCategorySecondLvl;
    }

    private void setPositionForCategoryFirstLvl(int positionForCategoryFirstLvl) {
        this.positionForCategoryFirstLvl = positionForCategoryFirstLvl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.card_empty_text);

        int countRow = 2;

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), countRow));
        recyclerView.post(new Runnable() {
            @Override
            public void run() {

                final List<CardItem> cards = ((CategoryActivity) getActivity()).getCategories(positionForCategory);

                try {
                    if (cards.size() != 0) {
                        Log.d("CategoryFragment", "visible");
                        manageVisible(true);

                        Log.d(TAG, "CATEGORY_POSITION = " + positionForCategory);

                        int width;
                        int height;

                        if (recyclerView.getWidth() > recyclerView.getHeight()) {
                            width = (recyclerView.getWidth()) / 4;
                        } else {
                            width = (recyclerView.getWidth()) / 2;
                        }
                        height = (int) (width * 1.6);

                        final List<CardItem> favoriteCards = cardRepository.getFavoriteCardsFromStorage();

                        adapter = new CategoryGridAdapter(
                                getActivity(),
                                cards,
                                CategoryFragment.this,
                                width,
                                height,
                                favoriteCards);

                        recyclerView.setAdapter(adapter);
                    } else {
                        manageVisible(false);
                        Log.d("CategoryFragment", "not visible");
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClicked(int pos) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.POSITION_FOR_CURRENT_CARD, pos);
        intent.putExtra(DetailActivity.POSITION_FOR_CATEGORY_SCND_LVL, positionForCategory); //todo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        intent.putExtra(DetailActivity.POSITION_FOR_CATEGORY_FRST_LVL, positionForCategoryFirstLvl);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClicked(int position, CardItem item) {

        progressDialog.show();

        if (adapter.isFavorite(item)) {
            cardRepository.removeCardFromFavorites(item);
            cardRepository.sendDeleteFavoriteCardRequest(item.getId(), new MyCardDeleteFromFavoriteCallback());
        } else {
            cardRepository.addCardToFavorites(item);
            cardRepository.sendAddFavoriteCardRequest(item.getId(), new MyCardAddToFavoriteCallback());
        }
        final List<CardItem> freshFavoritesCards = cardRepository.getFavoriteCardsFromStorage();
        adapter.setFavorites(freshFavoritesCards);
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

    private class MyCardAddToFavoriteCallback implements CardAddToFavoriteCallback {
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
