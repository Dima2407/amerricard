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

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.utils.CircleTransform;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.HolidaysVH> {

    private static final String TAG = ContactAdapter.class.getSimpleName();
    private List<Contact> contacts = new ArrayList<>();
    private Context context;
    private OnSwitchClickListener listener;

    public interface OnSwitchClickListener {
        void onItemClicked(int position);
    }

    public ContactAdapter(Context mContext, OnSwitchClickListener listener) {
        this.context = mContext;
        this.listener = listener;
    }

    public void updateContacts(List<Contact> contacts) {
        this.contacts = contacts;

        notifyDataSetChanged();
    }

    @Override
    public HolidaysVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_holiday_item, parent, false);
        return new HolidaysVH(v);
    }

    @Override
    public void onBindViewHolder(final HolidaysVH holder, int position) {
        final Contact contact = contacts.get(position);

        holder.tvContactTitle.setText(contact.getName());
        holder.subText.setText(contact.getBirthday());
        holder.swContact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (holder.swContact.isChecked()) {
                        Log.d(TAG, "onTouch:  contact.setCancelled(false)");
                        holder.swContact.setChecked(false);
                        contact.setCancelled(false);
                    } else {
                        Log.d(TAG, "onTouch:  contact.setCancelled(true)");
                        holder.swContact.setChecked(true);
                        contact.setCancelled(true);
                    }
                }
                return false;
            }
        });
        holder.swContact.setChecked(contact.isCancelled());

        Glide.with(context).load(contact.getPhotoUri()).error(R.drawable.ic_no_avatar).transform(new CircleTransform(context)).into(holder.ivContactIcon);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    final class HolidaysVH extends RecyclerView.ViewHolder {

        TextView tvContactTitle;
        TextView subText;
        ImageView ivContactIcon;
        SwitchCompat swContact;

        public HolidaysVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });
            tvContactTitle = (TextView) itemView.findViewById(R.id.tv_contact_title);
            ivContactIcon = (ImageView) itemView.findViewById(R.id.iv_contact_icon);
            swContact = (SwitchCompat) itemView.findViewById(R.id.sw_contact);
            subText = (TextView) itemView.findViewById(R.id.item_holiday_sub_text);
        }
    }
}