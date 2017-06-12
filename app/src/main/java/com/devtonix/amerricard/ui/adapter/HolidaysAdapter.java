package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.api.NetworkServiceProvider;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.utils.CircleTransform;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.devtonix.amerricard.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.HolidaysVH> {

    private static final String TAG = HolidaysAdapter.class.getSimpleName();
    private List<Item> items = new ArrayList<>();
    private Context context;
    private OnSwitchClickListener listener;
    private List<Item> cancelledHolidays = new ArrayList<>();
    private List<Long> cancelledIds = new ArrayList<>();

    public interface OnSwitchClickListener {
        void onItemClicked(int position);
    }

    public HolidaysAdapter(Context mContext, OnSwitchClickListener listener) {
        this.context = mContext;
        this.listener = listener;
    }

    public void updateData(List<Item> items) {
        this.items = items;

        cancelledHolidays = Preferences.getInstance().getEventsForHide();

        if (cancelledHolidays != null && cancelledHolidays.size() == 0) {
            cancelledIds.clear();
        }

        if (items != null && items.size() != 0) {
            for (int i = 0; i < items.size(); i++) {
                for (int j = 0; j < cancelledHolidays.size(); j++) {
                    if (items.get(i).id == cancelledHolidays.get(j).id) {
                        cancelledIds.add(items.get(i).id);
                    }
                }
            }
        }

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

        holder.tvHolidayTitle.setText(LanguageUtils.getCardNameAccordingLang(item.name));

        Glide.with(context).load(url).transform(new CircleTransform(context)).into(holder.ivHolidayIcon);
        holder.swHoliday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (holder.swHoliday.isChecked()) {
                        holder.swHoliday.setChecked(false);

                        if (!cancelledHolidays.contains(item)) {
                            cancelledHolidays.add(item);
                        }

                    } else {
                        holder.swHoliday.setChecked(true);

                        cancelledHolidays.remove(item);
                    }

                    for (int i = 0; i < cancelledHolidays.size(); i++) {
                        Log.d(TAG, "onTouch: i=" + i + " item=" + cancelledHolidays.get(i).name);
                    }

                    Preferences.getInstance().saveEventsForHide(cancelledHolidays);
                }

                return false;
            }
        });

        for (int i = 0; i < cancelledIds.size(); i++) {
            if (item.id == cancelledIds.get(i)) {
                holder.swHoliday.setChecked(false);
            }
        }
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
                    listener.onItemClicked(getAdapterPosition());
                }
            });
            tvHolidayTitle = (TextView) itemView.findViewById(R.id.tv_holiday_title);
            ivHolidayIcon = (ImageView) itemView.findViewById(R.id.iv_holiday_icon);
            swHoliday = (SwitchCompat) itemView.findViewById(R.id.sw_holiday);
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Item> getCancelledHolidays() {
        return cancelledHolidays;
    }
}