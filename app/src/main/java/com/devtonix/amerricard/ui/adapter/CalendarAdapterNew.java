package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.BaseEvent;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.utils.CircleTransform;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

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
        if(nearestPosition != -1){
            return nearestPosition;
        }
        nearestPosition = 0;
        for (int i = 0; i < baseEvents.size(); i++) {
            String[] dateStrs = baseEvents.get(i).getEventDate().split("[.]");
            Date date = new Date(GregorianCalendar.getInstance().getTime().getYear(), Integer.valueOf(dateStrs[0]) - 1, Integer.valueOf(dateStrs[1]));
            if (date.getTime() > GregorianCalendar.getInstance().getTime().getTime()) {
                nearestPosition = i;
                break;
            }
        }
        return nearestPosition;
    }

    public void updateAdapter(List<BaseEvent> baseEvents) {
        nearestPosition = -1;
        this.baseEvents.clear();
        this.baseEvents.addAll(baseEvents);
        Collections.sort(this.baseEvents, baseEventComparator);
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
        if(baseEvent == BaseEvent.EMPTY){
            AdRequest adRequest = new AdRequest.Builder().build();
            holder.adView.loadAd(adRequest);
            holder.adView.setVisibility(View.VISIBLE);
            holder.itemContent.setVisibility(View.INVISIBLE);
            return;
        }else {
            holder.adView.setVisibility(View.GONE);
            holder.itemContent.setVisibility(View.VISIBLE);
        }

        holder.text.setText(baseEvent.getName(currLang));
        holder.emptyIconText.setText(baseEvent.getLetters(currLang));
        holder.emptyIconText.setVisibility(View.GONE);

        Log.i("loadPicture", TAG + " onBindViewHolder()  Glide");

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
                        holder.icon.setImageBitmap(setImageIfEmpty(ICON_SIZE));
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
    }


    @Override
    public int getItemCount() {
        return baseEvents.size();
    }

    private Bitmap setImageIfEmpty(int size) {
        if (size <= 0)
            return null;
        int color = getRandomColor();
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);
        return bitmap;
    }

    private int getRandomColor() {
        Random rand = new Random();
        int numberOfColor = rand.nextInt(9);
        int color = 0;
        switch (numberOfColor) {
            case 0:
                color = Color.YELLOW;
                break;
            case 1:
                color = Color.DKGRAY;
                break;
            case 2:
                color = Color.GRAY;
                break;
            case 3:
                color = Color.LTGRAY;
                break;
            case 4:
                color = Color.RED;
                break;
            case 5:
                color = Color.GREEN;
                break;
            case 6:
                color = Color.BLUE;
                break;
            case 7:
                color = Color.CYAN;
                break;
            case 8:
                color = Color.MAGENTA;
                break;
        }
        return color;
    }

    static final class MainHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView subtext;
        ImageView icon;
        TextView emptyIconText;
        View itemContent;
        AdView adView;

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
        }
    }
}
