package com.devtonix.amerricard.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.vending.billing.IInAppBillingService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.model.Credit;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.ui.adapter.DetailPagerAdapter;
import com.devtonix.amerricard.ui.callback.CardShareCallback;
import com.devtonix.amerricard.ui.fragment.CategoryFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
    public static final String TYPE_VIP = "VIP";
    public static final String TYPE_PREMIUM = "PREMIUM";
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
    private CardItem item;
    private Thread thread1;
    private Thread thread2;
    private Bundle ownedItems;
    private Handler handler;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
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
        final CardItem currentCardItem;
        int positionForCurrentCard;

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        if (TextUtils.equals(getIntent().getAction(), ACTION_SHOW_FAVORITE_CARDS)) {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_FAVORITE_CARD, 0);
            List<CardItem> list = getIntent().getParcelableArrayListExtra(PARCELABLE_CARDS);
            cards.addAll(list);
            currentCardItem = cards.get(positionForCurrentCard);
        } else if (TextUtils.equals(getIntent().getAction(), ACTION_SHOW_CARD_FROM_EVENT_SCREEN)) {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_CARD_FROM_EVENT_SCREEN, 0);
            List<CardItem> list = getIntent().getParcelableArrayListExtra(PARCELABLE_CARDS);
            cards.addAll(list);
            currentCardItem = cards.get(positionForCurrentCard);
        } else {
            positionForCurrentCard = getIntent().getIntExtra(POSITION_FOR_CURRENT_CARD, 0);
            final int positionForCategory = getIntent().getIntExtra(POSITION_FOR_CATEGORY, 0);
            final int positionForCard = getIntent().getIntExtra(POSITION_FOR_CARD, 0);

            Log.d(TAG, "positionForCategory  = " + positionForCategory);
            Log.d(TAG, "positionForCard = " + positionForCard);

            if (positionForCategory == CategoryFragment.POSITION_NOT_SET) {
                //this are not categories with cards, there are cards only
                cards.addAll(mainCategories.get(positionForCard).getCardItems());
                currentCardItem = mainCategories.get(positionForCard).getCardItems().get(positionForCurrentCard);
            } else {
                //this are categories with cards
                if (mainCategories.get(positionForCategory).getCategoryItems().size() > 0) {
                    cards.addAll(mainCategories.get(positionForCategory).getCategoryItems().get(positionForCard).getCardItems());
                    currentCardItem = mainCategories.get(positionForCategory).getCategoryItems().get(positionForCard).getCardItems().get(positionForCurrentCard);
                } else {
                    cards.addAll(mainCategories.get(positionForCategory).getCardItems());
                    currentCardItem = mainCategories.get(positionForCategory).getCardItems().get(positionForCurrentCard);
                }
            }
        }

        handler = new Handler();

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getType() == null) {
                cards.remove(i);
            }
        }

        adapter = new DetailPagerAdapter(this, getSupportFragmentManager(), cards);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin((int) getResources().getDimension(R.dimen.base_padding));

        findViewById(R.id.toolbar_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CardItem cardItem = cards.get(viewPager.getCurrentItem());

                if (TextUtils.equals(cardItem.getCardType(), TYPE_VIP) && !sharedHelper.isVip()) {
                    chekMyPurchases();
                    showBuyDialog(getString(R.string.are_your_want_to_buy_vip));
                    return;
                } else if (TextUtils.equals(cardItem.getCardType(), TYPE_PREMIUM) && !sharedHelper.isVipOrPremium()) {
                    chekMyPurchases();
                    showBuyDialog(getString(R.string.are_your_want_to_buy_premium));
                    return;
                }

                progress.setVisibility(View.VISIBLE);

                onShareItem(/*adapter.getImage(viewPager.getCurrentItem())*/cardItem.getGlideImageUrl());

                String token = sharedHelper.getAccessToken();
                cardRepository.sendShareCardRequest(token, currentCardItem.getId(), new MyCardShareCallback());
            }
        });
        viewPager.setCurrentItem(positionForCurrentCard);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);

        setMode();

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
        if (requestCode == REQUEST_CODE_SHARE) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }
        }
    }

    public void onShareItem(GlideUrl s) {

        //web -> bmp
        Glide.with(this).asBitmap().load(s)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
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
        public void onSuccess(Credit credit) {
            if (credit != null) {
                Log.i(TAG, "onSuccess: card shared. credits VIP = " + credit.getData().getVip() + ", PREMIUM = " + credit.getData().getPremium());
            } else {
                Log.i(TAG, "onSuccess: card shared. credit = null");
            }
        }

        @Override
        public void onError() {
            Log.i(TAG, "onError: card not shared");
        }

        @Override
        public void onRetrofitError(String message) {
            Log.i(TAG, "onRetrofitError: error");
        }
    }

    private void showBuyDialog(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.app_name)
                .setMessage(message)
                .setIcon(R.drawable.ic_logo)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadVipScreen();
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

    private void chekMyPurchases() {

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ownedItems = mService.getPurchases(3, getPackageName(), "subs", null);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int response = ownedItems.getInt("RESPONSE_CODE");
                if (response == 0) {
                    ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                    ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                    ArrayList<String> signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                    String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                    for (int i = 0; i < purchaseDataList.size(); ++i) {
                        if (ownedSkus.get(i).contains(VIP)) {
                            Log.d(TAG, " vip_test was purchased");
                            sharedHelper.setUserStatus(TYPE_VIP);
                            return;
                        }
                        if (ownedSkus.get(i).contains(PREMIUM)) {
                            Log.d(TAG, " vip_test was purchased");
                            sharedHelper.setUserStatus(TYPE_PREMIUM);
                            return;
                        }
                        String purchaseData = purchaseDataList.get(i);
                        String signature = signatureList.get(i);
                        String sku = ownedSkus.get(i);

                        Log.d(TAG, "purchaseData = " + purchaseData);
                        Log.d(TAG, "thread4 signature = " + signature);
                        Log.d(TAG, "thread4 sku = " + sku);
                    }
                }

            }
        });

        handler.post(thread1);
        handler.post(thread2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("item", item);
    }
}
