package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.BaseEvent;
import com.devtonix.amerricard.utils.CircleTransform;
import com.devtonix.amerricard.utils.LanguageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CalendarAdapterNew extends RecyclerView.Adapter<CalendarAdapterNew.MainHolder> {

    private static final String TAG = CalendarAdapterNew.class.getSimpleName();
    private List<BaseEvent> baseEvents = new ArrayList<>();
    private Comparator<BaseEvent> baseEventComparator;
    private String currLang;
    private Context context;
    private OnCalendarItemClickListener listener;

    public interface OnCalendarItemClickListener {
        void onItemClicked(int position);
    }

    public CalendarAdapterNew(String currLang, Context context, OnCalendarItemClickListener listener) {
        this.currLang = currLang;
        this.context = context;
        this.listener = listener;
        baseEventComparator = new Comparator<BaseEvent>() {
            @Override
            public int compare(BaseEvent o1, BaseEvent o2) {
                return o1.getEventDate().compareTo(o2.getEventDate());
            }
        };
    }

    public void updateAdapter(List<BaseEvent> baseEvents) {
        this.baseEvents = baseEvents;
        Collections.sort(baseEvents, baseEventComparator);
        notifyDataSetChanged();
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_calendar_item, parent, false);
        return new MainHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {

        final BaseEvent baseEvent = baseEvents.get(position);

        switch (baseEvent.getEventType()){
            case BaseEvent.TYPE_EVENT:
                Log.d(TAG, "TYPE_EVENT");
                holder.text.setText(LanguageUtils.convertLang(baseEvent.getEventName(), currLang));
                break;
            case BaseEvent.TYPE_CONTACT:
                Log.d(TAG, "TYPE_CONTACT");
                holder.text.setText(baseEvent.getEventName().getBaseName());
                break;
            case BaseEvent.TYPE_CELEBRITY:
                Log.d(TAG, "TYPE_CELEBRITY");
                holder.text.setText(baseEvent.getEventName().getBaseName());
                break;
        }

        Log.i("loadPicture", TAG + " onBindViewHolder()  Glide");

        Glide.with(context)
                .load(baseEvent.getEventImage())
                .error(R.drawable.ic_no_avatar)
                .transform(new CircleTransform(context))
                .into(holder.icon);

        holder.subtext.setText(baseEvent.getEventDate());
    }

    @Override
    public int getItemCount() {
        return baseEvents.size();
    }

    static final class MainHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView subtext;
        ImageView icon;

        public MainHolder(View itemView, final OnCalendarItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });
            text = (TextView) itemView.findViewById(R.id.card_text);
            subtext = (TextView) itemView.findViewById(R.id.card_sub_text);
            icon = (ImageView) itemView.findViewById(R.id.card_icon);
        }
    }
}
