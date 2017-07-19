package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.storage.SharedHelper;

import java.util.ArrayList;
import java.util.List;

import static com.devtonix.amerricard.ui.activity.DetailActivity.TYPE_PREMIUM;
import static com.devtonix.amerricard.ui.activity.DetailActivity.TYPE_VIP;

public class FavoriteCardAdapter extends RecyclerView.Adapter<FavoriteCardAdapter.MainHolder> {

    private List<CardItem> items = new ArrayList<>();
    private OnFavoriteClickListener listener;
    private Context context;

    private int width;
    private int height;

    private int displayWidth;

    public interface OnFavoriteClickListener {
        void onItemClicked(int position);

        void onFavoriteClicked(int position, CardItem cardItem);
    }

    public FavoriteCardAdapter(
            Context context,
            List<CardItem> items,
            OnFavoriteClickListener listener,
            int width, int height,
            SharedHelper sharedHelper) {

        this.context = context;
        this.items = items;
        this.listener = listener;
        this.width = width;
        this.height = height;
        this.displayWidth = sharedHelper.getDisplayWidth();
    }

    public void setItems(List<CardItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_category_item, parent, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        v.setLayoutParams(params);
        return new MainHolder(v);
    }

    @Override
    public void onBindViewHolder(final MainHolder holder, int position) {
        final CardItem item = items.get(position);

        holder.favoriteButton.setVisibility(View.GONE);
        holder.favoriteButtonFull.setVisibility(View.VISIBLE);
        holder.favoriteContainer.setBackgroundResource(R.drawable.shape_white_circle);

        if (TextUtils.equals(item.getCardType(), TYPE_VIP)) {
            holder.ivVip.setVisibility(View.VISIBLE);
            holder.ivPremium.setVisibility(View.GONE);
        } else if (TextUtils.equals(item.getCardType(), TYPE_PREMIUM)) {
            holder.ivVip.setVisibility(View.GONE);
            holder.ivPremium.setVisibility(View.VISIBLE);
        } else {
            holder.ivVip.setVisibility(View.GONE);
            holder.ivPremium.setVisibility(View.GONE);
        }

        Glide.with(context).load(item.getThumbImageUrl(displayWidth / 2)).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        ViewGroup favoriteContainer;
        ImageView favoriteButton;
        ImageView favoriteButtonFull;
        ImageView ivVip;
        ImageView ivPremium;

        public MainHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(getAdapterPosition());
                }
            });
            icon = (ImageView) itemView.findViewById(R.id.category_icon);
            favoriteContainer = (ViewGroup) itemView.findViewById(R.id.category_favorite_container);
            favoriteContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFavoriteSelected(getAdapterPosition());
                }
            });
            favoriteButton = (ImageView) itemView.findViewById(R.id.category_favorite_button);
            favoriteButtonFull = (ImageView) itemView.findViewById(R.id.category_favorite_button_full);
            ivVip = (ImageView) itemView.findViewById(R.id.ivVip);
            ivPremium = (ImageView) itemView.findViewById(R.id.ivPremium);
        }
    }

    private void onSelected(int adapterPosition) {
        if (listener != null) {
            listener.onItemClicked(adapterPosition);
        }
    }

    private void onFavoriteSelected(int adapterPosition) {
        if (listener != null) {
            listener.onFavoriteClicked(adapterPosition, items.get(adapterPosition));
        }
    }
}