package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.utils.CircleTransform;
import com.devtonix.amerricard.utils.LanguageUtils;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MainHolder> {
    private static final String TAG = CalendarAdapter.class.getSimpleName();

    private List<Object> objects = new ArrayList<>();
    private List<EventItem> holidays = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();

    private String currLang;
    private Context context;
    private OnCalendarItemClickListener listener;


    public interface OnCalendarItemClickListener {
        void onItemClicked(int position);
    }

    public CalendarAdapter(Context mContext, String language, OnCalendarItemClickListener listener) {
        this.context = mContext;
        this.currLang = language;
        this.listener = listener;
    }

    public void updateData(List<Object> objects, List<EventItem> eventForHide) {
        this.objects = objects;

        for (Object o : objects) {
            if (o instanceof EventItem) {
                holidays.add((EventItem) o);
            } else if (o instanceof Contact) {
                contacts.add((Contact) o);
            }
        }

        final List<EventItem> copyOfHolidays = new ArrayList<>(holidays.size());
        copyOfHolidays.addAll(holidays);

        for (int i = 0; i < copyOfHolidays.size(); i++) {
            for (int j = 0; j < eventForHide.size(); j++) {
                if ((int) copyOfHolidays.get(i).getId() == (int) eventForHide.get(j).getId()) {
                    holidays.remove(copyOfHolidays.get(i));
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public CalendarAdapter.MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_calendar_item, parent, false);
        return new CalendarAdapter.MainHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(final CalendarAdapter.MainHolder holder, int position) {
        Object o = objects.get(position);

        if (o instanceof EventItem) {
            final EventItem event = (EventItem) o;
            final String url = NetworkModule.BASE_URL + "event/" + event.getId() + "/image?width=100&height=200&type=fit";

            holder.text.setText(LanguageUtils.convertLang(event.getName(), currLang));
            holder.subtext.setVisibility(View.VISIBLE);
            holder.subtext.setText(event.getFormattedDate());
            Glide.with(context)
                    .load(url)
                    .transform(new CircleTransform(context))
                    .into(holder.icon);

        } else if (o instanceof Contact) {
            final Contact contact = (Contact) o;

            holder.text.setText(contact.getName());
            holder.subtext.setText(contact.getBirthday());
            holder.subtext.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(contact.getPhotoUri())
                    .transform(new CircleTransform(context))
                    .error(Color.BLUE)
                    .into(holder.icon);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
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
