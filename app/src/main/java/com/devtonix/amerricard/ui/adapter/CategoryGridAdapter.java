package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.storage.SharedHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.MainHolder> {

    private static final int SIMPLE = 0;
    private static final int ADV = 1;

    private List<CardItem> cards = new ArrayList<>();
    private List<CardItem> favorites = new ArrayList<>();
    private List<CardItem> vipCards = new ArrayList<>();
    private List<CardItem> premiumCards = new ArrayList<>();
    private Context context;
    private OnFavoriteClickListener listener;
    private int reclamPosition;

    private int width;
    private int height;

    public interface OnFavoriteClickListener {
        void onItemClicked(int position);

        void onFavoriteClicked(int position);

        void onVipClicked(int position);

        void onPremiumClicked(int position);
    }

    public CategoryGridAdapter(Context context, List<CardItem> cards, OnFavoriteClickListener listener,
                               int width, int height, List<CardItem> favoriteCards,
                               List<CardItem> vipCards, List<CardItem> premiumCards, SharedHelper sharedHelper) {
        this.context = context;
        this.cards = cards;
        reclamPosition = -1;
        boolean isReclamOnThisPage = false;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getName() == null) {
                isReclamOnThisPage = true;
                reclamPosition = i;
            }
        }
        if (!sharedHelper.isVipOrPremium() && !isReclamOnThisPage) {
            if (!this.cards.isEmpty() && this.cards.size() <= 2) {
                this.cards.add(1, CardItem.EMPTY);
                reclamPosition = 1;
            } else if (this.cards.size() >= 3) {
                if (new Random().nextBoolean()) {
                    this.cards.add(2, CardItem.EMPTY);
                    reclamPosition = 2;
                } else {
                    this.cards.add(3, CardItem.EMPTY);
                    reclamPosition = 3;
                }
            }
        }
        this.listener = listener;
        this.width = width;
        this.height = height;
        this.favorites = favoriteCards;
        this.vipCards = vipCards;
        this.premiumCards = premiumCards;
    }

    public void updateData(List<CardItem> items) {
        this.cards = items;
        notifyDataSetChanged();
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_category_item, parent, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        v.setLayoutParams(params);
        return new MainHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(final MainHolder holder, int position) {
        final CardItem item = cards.get(position);
        if (item == CardItem.EMPTY) {
            final NativeExpressAdView advView = new NativeExpressAdView(context);
            advView.setAdUnitId(context.getString(R.string.banner_ad_unit_id));


            float density = context.getResources().getDisplayMetrics().density;
            int count = context.getResources().getInteger(R.integer.span_count);
            int w = (int) (this.width / count / density) - 10;
            int h = (int) (this.height / density) - 10;
            Log.d("Ads", "onBindViewHolder: " + w + " x " + h);

            advView.setAdSize(new AdSize(w, h));
            advView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            advView.loadAd(adRequest);
            ((ViewGroup) holder.itemView).addView(advView);
            Log.d("Ads", "onBindViewHolder: " + width + " х " + height);
            holder.content.setVisibility(View.INVISIBLE);
            return;
        } else {
            ViewGroup itemView = (ViewGroup) holder.itemView;
            if (itemView.getChildCount() == 2) {
                itemView.getChildAt(1).setVisibility(View.GONE);
            }
            holder.content.setVisibility(View.VISIBLE);
        }

        if (isFavorite(item)) {
            holder.favoriteButton.setVisibility(View.GONE);
            holder.favoriteButtonFull.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteButton.setVisibility(View.VISIBLE);
            holder.favoriteButtonFull.setVisibility(View.GONE);
        }

        holder.ivVip.setVisibility(isVip(item) ? View.VISIBLE : View.GONE);
        holder.ivPremium.setVisibility(isPremium(item) ? View.VISIBLE : View.GONE);

        Glide.with(context).load(item.getThumbImageUrl()).into(holder.icon);


    }

    public boolean isFavorite(CardItem item) {
        for (CardItem cardItem : favorites) {
            if ((int) cardItem.getId() == (int) item.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isVip(CardItem item) {
        for (CardItem cardItem : vipCards) {
            if ((int) cardItem.getId() == (int) item.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPremium(CardItem item) {
        for (CardItem cardItem : premiumCards) {
            if ((int) cardItem.getId() == (int) item.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static final class MainHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        ViewGroup favoriteContainer;
        ImageView favoriteButton;
        ImageView favoriteButtonFull;
        ImageView ivVip;
        ImageView ivPremium;
        View content;

        public MainHolder(View itemView, final OnFavoriteClickListener listener) {
            super(itemView);
            content = itemView.findViewById(R.id.item_content);

            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });
            icon = (ImageView) itemView.findViewById(R.id.category_icon);
            favoriteContainer = (ViewGroup) itemView.findViewById(R.id.category_favorite_container);
            favoriteContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFavoriteClicked(getAdapterPosition());
                }
            });
            favoriteButton = (ImageView) itemView.findViewById(R.id.category_favorite_button);
            favoriteButtonFull = (ImageView) itemView.findViewById(R.id.category_favorite_button_full);

            ivVip = (ImageView) itemView.findViewById(R.id.ivVip);
            ivPremium = (ImageView) itemView.findViewById(R.id.ivPremium);

            ivVip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onVipClicked(getAdapterPosition());
                }
            });

            ivPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPremiumClicked(getAdapterPosition());
                }
            });
        }
    }

    public void setFavorites(List<CardItem> favorites) {
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    public int getReclamPosition() {
        return reclamPosition;
    }
}