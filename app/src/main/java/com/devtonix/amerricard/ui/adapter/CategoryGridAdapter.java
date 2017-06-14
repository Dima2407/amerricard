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
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.utils.FavoriteUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryGridAdapter/* extends RecyclerView.Adapter<CategoryGridAdapter.MainHolder>*/ {

//    private List<Item> items = new ArrayList<>();
//    private Context context;
//    private OnFavoriteClickListener listener;
//    private List<Item> favorites;
//
//    private int width;
//    private int height;
//
//    public interface OnFavoriteClickListener {
//        void onItemClicked(int position);
//        void onFavoriteClicked(int position);
//    }
//
//    public CategoryGridAdapter(Context context, List<Item> items, OnFavoriteClickListener listener, int width, int height) {
//        this.context = context;
//        this.items = items;
//        this.listener = listener;
//        this.width = width;
//        this.height = height;
//        favorites = FavoriteUtils.getFavorites();
//    }
//
//    public void updateData(List<Item> items) {
//        this.items = items;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.view_category_item, parent, false);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
//        v.setLayoutParams(params);
//        return new MainHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final MainHolder holder, int position) {
//        Item item = items.get(position);
//
//        if (isFavorite(item)) {
//            holder.favoriteButton.setVisibility(View.GONE);
//            holder.favoriteButtonFull.setVisibility(View.VISIBLE);
//            holder.favoriteContainer.setBackgroundResource(R.drawable.shape_white_circle);
//        } else {
//            holder.favoriteButton.setVisibility(View.VISIBLE);
//            holder.favoriteButtonFull.setVisibility(View.GONE);
//            holder.favoriteContainer.setBackgroundResource(R.drawable.shape_red_circle);
//        }
//        String url = NetworkModule.BASE_URL
//                + (item.getUrlByType())
//                + item.id + "/image?width="+width+"&height="+height+"&type=fit";
//
//        Glide.with(context).load(url)
//                .into(holder.icon);
//    }
//
//    private boolean isFavorite(Item item) {
//        for (Item i: favorites) {
//            if (i.id==item.id) {
//                return true;
//            }
//
//        }
//        return false;
//    }
//
//    private void manageFavorites(Item item) {
//        if (isFavorite(item)) {
//            FavoriteUtils.removeFromFavorites(item);
//        } else {
//            FavoriteUtils.addToFavorites(item);
//        }
//        favorites = FavoriteUtils.getFavorites();
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class MainHolder extends RecyclerView.ViewHolder  {
//
//        ImageView icon;
//        ViewGroup favoriteContainer;
//        ImageView favoriteButton;
//        ImageView favoriteButtonFull;
//
//        public MainHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onSelected(getAdapterPosition());
//                }
//            });
//            icon = (ImageView) itemView.findViewById(R.id.category_icon);
//            favoriteContainer = (ViewGroup) itemView.findViewById(R.id.category_favorite_container);
//            favoriteContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onFavoriteSelected(getAdapterPosition());
//                }
//            });
//            favoriteButton = (ImageView) itemView.findViewById(R.id.category_favorite_button);
//            favoriteButtonFull = (ImageView) itemView.findViewById(R.id.category_favorite_button_full);
//        }
//    }
//
//    private void onSelected(int adapterPosition) {
//        if (listener != null) {
//            listener.onItemClicked(adapterPosition);
//        }
//    }
//
//    private void onFavoriteSelected(int adapterPosition) {
//        manageFavorites(items.get(adapterPosition));
//        if (listener != null) {
//            listener.onFavoriteClicked(adapterPosition);
//        }
//    }
}