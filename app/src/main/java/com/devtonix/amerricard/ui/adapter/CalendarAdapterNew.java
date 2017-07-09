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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.BaseEvent;
import com.devtonix.amerricard.utils.CircleTransform;
import com.devtonix.amerricard.utils.LanguageUtils;

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

    public int getNearestDatePosition() {
        int position = 0;
        for (int i = 0; i < baseEvents.size(); i++) {
            String[] dateStrs = baseEvents.get(i).getEventDate().split("[.]");
            Date date = new Date(GregorianCalendar.getInstance().getTime().getYear(), Integer.valueOf(dateStrs[0]) - 1, Integer.valueOf(dateStrs[1]));
            if (date.getTime() > GregorianCalendar.getInstance().getTime().getTime()) {
                position = i;
                break;
            }
        }
        return position;
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

    private String firstLeters(CharSequence name) {
        if (TextUtils.isEmpty(name))
            return null;
        String strs[] = name.toString().split(" ");
        if (strs.length > 1) {
            return strs[0].substring(0, 1).concat(strs[1].substring(0, 1)).toUpperCase();
        } else
            return name.toString().substring(0, 2).toUpperCase();
    }

    @Override
    public void onBindViewHolder(final MainHolder holder, int position) {

        final BaseEvent baseEvent = baseEvents.get(position);

        switch (baseEvent.getEventType()) {
            case BaseEvent.TYPE_EVENT:
                Log.d(TAG, "TYPE_EVENT");
                holder.text.setText(LanguageUtils.convertLang(baseEvent.getEventName(), currLang));
                holder.emptyIconText.setText(firstLeters(holder.text.getText()));
                break;
            case BaseEvent.TYPE_CONTACT:
                Log.d(TAG, "TYPE_CONTACT");
                holder.text.setText(baseEvent.getEventName().getBaseName());
                holder.emptyIconText.setText(firstLeters(holder.text.getText()));
                break;
            case BaseEvent.TYPE_CELEBRITY:
                Log.d(TAG, "TYPE_CELEBRITY");
                holder.text.setText(baseEvent.getEventName().getBaseName());
                holder.emptyIconText.setText(firstLeters(holder.text.getText()));
                break;
        }

        Log.i("loadPicture", TAG + " onBindViewHolder()  Glide");

        Glide.with(context)
                .load(baseEvent.getEventImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
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
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

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
            emptyIconText = (TextView) itemView.findViewById(R.id.text_icon_empty);
        }
    }
}
