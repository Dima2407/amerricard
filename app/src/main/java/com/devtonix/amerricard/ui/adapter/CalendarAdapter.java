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
import com.devtonix.amerricard.api.NetworkServiceProvider;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.utils.CircleTransform;
import com.devtonix.amerricard.utils.LanguageUtils;
import com.devtonix.amerricard.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MainHolder> {
    private static final String TAG = CalendarAdapter.class.getSimpleName();

    private List<Object> objects = new ArrayList<>();
    private List<Item> holidays = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();

    private Context context;
    private OnCalendarItemClickListener listener;


    public interface OnCalendarItemClickListener {
        void onItemClicked(int position);
    }

    public CalendarAdapter(Context mContext, OnCalendarItemClickListener listener) {
        this.context = mContext;
        this.listener = listener;
    }

    public void updateData(List<Object> objects) {
        this.objects = objects;

        for (Object o : objects) {
            if (o instanceof Item) {
                holidays.add((Item) o);
            } else if (o instanceof Contact) {
                contacts.add((Contact) o);
            }
        }

        final List<Item> cancelledHolidays = Preferences.getInstance().getEventsForHide();
        final List<Item> copyOfHolidays = new ArrayList<>(holidays.size());
        copyOfHolidays.addAll(holidays);

        for (int i = 0; i < copyOfHolidays.size(); i++) {
            for (int j = 0; j < cancelledHolidays.size(); j++) {
                if (copyOfHolidays.get(i).id == cancelledHolidays.get(j).id) {
                    holidays.remove(copyOfHolidays.get(i));
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public CalendarAdapter.MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_calendar_item, parent, false);
        return new CalendarAdapter.MainHolder(v);
    }

    @Override
    public void onBindViewHolder(final CalendarAdapter.MainHolder holder, int position) {
        Object o = objects.get(position);

        if (o instanceof Item) {
            final Item item = (Item) o;
            final String url = NetworkServiceProvider.BASE_URL + item.getUrlByType() + item.id + "/image?width=100&height=200&type=fit";

            holder.text.setText(LanguageUtils.cardNameWrapper(item.getName()));
            holder.subtext.setVisibility(View.VISIBLE);
            holder.subtext.setText(item.getDate());
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

    public class MainHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView subtext;
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
            subtext = (TextView) itemView.findViewById(R.id.card_sub_text);
            icon = (ImageView) itemView.findViewById(R.id.card_icon);
        }
    }

    private void onSelected(int adapterPosition) {
        if (listener != null) {
            listener.onItemClicked(adapterPosition);
        }
    }
}
