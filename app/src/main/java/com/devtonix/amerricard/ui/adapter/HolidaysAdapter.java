package com.devtonix.amerricard.ui.adapter;

public class HolidaysAdapter /*extends RecyclerView.Adapter<HolidaysAdapter.HolidaysVH>*/ {

//    private static final String TAG = HolidaysAdapter.class.getSimpleName();
//    private List<Item> items = new ArrayList<>();
//    private Context context;
//    private OnSwitchClickListener listener;
//    private List<Item> cancelledHolidays = new ArrayList<>();
//    private List<Long> cancelledIds = new ArrayList<>();
//
//    public interface OnSwitchClickListener {
//        void onItemClicked(int position);
//    }
//
//    public HolidaysAdapter(Context mContext, OnSwitchClickListener listener) {
//        this.context = mContext;
//        this.listener = listener;
//    }
//
//    public void updateEvents(List<Item> items) {
//        this.items = items;
//
//        cancelledHolidays = SharedHelper.getInstance().getEventsForHide();
//
//        if (cancelledHolidays != null && cancelledHolidays.size() == 0) {
//            cancelledIds.clear();
//        }
//
//        if (items != null && items.size() != 0) {
//            for (int i = 0; i < items.size(); i++) {
//                for (int j = 0; j < cancelledHolidays.size(); j++) {
//                    if (items.get(i).id == cancelledHolidays.get(j).id) {
//                        cancelledIds.add(items.get(i).id);
//                    }
//                }
//            }
//        }
//
//        notifyDataSetChanged();
//    }
//
//    public long getIdByPosition(int position) {
//        return items.get(position).id;
//    }
//
//    @Override
//    public HolidaysVH onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.holidays_item, parent, false);
//        return new HolidaysVH(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final HolidaysVH holder, int position) {
//        final Item item = items.get(position);
//        final String url = NetworkModule.BASE_URL + item.getUrlByType() + item.id + "/image?width=100&height=200&type=fit";
//
//        holder.tvHolidayTitle.setText(LanguageUtils.cardNameWrapper(item.getNameJsonEl()));
//
//        Glide.with(context).load(url).transform(new CircleTransform(context)).into(holder.ivHolidayIcon);
//        holder.swHoliday.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    if (holder.swHoliday.isChecked()) {
//                        holder.swHoliday.setChecked(false);
//
//                        if (!cancelledHolidays.contains(item)) {
//                            cancelledHolidays.add(item);
//                        }
//
//                    } else {
//                        holder.swHoliday.setChecked(true);
//
//                        cancelledHolidays.remove(item);
//                    }
//
//                    for (int i = 0; i < cancelledHolidays.size(); i++) {
//                        Log.d(TAG, "onTouch: i=" + i + " item=" + cancelledHolidays.get(i).getNameJsonEl());
//                    }
//
//                    SharedHelper.getInstance().saveEventsForHide(cancelledHolidays);
//                }
//
//                return false;
//            }
//        });
//
//        for (int i = 0; i < cancelledIds.size(); i++) {
//            if (item.id == cancelledIds.get(i)) {
//                holder.swHoliday.setChecked(false);
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    final class HolidaysVH extends RecyclerView.ViewHolder {
//
//        TextView tvHolidayTitle;
//        ImageView ivHolidayIcon;
//        SwitchCompat swHoliday;
//
//        public HolidaysVH(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClicked(getAdapterPosition());
//                }
//            });
//            tvHolidayTitle = (TextView) itemView.findViewById(R.id.tv_holiday_title);
//            ivHolidayIcon = (ImageView) itemView.findViewById(R.id.iv_holiday_icon);
//            swHoliday = (SwitchCompat) itemView.findViewById(R.id.sw_holiday);
//        }
//    }
//
//    public List<Item> getItems() {
//        return items;
//    }
//
//    public List<Item> getCancelledHolidays() {
//        return cancelledHolidays;
//    }
}