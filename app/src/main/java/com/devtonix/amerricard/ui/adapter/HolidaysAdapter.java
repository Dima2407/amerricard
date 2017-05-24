package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.api.NetworkServiceProvider;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.utils.CircleTransform;

import java.util.ArrayList;
import java.util.List;

public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.HolidaysVH> {

    private List<Item> items = new ArrayList<>();
    private Context context;
    private OnSwitchClickListener listener;

    public interface OnSwitchClickListener {
        void onItemClicked(int position);
    }

    public HolidaysAdapter(Context mContext, OnSwitchClickListener listener) {
        this.context = mContext;
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
    public HolidaysVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.holidays_item, parent, false);
        return new HolidaysVH(v);
    }

    @Override
    public void onBindViewHolder(final HolidaysVH holder, int position) {
        final Item item = items.get(position);
        final String url = NetworkServiceProvider.BASE_URL + item.getUrlByType() + item.id + "/image?width=100&height=200&type=fit";

        holder.tvHolidayTitle.setText(item.name == null ? "" : item.name);
        Glide.with(context).load(url).transform(new CircleTransform(context)).into(holder.ivHolidayIcon);
        holder.swHoliday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, "switch", Toast.LENGTH_SHORT).show();
                //нужно записать в шареды массив тех праздников, кторые пользователь отметил как ненужные
                //в CalendarFragment их вытаскивать и вытаскивать список из сервака и сравнивать

                if (!isChecked){

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    final class HolidaysVH extends RecyclerView.ViewHolder {

        TextView tvHolidayTitle;
        ImageView ivHolidayIcon;
        SwitchCompat swHoliday;

        public HolidaysVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(getAdapterPosition());
                }
            });
            tvHolidayTitle = (TextView) itemView.findViewById(R.id.tv_holiday_title);
            ivHolidayIcon = (ImageView) itemView.findViewById(R.id.iv_holiday_icon);
            swHoliday = (SwitchCompat) itemView.findViewById(R.id.sw_holiday);
        }
    }

    private void onSelected(int adapterPosition) {
        if (listener != null) {
            listener.onItemClicked(adapterPosition);
        }
    }
}