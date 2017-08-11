package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.BaseEvent;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.utils.CircleTransform;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarAdapterNew extends RecyclerView.Adapter<CalendarAdapterNew.MainHolder> {

    private static final String TAG = CalendarAdapterNew.class.getSimpleName();
    private static final int ICON_SIZE = 56;
    private List<BaseEvent> baseEvents = new ArrayList<>();
    private Comparator<BaseEvent> baseEventComparator;
    private String currLang;
    private Context context;
    private OnCalendarItemClickListener listener;
    private Handler uiHandler = new Handler();
    private SharedHelper sharedHelper;
    private int nearestPosition = -1;
    private Date currentDate = GregorianCalendar.getInstance().getTime();

    public BaseEvent getItem(int position) {
        return baseEvents.get(position);
    }

    public interface OnCalendarItemClickListener {
        void onItemClicked(int position);
    }

    public CalendarAdapterNew(String currLang, Context context, OnCalendarItemClickListener listener, SharedHelper sharedHelper) {
        this.currLang = currLang;
        this.context = context;
        this.listener = listener;
        this.sharedHelper = sharedHelper;
        baseEventComparator = new Comparator<BaseEvent>() {
            @Override
            public int compare(BaseEvent o1, BaseEvent o2) {
                return o1.getEventDate().compareTo(o2.getEventDate());
            }
        };
    }

    public int getNearestDatePosition() {
        if (nearestPosition != -1) {
            return nearestPosition;
        }
        nearestPosition = 0;
        for (int i = 0; i < baseEvents.size(); i++) {
            if (baseEvents.get(i).getEventDate() != null) {
                String[] dateStrs = baseEvents.get(i).getEventDate().split("[.]");
                Date date = new Date(currentDate.getYear(), Integer.valueOf(dateStrs[0]) - 1, Integer.valueOf(dateStrs[1]));
                if (date.getTime() > currentDate.getTime()) {
                    nearestPosition = i;
                    break;
                }
            }
        }
        return nearestPosition;
    }

    private boolean isEquals(List<BaseEvent> baseEvents) {
        List<BaseEvent> eventsWithoutReclam = new ArrayList<>();
        for (BaseEvent baseEvent : this.baseEvents) {
            if (baseEvent.getEventDate() != null) {
                eventsWithoutReclam.add(baseEvent);
            }
        }
        if (eventsWithoutReclam.size() != baseEvents.size()) {
            return false;
        }

        for (int i = 0; i < eventsWithoutReclam.size(); i++) {
            if (!eventsWithoutReclam.get(i).equals(baseEvents.get(i))) {
                return false;
            }
        }

        return true;
    }

    public void updateAdapter(List<BaseEvent> baseEvents) {
        nearestPosition = -1;

        Collections.sort(baseEvents, baseEventComparator);
        if (isEquals(baseEvents)) {
            return;
        }

        this.baseEvents.clear();
        this.baseEvents.addAll(baseEvents);
        nearestPosition = getNearestDatePosition();
        if (!sharedHelper.isVipOrPremium() && !this.baseEvents.isEmpty()) {
            this.baseEvents.add(nearestPosition, BaseEvent.EMPTY);
        }

        notifyDataSetChanged();
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_calendar_item, parent, false);
        return new MainHolder(view, listener);
    }


    @Override
    public void onBindViewHolder(final MainHolder holder, int position) {

        final BaseEvent baseEvent = baseEvents.get(position);
        if (baseEvent == BaseEvent.EMPTY) {
            AdRequest adRequest = new AdRequest.Builder().build();
            holder.adView.loadAd(adRequest);
            holder.adView.setVisibility(View.VISIBLE);
            holder.itemContent.setVisibility(View.INVISIBLE);
            return;
        } else {
            holder.adView.setVisibility(View.GONE);
            holder.itemContent.setVisibility(View.VISIBLE);
        }

        holder.text.setText(baseEvent.getName(currLang));
        holder.emptyIconText.setText(baseEvent.getLetters(currLang));
        holder.emptyIconText.setVisibility(View.GONE);

        DrawableTypeRequest<?> request;
        if (baseEvent instanceof Contact) {
            request = Glide.with(context).load(((Contact) baseEvent).getPhotoUri());
        } else {
            request = Glide.with(context).load(baseEvent.getThumbImageUrl());
        }
        request.listener(new RequestListener<Object, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.icon.setImageBitmap(setImageIfEmpty(context.getResources().getColor(baseEvent.getColor()), ICON_SIZE));
                        holder.emptyIconText.setVisibility(View.VISIBLE);
                    }
                }, 300);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                return false;
            }
        })
                .transform(new CircleTransform(context))
                .into(holder.icon);

        holder.subtext.setText(baseEvent.getEventDate());

        String[] dateStrs = baseEvent.getEventDate().split("[.]");
        Date date = new Date(GregorianCalendar.getInstance().getTime().getYear(), Integer.valueOf(dateStrs[0]) - 1, Integer.valueOf(dateStrs[1]));
        if (getMonth(date) == getMonth(currentDate) && getDayOfMonth(date) == getDayOfMonth(currentDate)) {
            holder.iconPresent.setVisibility(View.VISIBLE);
        }
    }

    private int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }


    @Override
    public int getItemCount() {
        return baseEvents.size();
    }

    private Bitmap setImageIfEmpty(int color, int size) {
        if (size <= 0)
            return null;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);
        return bitmap;
    }

    static final class MainHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView subtext;
        ImageView icon;
        TextView emptyIconText;
        View itemContent;
        AdView adView;
        ImageView iconPresent;

        public MainHolder(View itemView, final OnCalendarItemClickListener listener) {
            super(itemView);
            itemContent = itemView.findViewById(R.id.item_content);
            adView = (AdView) itemView.findViewById(R.id.adView);
            itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });
            text = (TextView) itemView.findViewById(R.id.card_text);
            subtext = (TextView) itemView.findViewById(R.id.card_sub_text);
            icon = (ImageView) itemView.findViewById(R.id.card_icon);
            emptyIconText = (TextView) itemView.findViewById(R.id.text_icon_empty);
            iconPresent = (ImageView) itemView.findViewById(R.id.img_celebrate);
        }
    }
}
