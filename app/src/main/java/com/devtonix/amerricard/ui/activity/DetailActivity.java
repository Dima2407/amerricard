package com.devtonix.amerricard.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.ui.adapter.DetailPagerAdapter;
import com.devtonix.amerricard.ui.callback.CardShareCallback;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class DetailActivity extends BaseActivity {

    @Inject
    CardRepository cardRepository;

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String POSITION_FOR_CURRENT_CARD = "position_for_card_item";
    public static final String POSITION_FOR_FAVORITE_CARD = "position_for_favorite_card";
    public static final String POSITION_FOR_CATEGORY_SCND_LVL = "position_for_category_scnd_lvl";
    public static final String POSITION_FOR_CATEGORY_FRST_LVL = "position_for_category_frst_lvl";
    public static final String PARCELABLE_CARDS = "parcelable_cads";
    public static final String ACTION_SHOW_FAVORITE_CARDS = "action_show_favorite_cards";
    private static final String WEB_CITE = " amerricards.com";
    private static final int REQUEST_CODE_SHARE = 2002;
    private boolean isFullScreen = false;
    private ViewGroup container;
    private AppBarLayout bar;
    private ProgressBar progress;
    private ViewPager viewPager;
    private DetailPagerAdapter adapter;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ACApplication.getMainComponent().inject(this);

        setTitle(getString(R.string.send_card));
        initViews();
        initToolbar();

        final List<CategoryItem> categoryFirstLevel = cardRepository.getCardsFromStorage();

        final List<CardItem> cards;
        final CardItem currentCardItem;
        int positionForCurrentCard;

        if (TextUtils.equals(getIntent().getAction(), ACTION_SHOW_FAVORITE_CARDS)) {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_FAVORITE_CARD, 0);
            Log.d(TAG, "onCreate: action = " + getIntent().getAction() + "  position =" + positionForCurrentCard);
            cards = getIntent().getParcelableArrayListExtra(PARCELABLE_CARDS);
            currentCardItem = cards.get(positionForCurrentCard);
        } else {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_CURRENT_CARD, 0);
            final int positionForCategorySecondLvl = getIntent().getIntExtra(POSITION_FOR_CATEGORY_SCND_LVL, 0);
            final int positionForCategoryFirstLvl = getIntent().getIntExtra(POSITION_FOR_CATEGORY_FRST_LVL, 0);
//            cards = categoryFirstLevel.get(positionForCategoryFirstLvl).getData().get(positionForCategorySecondLvl).getData();
            cards = categoryFirstLevel.get(positionForCategoryFirstLvl).getCategoryItems().get(positionForCategorySecondLvl).getCardItems();
//            currentCardItem = categoryFirstLevel.get(positionForCategoryFirstLvl).getData().get(positionForCategorySecondLvl).getData().get(positionForCurrentCard);
            currentCardItem = categoryFirstLevel.get(positionForCategoryFirstLvl).getCategoryItems().get(positionForCategorySecondLvl).getCardItems().get(positionForCurrentCard);
        }


        adapter = new DetailPagerAdapter(this, getSupportFragmentManager(), cards);
        viewPager.setAdapter(adapter);

        findViewById(R.id.toolbar_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                onShareItem(adapter.getImage(viewPager.getCurrentItem()));
                cardRepository.sendShareCardRequest(currentCardItem.getId(), new MyCardShareCallback());
            }
        });
        viewPager.setCurrentItem(positionForCurrentCard);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    private void initViews() {
        bar = (AppBarLayout) findViewById(R.id.detail_appbar);
        container = (ViewGroup) findViewById(R.id.detail_pager_container);
        progress = (ProgressBar) findViewById(R.id.detail_activity_progress);
        viewPager = (ViewPager) findViewById(R.id.detail_view_pager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void changeMode() {
        isFullScreen = !isFullScreen;

        if (isFullScreen) {
            bar.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
        } else {
            bar.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
        }
        adapter.setFullScreen(isFullScreen);

    }


    public void onShareItem(ImageView view) {
        new LoadImageTask().execute(view);

    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        Uri bmpUri = null;

        try {
            Drawable drawable = imageView.getDrawable();
            Bitmap bmp = ((GlideBitmapDrawable) drawable).getBitmap();

            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");

            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(DetailActivity.this, "com.devtonix.amerricard.fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SHARE) {
            Log.d(TAG, "onActivityResult: 2002");
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }
        }
    }

    class LoadImageTask extends AsyncTask<ImageView, Void, Uri> {

        @Override
        protected Uri doInBackground(ImageView... views) {
            return getLocalBitmapUri(views[0]);
        }

        @Override
        protected void onPostExecute(Uri bmpUri) {
            super.onPostExecute(bmpUri);

            if (bmpUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.created_with) + WEB_CITE);
                shareIntent.setType("image/*");

                startActivityForResult(Intent.createChooser(shareIntent, "Share Image"), REQUEST_CODE_SHARE);
            }
            progress.setVisibility(View.GONE);
        }
    }

    private class MyCardShareCallback implements CardShareCallback {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess: card shared");
        }

        @Override
        public void onError() {
            Log.d(TAG, "onError: card not shared");
        }

        @Override
        public void onRetrofitError(String message) {
            Log.d(TAG, "onRetrofitError: error");
        }
    }
}
