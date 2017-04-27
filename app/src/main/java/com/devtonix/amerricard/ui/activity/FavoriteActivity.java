package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;

import com.devtonix.amerricard.R;

public class FavoriteActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_settings);

        setTitle("Settings");
    }
}
