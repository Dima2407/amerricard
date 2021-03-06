package com.devtonix.amerricard.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.ui.activity.DetailActivity;


public class DetailFragment extends BaseFragment {

    private ImageView image;
    private ImageView ivVip;
    private ImageView ivPremium;

    public static DetailFragment getInstance(int position) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle b = new Bundle();
        setBundle(b, position);
        detailFragment.setArguments(b);
        return detailFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);

        image = (ImageView) view.findViewById(R.id.detail_image);
        ivVip = (ImageView) view.findViewById(R.id.ivVip);
        ivPremium = (ImageView) view.findViewById(R.id.ivPremium);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFullScreenMode();
                updateFragment();
            }
        });
        return view;
    }

    private void changeFullScreenMode() {
        ((DetailActivity) getActivity()).changeMode();
    }


    private int getCurrentItemIndex(){
        return getArguments().getInt("card");
    }

    private CardItem getCurrentItem() {
        return ((DetailActivity) getActivity()).getItemAt(getCurrentItemIndex());
    }

    private boolean isVip() {
        return getCurrentItem().isVip();
    }

    private boolean isPremium() {
        return getCurrentItem().isPremium();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int screenOrientation = getResources().getConfiguration().orientation;
        final int screenWidth = screenOrientation == Configuration.ORIENTATION_PORTRAIT ? sharedHelper.getDisplayWidth() : sharedHelper.getDisplayHight();

        RequestOptions options = RequestOptions.skipMemoryCacheOf(true);
        Glide.with(this)
                .load(getCurrentItem().getThumbImageUrl(screenWidth))
                .apply( options)
                .into(image);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateFragment();
    }

    public void updateFragment() {
        boolean isFullScreen = isFullScreen();

        image.setAdjustViewBounds(true);
        image.setScaleType( isFullScreen ? ImageView.ScaleType.FIT_CENTER : ImageView.ScaleType.CENTER_CROP);
        updateBadge(isFullScreen);
    }

    private boolean isFullScreen(){
        return ((DetailActivity) getActivity()).isFullScreen();
    }

    private void updateBadge(boolean isFullScreen) {
        if (isFullScreen) {
            ivVip.setVisibility(View.GONE);
            ivPremium.setVisibility(View.GONE);
        } else {
            if (isVip()) {
                ivVip.setVisibility(View.VISIBLE);
                ivPremium.setVisibility(View.GONE);
            } else if (isPremium()) {
                ivVip.setVisibility(View.GONE);
                ivPremium.setVisibility(View.VISIBLE);
            } else {
                ivVip.setVisibility(View.GONE);
                ivPremium.setVisibility(View.GONE);
            }
        }
    }

    private static void setBundle(Bundle b, int index) {
        b.putInt("card", index);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setBundle(outState, getCurrentItemIndex());
    }
}
