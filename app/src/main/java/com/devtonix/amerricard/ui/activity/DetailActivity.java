package com.devtonix.amerricard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.api.NetworkServiceProvider;
import com.devtonix.amerricard.model.Item;

/**
 * Created by Oleksii on 02.02.17.
 */
public class DetailActivity extends BaseActivity {

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle("Send a card");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Item item = (Item) getIntent().getSerializableExtra("item");

        findViewById(R.id.toolbar_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, NetworkServiceProvider.BASE_URL
                        + (item.isItemCategory() ? NetworkServiceProvider.CATEGORY_SUFFIX : NetworkServiceProvider.CARD_SUFFIX)
                        + item.id + "/image");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });





        image = (ImageView) findViewById(R.id.image);
        image.post(new Runnable() {
            @Override
            public void run() {
                String url = NetworkServiceProvider.BASE_URL
                        + (item.isItemCategory() ? NetworkServiceProvider.CATEGORY_SUFFIX : NetworkServiceProvider.CARD_SUFFIX)
                        + item.id + "/image?width="+image.getWidth()+"&height="+image.getHeight()+"&type=fit";

                Glide.with(DetailActivity.this).load(url)
                        .into(image);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
