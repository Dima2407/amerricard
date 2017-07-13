package com.devtonix.amerricard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.ui.activity.DetailActivity;

import static com.devtonix.amerricard.ui.activity.DetailActivity.TYPE_PREMIUM;
import static com.devtonix.amerricard.ui.activity.DetailActivity.TYPE_VIP;

public class DetailFragment extends BaseFragment {

    private ViewGroup detailContainer;
    private ImageView image;
    private ImageView ivVip;
    private ImageView ivPremium;

    public static DetailFragment getInstance(CardItem item, boolean isFullScreen) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle b = new Bundle();
        b.putParcelable("card", item);
        b.putBoolean("fullscreen", isFullScreen);
        b.putBoolean("isVip", TextUtils.equals(item.getCardType(), TYPE_VIP));
        b.putBoolean("isPremium", TextUtils.equals(item.getCardType(), TYPE_PREMIUM));
        detailFragment.setArguments(b);
        return detailFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);

        detailContainer = (ViewGroup) view.findViewById(R.id.detail_container);

        image = (ImageView) view.findViewById(R.id.detail_image);
        ivVip = (ImageView) view.findViewById(R.id.ivVip);
        ivPremium = (ImageView) view.findViewById(R.id.ivPremium);

        final CardItem item = getArguments().getParcelable("card");

        if (getArguments().getBoolean("isVip")) {
            ivVip.setVisibility(View.VISIBLE);
            ivPremium.setVisibility(View.GONE);
        } else if (getArguments().getBoolean("isPremium")) {
            ivVip.setVisibility(View.GONE);
            ivPremium.setVisibility(View.VISIBLE);
        } else {
            ivVip.setVisibility(View.GONE);
            ivPremium.setVisibility(View.GONE);
        }

        image.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getActivity()).load(item.getGlideImageUrl())
                        .into(image);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DetailActivity) getActivity()).changeMode();
            }
        });
        return view;
    }

    public void updateFragment(boolean isFullScreen) {
        image.setScaleType(isFullScreen ? ImageView.ScaleType.FIT_CENTER : ImageView.ScaleType.CENTER_CROP);
    }
}
