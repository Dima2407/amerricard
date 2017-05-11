package com.devtonix.amerricard.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devtonix.amerricard.R;

public class DrawerActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater inflater;

    private TextView headerTitle;
    private TextView headerEmail;
    private ImageView headerImage;
    private TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initSidePanel();
    }

    protected void init(int layout) {
        inflater.inflate(layout, (ViewGroup) findViewById(R.id.drawer_content));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        name = (TextView) findViewById(R.id.drawer_nav_name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getSupportActionBar().setTitle("amerricard");
    }

    private void initSidePanel() {

        headerTitle = (TextView) findViewById(R.id.drawer_nav_name);
        headerEmail = (TextView) findViewById(R.id.drawer_nav_email);
        headerImage = (ImageView) findViewById(R.id.drawer_nav_image);

        addItem(R.id.drawer_cards, getString(R.string.cards), R.drawable.ic_birthday);
        addItem(R.id.drawer_calendar, getString(R.string.calendar), R.drawable.ic_calendar);
        addItem(R.id.drawer_favorites, getString(R.string.favorite_cards), R.drawable.ic_favorite_full);
        addItem(R.id.drawer_manage_birthday, getString(R.string.manage_birthdays), R.drawable.ic_edit);
        addItem(R.id.drawer_manage_holidays, getString(R.string.manage_holidays), R.drawable.ic_profile_settings);
        addItem(R.id.drawer_vip, getString(R.string.become_vip), R.drawable.ic_crown);
        addItem(R.id.drawer_settings, getString(R.string.settings), R.drawable.ic_settings);
    }


    private void addItem(int view, String title, int image) {
        ViewGroup vg = (ViewGroup) findViewById(view);
        ((TextView)vg.findViewById(R.id.view_drawer_item_text)).setText(title);
        ((ImageView)vg.findViewById(R.id.view_drawer_item_icon)).setImageResource(image);
        vg.setOnClickListener(this);
    }

    protected void setTitle(String title) {
        getSupportActionBar().setTitle(title == null ? "" : title);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_cards:
                Intent cardIntent = new Intent(this, MainActivity.class);
                cardIntent.putExtra("position", 0);
                startActivity(cardIntent);
                break;
            case R.id.drawer_calendar:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            case R.id.drawer_favorites:
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
            case R.id.drawer_manage_birthday:
                startActivity(new Intent(this, ManageActivity.class));
                break;
            case R.id.drawer_manage_holidays:
                startActivity(new Intent(this, ManageActivity.class));
                break;
            case R.id.drawer_vip:
                break;
            case R.id.drawer_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        this.finish();
    }
}