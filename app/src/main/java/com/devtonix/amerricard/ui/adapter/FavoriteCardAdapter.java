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

public class FavoriteCardAdapter extends RecyclerView.Adapter<FavoriteCardAdapter.MainHolder> {

    private List<CardItem> items = new ArrayList<>();
    private OnFavoriteClickListener listener;
    private Context context;

    private int width;
    private int height;

    public interface OnFavoriteClickListener {
        void onItemClicked(int position);

        void onFavoriteClicked(int position, CardItem cardItem);
    }

    public FavoriteCardAdapter(
            Context context,
            List<CardItem> items,
            OnFavoriteClickListener listener,
            int width, int height) {

        this.context = context;
        this.items = items;
        this.listener = listener;
        this.width = width;
        this.height = height;
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

        final String url = NetworkModule.BASE_URL + item.getType() + "/" + item.getId() + "/image?width=" + width + "&height=" + height + "&type=fit";

        Glide.with(context).load(url).into(holder.icon);
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