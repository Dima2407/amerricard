package com.devtonix.amerricard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.content.FileProvider;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.DataCreditResponse;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.callback.CardShareCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class SharingUtils {

    private static final String WEB_CITE = " amerricards.com";
    private static int REQUEST_CODE_SHARE = 2002;

    private static final ConcurrentHashMap<Integer, Pair<CardItem, Runnable>> sharingTasks = new ConcurrentHashMap<>();


    public static void cache(final Activity context, final ProgressBar progressBar, final CardItem item, final Runnable completed) {
        GlideUrl glideImageUrl = item.getGlideImageUrl();

        if (item.getCachedUri() == null) {
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().load(glideImageUrl)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            new LoadImageTask(context, progressBar, resource, item){
                                @Override
                                protected void onPostExecute(Uri bmpUri) {
                                    super.onPostExecute(bmpUri);
                                    if(completed != null){
                                        completed.run();
                                    }
                                }
                            }.execute();
                        }
                    });
        }else {
            if(completed != null){
                completed.run();
            }
        }
    }

    public static void share(final Activity context,
                             CardRepository cardRepository,SharedHelper sharedHelper, final CardItem item, Runnable completed) {
        final WeakReference<Activity> activityRef  = new WeakReference<Activity>(context);
        for(Integer key : sharingTasks.keySet()){
            Pair<CardItem, Runnable> pair = sharingTasks.get(key);
            if(pair.first == item){
                shareToOS(item.getCachedUri(), activityRef.get(), key);
                return;
            }
        }
        final int requestKey = REQUEST_CODE_SHARE++;
        sharingTasks.put(requestKey, new Pair<CardItem, Runnable>(item, completed));

        cardRepository.sendShareCardRequest(sharedHelper.getAccessToken(), item.getId(),
                new MyCardShareCallback(sharedHelper){
                    @Override
                    public void onSuccess(DataCreditResponse dataCreditResponse) {
                        super.onSuccess(dataCreditResponse);
                        if(activityRef.get() != null) {
                            shareToOS(item.getCachedUri(), activityRef.get(),requestKey);
                        }
                    }
                });
    }

    public static void clear() {
        sharingTasks.clear();
    }

    public static void processActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Pair<CardItem, Runnable> pair = sharingTasks.remove(requestCode);
            if(pair != null){
                pair.second.run();
            }
        }else {
            Pair<CardItem, Runnable> pair = sharingTasks.get(requestCode);
            if(pair != null){
                pair.second.run();
            }
        }
    }

    private static class LoadImageTask extends AsyncTask<Void, Void, Uri> {
        private final WeakReference<Activity> context;
        private final WeakReference<ProgressBar> progress;
        private Bitmap bmp;
        private final CardItem cardItem;

        public LoadImageTask(Activity context, ProgressBar progress, Bitmap bmp, CardItem cardItem) {
            this.context = new WeakReference<Activity>(context);
            this.progress = new WeakReference<ProgressBar>(progress);
            this.bmp = bmp;
            this.cardItem = cardItem;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progress.get() != null) {
                progress.get().setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Uri doInBackground(Void... voids) {
            Activity context = this.context.get();
            if (context != null) {
                return FileProvider.getUriForFile(context, "com.devtonix.amerricard.fileprovider", getFile(context, bmp));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Uri bmpUri) {
            super.onPostExecute(bmpUri);
            if(bmpUri != null){
                cardItem.setCachedUri(bmpUri);
            }
            if (progress.get() != null) {
                progress.get().setVisibility(View.GONE);
            }
        }
    }

    private static void shareToOS(Uri bmpUri, Activity activity, int requestCode) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.created_with) + WEB_CITE);
        shareIntent.setType("image/*");

        activity.startActivityForResult(Intent.createChooser(shareIntent, activity.getString(R.string.share_via)), requestCode);
    }

    private static File getFile(Context context, Bitmap bmp) {


        try {
            File file = File.createTempFile("share-image", ".jpeg", context.getFilesDir());
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return file;
        } catch (IOException e) {
            Log.e(SharingUtils.class.getSimpleName(), "getFile: ", e);
        }

        return null;
    }

    private static class MyCardShareCallback implements CardShareCallback {
        private SharedHelper sharedHelper;

        public MyCardShareCallback(SharedHelper sharedHelper) {
            this.sharedHelper = sharedHelper;
        }

        @Override
        public void onSuccess(DataCreditResponse dataCreditResponse) {
            if (dataCreditResponse != null) {
                sharedHelper.setValueVipCoins(dataCreditResponse.getVipCoins());
                sharedHelper.setValuePremiumCoins(dataCreditResponse.getPremiumCoins());
                Log.i(SharingUtils.class.getSimpleName(), "onSuccess: card shared. credits VIP = " + dataCreditResponse.getCredit().getVip() + ", PREMIUM = " + dataCreditResponse.getCredit().getPremium());
            } else {
                Log.i(SharingUtils.class.getSimpleName(), "onSuccess: card shared. dataCreditResponse = null");
            }
        }

        @Override
        public void onError() {
            Log.e(SharingUtils.class.getSimpleName(), "onError: card not shared");
        }

        @Override
        public void onRetrofitError(String message) {
            Log.e(SharingUtils.class.getSimpleName(), "onRetrofitError: " + message);
        }
    }

}
