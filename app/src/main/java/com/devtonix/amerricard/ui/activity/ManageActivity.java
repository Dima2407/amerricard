package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;

import com.devtonix.amerricard.R;

public class ManageActivity extends DrawerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_manage_holidays);

        setTitle("Manage holidays");
    }
}
