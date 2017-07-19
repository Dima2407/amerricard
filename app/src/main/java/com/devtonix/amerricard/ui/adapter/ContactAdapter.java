package com.devtonix.amerricard.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    private static final int ICON_SIZE = 56;
    private Handler uiHandler = new Handler();

    public interface OnSwitchClickListener {
        void onItemClicked(int position);
        void onSwitchPositionChanged(int position, boolean isPositivePosition);
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
    public void onBindViewHolder(final HolidaysVH holder, final int position) {
        final Contact contact = contacts.get(position);

        holder.tvContactTitle.setText(contact.getName());
        holder.subText.setText(contact.getBirthday());
        holder.swContact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (holder.swContact.isChecked()) {
                        holder.swContact.setChecked(false);
                        contact.setCancelled(false);
                        listener.onSwitchPositionChanged(position, false);

                    } else {
                        holder.swContact.setChecked(true);
                        contact.setCancelled(true);
                        listener.onSwitchPositionChanged(position, true);
                    }
                }
                return false;
            }
        });
        holder.emptyIconText.setText(contact.getLetters());
        holder.emptyIconText.setVisibility(View.GONE);
        holder.swContact.setChecked(!contact.isCancelled());

        Glide.with(context).load(contact.getPhotoUri())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        uiHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                holder.ivContactIcon.setImageBitmap(setImageIfEmpty(context.getResources().getColor(contact.getColor()), ICON_SIZE));
                                holder.emptyIconText.setVisibility(View.VISIBLE);
                            }
                        }, 300);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                }).transform(new CircleTransform(context)).into(holder.ivContactIcon);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
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

    final class HolidaysVH extends RecyclerView.ViewHolder {

        TextView tvContactTitle;
        TextView subText;
        ImageView ivContactIcon;
        SwitchCompat swContact;
        TextView emptyIconText;

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
            emptyIconText = (TextView) itemView.findViewById(R.id.text_icon_empty);
        }
    }
}