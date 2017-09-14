package com.devtonix.amerricard.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.ui.activity.auth.AuthActivity;
import com.devtonix.amerricard.ui.adapter.DetailPagerAdapter;
import com.devtonix.amerricard.ui.fragment.CategoryFragment;
import com.devtonix.amerricard.utils.SharingUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
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
    public static final String RECLAM_POSITION = "reclam_position";
    private final static String VIP = "vip_test";
    private final static String PREMIUM = "premium_test";

    private static final int REQUEST_AUTH_CODE = 1001;
    private boolean isFullScreen = false;
    private ViewGroup container;
    private AppBarLayout bar;
    private ProgressBar progress;
    private ViewPager viewPager;
    private DetailPagerAdapter adapter;
    private InterstitialAd interstitialAd;
    private List<CategoryItem> mainCategories;
    private CardItem item;
    private Runnable sharingCompleted = new Runnable() {
        @Override
        public void run() {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            item = savedInstanceState.getParcelable("item");
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_detail);

        ACApplication.getMainComponent().inject(this);

        isFullScreen = savedInstanceState != null && savedInstanceState.getBoolean("fullscreen");

        setTitle(getString(R.string.send_card));
        initViews();
        initToolbar();

        mainCategories = cardRepository.getCardsFromStorage();

        final List<CardItem> cards = new ArrayList<>();
        int positionForCurrentCard;

        if (TextUtils.equals(getIntent().getAction(), ACTION_SHOW_FAVORITE_CARDS)) {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_FAVORITE_CARD, 0);
            List<CardItem> list = getIntent().getParcelableArrayListExtra(PARCELABLE_CARDS);
            cards.addAll(list);
        } else if (TextUtils.equals(getIntent().getAction(), ACTION_SHOW_CARD_FROM_EVENT_SCREEN)) {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_CARD_FROM_EVENT_SCREEN, 0);
            List<CardItem> list = getIntent().getParcelableArrayListExtra(PARCELABLE_CARDS);
            cards.addAll(list);
        } else {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_CURRENT_CARD, 0);
            final int positionForCategory = getIntent().getIntExtra(POSITION_FOR_CATEGORY, 0);
            final int positionForCard = getIntent().getIntExtra(POSITION_FOR_CARD, 0);

            Log.d(TAG, "positionForCategory  = " + positionForCategory);
            Log.d(TAG, "positionForCard = " + positionForCard);

            if (positionForCategory == CategoryFragment.POSITION_NOT_SET) {
                //this are not categories with cards, there are cards only
                cards.addAll(mainCategories.get(positionForCard).getCardItems());
            } else {
                //this are categories with cards
                if (mainCategories.get(positionForCategory).getCategoryItems().size() > 0) {
                    cards.addAll(mainCategories.get(positionForCategory).getCategoryItems().get(positionForCard).getCardItems());
                } else {
                    cards.addAll(mainCategories.get(positionForCategory).getCardItems());
                }
            }
        }


        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getType() == null) {
                cards.remove(i);
            }
        }

        adapter = new DetailPagerAdapter(this, getSupportFragmentManager(), cards);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin((int) getResources().getDimension(R.dimen.base_padding));

        findViewById(R.id.toolbar_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CardItem cardItem = cards.get(viewPager.getCurrentItem());

                String token = sharedHelper.getAccessToken();

                if (cardItem.isFree()) {
                    shareCard(cardItem);
                    return;
                }

                if (TextUtils.isEmpty(token)) {
                    AuthActivity.login(DetailActivity.this, REQUEST_AUTH_CODE);
                    return;
                }

                if (cardItem.isVip()) {
                    if (sharedHelper.isVip()) {
                        if (sharedHelper.getValueVipCoins() > 0) {
                            shareCard(cardItem);
                        } else {
                            showBuyDialog(getString(R.string.not_enough_to_buy_vip), VipAndPremiumActivity.SHOW_VIP_ACTION);
                        }

                    } else {
                        showBuyDialog(getString(R.string.are_your_want_to_buy_vip), VipAndPremiumActivity.SHOW_VIP_ACTION);
                    }
                } else if (cardItem.isPremium()) {
                    if (sharedHelper.isPremium()) {
                        if (sharedHelper.getValuePremiumCoins() > 0) {
                            shareCard(cardItem);
                        } else {
                            showBuyDialog(getString(R.string.not_enough_to_buy_premium), VipAndPremiumActivity.SHOW_PREMIUM_ACTION);
                        }
                    } else {
                        showBuyDialog(getString(R.string.are_your_want_to_buy_premium), VipAndPremiumActivity.SHOW_PREMIUM_ACTION);
                    }
                }
            }
        });
        viewPager.setCurrentItem(positionForCurrentCard);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);

        setMode();

    }

    private void shareCard(final CardItem cardItem) {
        SharingUtils.cache(this, progress, cardItem, new Runnable() {
            @Override
            public void run() {
                SharingUtils.share(DetailActivity.this, cardRepository, sharedHelper, cardItem,
                        sharingCompleted);
            }
        });
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

    private void loadBuyScreen(String action) {
        Intent intent = new Intent(DetailActivity.this, VipAndPremiumActivity.class);
        intent.setAction(action);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void changeMode() {
        isFullScreen = !isFullScreen;
        setMode();
    }

    private void setMode() {
        if (isFullScreen) {
            bar.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
        } else {
            bar.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
        }
    }

    public void setFragment(CardItem item) {
        this.item = item;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharingUtils.processActivityResult(requestCode, resultCode, data);
    }

    public CardItem getItemAt(int currentItemIndex) {
        return adapter.getDataItemAt(currentItemIndex);
    }

    private void showBuyDialog(String message, final String action) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.app_name)
                .setMessage(message)
                .setIcon(R.drawable.ic_logo)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadBuyScreen(action);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fullscreen", isFullScreen);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharingUtils.clear();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("item", item);
    }
}
