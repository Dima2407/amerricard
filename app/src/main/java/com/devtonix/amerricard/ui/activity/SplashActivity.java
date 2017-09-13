package com.devtonix.amerricard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.ui.view.GifView;

public class SplashActivity extends AppCompatActivity {


    private Handler delayHandler = new Handler();
    private GifView introImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        introImageView = (GifView) findViewById(R.id.intro_gif_image_view);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        introImageView.setGif(R.raw.intro, new GifView.OnGifFinishedListener() {
            @Override
            public void onFinished() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
