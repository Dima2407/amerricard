package com.devtonix.amerricard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.devtonix.amerricard.ui.activity.VipAndPremiumActivity;
import com.devtonix.amerricard.ui.adapter.CategoryGridAdapter;
import com.devtonix.amerricard.ui.callback.CardAddToFavoriteCallback;
import com.devtonix.amerricard.ui.callback.CardDeleteFromFavoriteCallback;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CategoryFragment extends BaseFragment implements CategoryGridAdapter.OnFavoriteClickListener {

    private static final String POSITION_FOR_CARD = "position_for_card";
    private static final String POSITION_FOR_CATEGORY = "position_for_category";
    private static final String CATEGORY_ID = "category_id";
    @Inject
    CardRepository cardRepository;

    private static final String TAG = CategoryFragment.class.getSimpleName();
    public static final int POSITION_NOT_SET = -1;

    private CategoryGridAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private List<CardItem> cards;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    public static CategoryFragment getInstance(int positionForCard, int positionForCategory) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_FOR_CARD, positionForCard);
        bundle.putInt(POSITION_FOR_CATEGORY, positionForCategory);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }

    public static CategoryFragment getInstance(int positionForCard) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_FOR_CARD, positionForCard);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }

    public static CategoryFragment getInstanceForCategoryId(int categoryId) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CATEGORY_ID, categoryId);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.card_empty_text);

        final int countRow = getResources().getInteger(R.integer.span_count);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), countRow));
        recyclerView.post(new Runnable() {
            @Override
            public void run() {

                cards = ((CategoryActivity) getActivity()).getCategories(getArguments().getInt(POSITION_FOR_CARD));

                try {
                    if (cards.size() != 0) {
                        manageVisible(true);

                        int width;
                        int height;

                        if (recyclerView.getWidth() > recyclerView.getHeight()) {
                            width = (recyclerView.getWidth()) / 4;
                        } else {
                            width = (recyclerView.getWidth()) / 2;
                        }

                        height = (int) (width * 1.6);

                        final List<CardItem> favoriteCards = cardRepository.getFavoriteCardsFromStorage();

                        final List<CardItem> vipCards = new ArrayList<CardItem>();
                        for (CardItem card : cards) {
                            if (TextUtils.equals(card.getCardType(), "VIP")) {
                                vipCards.add(card);
                            }
                        }

                        final List<CardItem> premiumCards = new ArrayList<CardItem>();
                        for (CardItem card : cards) {
                            if (TextUtils.equals(card.getCardType(), "PREMIUM")) {
                                premiumCards.add(card);
                            }
                        }

                        adapter = new CategoryGridAdapter(
                                getActivity(),
                                cards,
                                CategoryFragment.this,
                                recyclerView.getWidth(),
                                height,
                                favoriteCards,
                                vipCards,
                                premiumCards,
                                sharedHelper);

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
        int realPosition = pos;
        if (adapter.getReclamPosition() >= 0 && pos > adapter.getReclamPosition()) {
            realPosition--;
        }
        if (getArguments().getInt(CATEGORY_ID, -1) != -1) {
            //show selected card from CALENDAR tab (click on some event)
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putParcelableArrayListExtra(DetailActivity.PARCELABLE_CARDS, new ArrayList<>(cards));
            intent.putExtra(DetailActivity.POSITION_FOR_CARD_FROM_EVENT_SCREEN, realPosition);
            intent.setAction(DetailActivity.ACTION_SHOW_CARD_FROM_EVENT_SCREEN);
            startActivity(intent);
        } else {
            //show selected card from CARDS tab
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(DetailActivity.POSITION_FOR_CURRENT_CARD, realPosition);
            intent.putExtra(DetailActivity.POSITION_FOR_CARD, getArguments().getInt(POSITION_FOR_CARD));
            intent.putExtra(DetailActivity.POSITION_FOR_CATEGORY, getArguments().getInt(POSITION_FOR_CATEGORY));
            startActivity(intent);
        }
    }

    @Override
    public void onFavoriteClicked(int position) {

        progressDialog.show();
        final CardItem item = cards.get(position);

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

    @Override
    public void onVipClicked(int position) {
        loadVipScreen();
    }

    @Override
    public void onPremiumClicked(int position) {
        loadVipScreen();
    }

    private void loadVipScreen() {
        Intent intent = new Intent(getActivity(), VipAndPremiumActivity.class);
        intent.setAction(VipAndPremiumActivity.SHOW_VIP_ACTION);
        startActivity(intent);
        getActivity().finish();
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
