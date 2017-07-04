package com.devtonix.amerricard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.ui.activity.DetailActivity;

public class DetailFragment extends BaseFragment {

    private ViewGroup detailContainer;
    private ImageView image;
    private String url = "";

    public static DetailFragment getInstance(int id, boolean isFullScreen) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle b = new Bundle();
        b.putString("id", String.valueOf(id));
        b.putBoolean("fullscreen", isFullScreen);
        detailFragment.setArguments(b);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);

        detailContainer = (ViewGroup) view.findViewById(R.id.detail_container);

        image = (ImageView) view.findViewById(R.id.detail_image);
        image.post(new Runnable() {
            @Override
            public void run() {
                url = NetworkModule.BASE_URL
                        + NetworkModule.CARD_SUFFIX
                        + getArguments().getString("id") + "/image?width="+image.getHeight()+"&height="+image.getWidth()+"&type=fit";

                Glide.with(getActivity()).load(url)
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

    public String getImageUrl() {
        return url;
    }


    public void updateFragment(boolean isFullScreen) {
        image.setScaleType(isFullScreen ? ImageView.ScaleType.FIT_CENTER : ImageView.ScaleType.CENTER_CROP);
    }
}
