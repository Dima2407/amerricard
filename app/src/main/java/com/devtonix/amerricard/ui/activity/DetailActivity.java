package com.devtonix.amerricard.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.ui.adapter.DetailPagerAdapter;
import com.devtonix.amerricard.ui.callback.CardShareCallback;
import com.devtonix.amerricard.ui.fragment.CategoryFragment;
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
    public static final String POSITION_FOR_CARD = "position_for_card";
    public static final String POSITION_FOR_CATEGORY = "position_for_category";
    public static final String PARCELABLE_CARDS = "parcelable_cads";
    public static final String ACTION_SHOW_FAVORITE_CARDS = "action_show_favorite_cards";
    public static final String POSITION_FOR_CARD_FROM_EVENT_SCREEN = "position_for_card_from_event_screen";
    public static final String ACTION_SHOW_CARD_FROM_EVENT_SCREEN = "action_show_card_from_event_screen";
    private static final String TYPE_VIP = "VIP";
    private static final String TYPE_PREMIUM = "PREMIUM";
    private static final String WEB_CITE = " amerricards.com";
    private static final int REQUEST_CODE_SHARE = 2002;
    private boolean isFullScreen = false;
    private ViewGroup container;
    private AppBarLayout bar;
    private ProgressBar progress;
    private ViewPager viewPager;
    private DetailPagerAdapter adapter;
    private InterstitialAd interstitialAd;
    private List<CategoryItem> mainCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ACApplication.getMainComponent().inject(this);

        setTitle(getString(R.string.send_card));
        initViews();
        initToolbar();

        mainCategories = cardRepository.getCardsFromStorage();

        final List<CardItem> cards;
        final CardItem currentCardItem;
        int positionForCurrentCard;

        if (TextUtils.equals(getIntent().getAction(), ACTION_SHOW_FAVORITE_CARDS)) {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_FAVORITE_CARD, 0);
            cards = getIntent().getParcelableArrayListExtra(PARCELABLE_CARDS);
            currentCardItem = cards.get(positionForCurrentCard);
        } else if (TextUtils.equals(getIntent().getAction(), ACTION_SHOW_CARD_FROM_EVENT_SCREEN)) {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_CARD_FROM_EVENT_SCREEN, 0);
            cards = getIntent().getParcelableArrayListExtra(PARCELABLE_CARDS);
            currentCardItem = cards.get(positionForCurrentCard);
        } else {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_CURRENT_CARD, 0);
            final int positionForCategory = getIntent().getIntExtra(POSITION_FOR_CATEGORY, 0);
            final int positionForCard = getIntent().getIntExtra(POSITION_FOR_CARD, 0);

            Log.d(TAG, "positionForCategory  = " + positionForCategory);
            Log.d(TAG, "positionForCard = " + positionForCard);

            if (positionForCategory == CategoryFragment.POSITION_NOT_SET) {
                //this are not categories with cards, there are cards only
                cards = mainCategories.get(positionForCard).getCardItems();
                currentCardItem = mainCategories.get(positionForCard).getCardItems().get(positionForCurrentCard);
            } else {
                //this are categories with cards
                cards = mainCategories.get(positionForCategory).getCategoryItems().get(positionForCard).getCardItems();
                currentCardItem = mainCategories.get(positionForCategory).getCategoryItems().get(positionForCard).getCardItems().get(positionForCurrentCard);
            }
        }

        adapter = new DetailPagerAdapter(this, getSupportFragmentManager(), cards);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin((int) getResources().getDimension(R.dimen.base_padding));

        findViewById(R.id.toolbar_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CardItem cardItem = cards.get(viewPager.getCurrentItem());
                if (TextUtils.equals(cardItem.getCardType(), TYPE_VIP) ||
                        TextUtils.equals(cardItem.getCardType(), TYPE_PREMIUM)) {
                    loadVipScreen();
                    return;
                }
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

    private void loadVipScreen() {
        Intent intent = new Intent(DetailActivity.this, VipAndPremiumActivity.class);
        intent.setAction(VipAndPremiumActivity.SHOW_VIP_ACTION);
        startActivity(intent);
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SHARE) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }
        }
    }

    public void onShareItem(String s) {

        //web -> bmp
        Glide.with(this).load(s).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                new LoadImageTask(resource).execute();
            }
        });
    }

    private class LoadImageTask extends AsyncTask<Void, Void, Uri> {
        private Bitmap bmp;

        public LoadImageTask(Bitmap bmp) {
            this.bmp = bmp;
        }

        @Override
        protected Uri doInBackground(Void... voids) {
            return FileProvider.getUriForFile(DetailActivity.this, "com.devtonix.amerricard.fileprovider", getFile(bmp));
        }

        @Override
        protected void onPostExecute(Uri bmpUri) {
            super.onPostExecute(bmpUri);
            if (bmpUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.created_with) + WEB_CITE);
                shareIntent.setType("image/*");

                startActivityForResult(Intent.createChooser(shareIntent, getString(R.string.share_via)), REQUEST_CODE_SHARE);
            }
            progress.setVisibility(View.GONE);
        }
    }

    // bmp -> file
    private File getFile(Bitmap bmp) {

        File file = new File(getCacheDir(), "share_image_" + System.currentTimeMillis() + ".jpeg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
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
