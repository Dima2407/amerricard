package com.devtonix.amerricard.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.ui.view.GifView;

public class SplashActivity extends AppCompatActivity {


    private Handler delayHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView introImageView = (ImageView) findViewById(R.id.intro_gif_image_view);

        introImageView.setBackgroundResource(R.drawable.egg_splash);

        AnimationDrawable eggAnim = (AnimationDrawable) introImageView.getBackground();
        int delay = eggAnim.getNumberOfFrames() * eggAnim.getDuration(0);
        eggAnim.start();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, delay);
    }
}
