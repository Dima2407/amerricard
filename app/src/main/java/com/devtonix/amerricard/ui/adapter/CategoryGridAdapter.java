package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.network.NetworkModule;

import java.util.ArrayList;
import java.util.List;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.MainHolder> {

    private List<CardItem> cards = new ArrayList<>();
    private List<CardItem> favorites = new ArrayList<>();
    private List<CardItem> vipCards = new ArrayList<>();
    private List<CardItem> premiumCards = new ArrayList<>();
    private Context context;
    private OnFavoriteClickListener listener;

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
                               List<CardItem> vipCards, List<CardItem> premiumCards) {
        this.context = context;
        this.cards = cards;
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

        if (isFavorite(item)) {
            holder.favoriteButton.setVisibility(View.GONE);
            holder.favoriteButtonFull.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteButton.setVisibility(View.VISIBLE);
            holder.favoriteButtonFull.setVisibility(View.GONE);
        }

        holder.ivVip.setVisibility(isVip(item) ? View.VISIBLE : View.GONE);
        holder.ivPremium.setVisibility(isPremium(item) ? View.VISIBLE : View.GONE);

        final String url = NetworkModule.BASE_URL + item.getType() + "/" + item.getId() + "/image?width=" + width + "&height=" + height + "&type=fit";

        Glide.with(context).load(url).into(holder.icon);
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

        public MainHolder(View itemView, final OnFavoriteClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
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
}