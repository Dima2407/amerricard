package com.devtonix.amerricard.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.ui.adapter.DetailPagerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Oleksii on 02.02.17.
 */
public class DetailActivity extends BaseActivity {

    private boolean isFullScreen = false;
    private ViewGroup container;
    private AppBarLayout bar;
    private ProgressBar progress;
    private DetailPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(getString(R.string.send_card));

        bar = (AppBarLayout) findViewById(R.id.detail_appbar);
        container = (ViewGroup) findViewById(R.id.detail_pager_container);
        progress = (ProgressBar) findViewById(R.id.detail_activity_progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final List<Item> items = (List<Item>) getIntent().getSerializableExtra("list");
        final int position = getIntent().getIntExtra("position", 0);
        final ViewPager pager = (ViewPager) findViewById(R.id.detail_view_pager);

        adapter = new DetailPagerAdapter(this, getSupportFragmentManager(), items);
        pager.setAdapter(adapter);

        findViewById(R.id.toolbar_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                onShareItem(adapter.getImage(pager.getCurrentItem()));
            }
        });

        pager.setCurrentItem(position);
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
            Bitmap bmp = ((GlideBitmapDrawable)drawable).getBitmap();

            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");

            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(DetailActivity.this, "com.devtonix.amerricard.fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
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
                shareIntent.putExtra(Intent.EXTRA_TEXT, "From Amerricards");
                shareIntent.setType("image/*");

                startActivity(Intent.createChooser(shareIntent, "Share Image"));

            }
            progress.setVisibility(View.GONE);
        }
    }
}
