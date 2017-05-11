package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.api.NetworkServiceProvider;
import com.devtonix.amerricard.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MainHolder> {

    private List<Item> items = new ArrayList<>();
    private Context context;
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onItemClicked(int position);
    }

    public CardAdapter(Context mContext, List<Item> items, OnFavoriteClickListener listener) {
        this.context = mContext;
        this.items = items;
        this.listener = listener;
    }

    public void updateData(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public long getIdByPosition(int position) {
        return items.get(position).id;
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_card_item, parent, false);
        return new MainHolder(v);
    }

    @Override
    public void onBindViewHolder(final MainHolder holder, int position) {
        Item item = items.get(position);
        holder.text.setText(item.name == null ? "" : item.name);

        String url = NetworkServiceProvider.BASE_URL + NetworkServiceProvider.CATEGORY_SUFFIX + item.id + "/image?width=100&height=200&type=fit";

        Glide.with(context).load(url)
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder  {

        TextView text;
        ImageView icon;

        public MainHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(getAdapterPosition());
                }
            });
            text = (TextView) itemView.findViewById(R.id.card_text);
            icon = (ImageView) itemView.findViewById(R.id.card_icon);
        }
    }

    private void onSelected(int adapterPosition) {
        if (listener != null) {
            listener.onItemClicked(adapterPosition);
        }
    }
}