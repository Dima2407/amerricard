package com.devtonix.amerricard.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.network.response.CreditsResponse;
import com.devtonix.amerricard.repository.UserRepository;
import com.devtonix.amerricard.ui.activity.auth.AuthActivity;
import com.devtonix.amerricard.ui.callback.GetCreditsCallback;
import com.devtonix.amerricard.ui.fragment.VipAndPremiumAbstractFragment;

import javax.inject.Inject;

public class DrawerActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    UserRepository userRepository;

    private static final int AUTH_REQUEST_CODE = 10;

    private LayoutInflater inflater;

    public static final String TAG = VipAndPremiumAbstractFragment.class.getSimpleName();

    private TextView headerTitle;
    private TextView headerEmail;
    private ImageView headerImage;
    private TextView name;
    private DrawerLayout drawer;

    private LinearLayout regLayout;
    private Button logoutButton;
    private ImageView unregLogoImageView;
    private ImageView isregLogoImageView;
    private TextView regNameTextView;
    private TextView regEmailTextView;

    private String logInOut;

    protected final static String PURCHASE_TRANSACTION_ID = "12345";
    protected final static String APP_TYPE = "android";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ACApplication.getMainComponent().inject(this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        logInOutButtonInit();
        initSidePanel();

    }

    protected void init(int layout) {

        inflater.inflate(layout, (ViewGroup) findViewById(R.id.drawer_content));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    protected void onResume() {
        Log.d("MyLog", "onResume: " + String.format("(%s)", !(TextUtils.isEmpty(sharedHelper.getAccessToken())))
                + sharedHelper.getEmail() + " " + sharedHelper.getName());
        super.onResume();
        logInOutButtonInit();

        if (!(TextUtils.isEmpty(sharedHelper.getAccessToken()))) {
            regLayout.setVisibility(View.VISIBLE);
            Log.d("MyLog", "onResume: visible " + regLayout.getVisibility());
            unregLogoImageView.setVisibility(View.INVISIBLE);
            regNameTextView.setText(sharedHelper.getName());
            regEmailTextView.setText(sharedHelper.getEmail());

            userRepository.getCredits(sharedHelper.getAccessToken(), new GetCreditsCallback() {
                @Override
                public void onSuccess(CreditsResponse creditsResponse) {

                }

                @Override
                public void onError() {

                }

                @Override
                public void onRetrofitError(String message) {

                }
            });

        } else {
            unregLogoImageView.setVisibility(View.VISIBLE);
            regLayout.setVisibility(View.INVISIBLE);
        }


    }

    protected DrawerLayout getDrawer() {
        return drawer;
    }

    private void initSidePanel() {
        regLayout = (LinearLayout) findViewById(R.id.drawer_registration_layout);
        unregLogoImageView = (ImageView) findViewById(R.id.un_reg_logo_image_view);
        isregLogoImageView = (ImageView) findViewById(R.id.is_reg_logo_image_view);

        regNameTextView = (TextView) findViewById(R.id.drawer_name_text_view);
        regEmailTextView = (TextView) findViewById(R.id.drawer_email_text_view);

        addItem(R.id.drawer_cards, getString(R.string.category), R.drawable.categories_icon);
        addItem(R.id.drawer_calendar, getString(R.string.calendar), R.drawable.ic_calendar);
        addItem(R.id.drawer_favorites, getString(R.string.favorite_cards), R.drawable.ic_favorite_full);
        addItem(R.id.drawer_manage_holidays, getString(R.string.manage_birthdays), R.drawable.ic_edit);
        /*userRepository.getCredits(sharedHelper.getAccessToken(), new GetCreditsCallback() {
        });*/
        if (!(sharedHelper.getAccessToken() != null)) {
            addItem(R.id.drawer_vip, getString(R.string.become_vip_title), R.drawable.ic_vip, userRepository.getValueVipCoin());
            addItem(R.id.drawer_premium, getString(R.string.premium), R.drawable.ic_premium, userRepository.getValuePremiumCoin());
        } else {
            addItem(R.id.drawer_vip, getString(R.string.become_vip_title), R.drawable.ic_vip);
            addItem(R.id.drawer_premium, getString(R.string.premium), R.drawable.ic_premium);
        }
        addItem(R.id.drawer_settings, getString(R.string.settings), R.drawable.ic_settings);

    }

    private void addItem(int view, String title, int image) {
        ViewGroup vg = (ViewGroup) findViewById(view);
        ((TextView) vg.findViewById(R.id.view_drawer_item_text)).setText(title);
        ((ImageView) vg.findViewById(R.id.view_drawer_item_icon)).setImageResource(image);
        vg.setOnClickListener(this);
    }

    private void addItem(int view, String title, int image, int coinValue) {
        ViewGroup vg = (ViewGroup) findViewById(view);

        ((TextView) vg.findViewById(R.id.view_drawer_item_text)).setText(title);
        ((ImageView) vg.findViewById(R.id.view_drawer_item_icon)).setImageResource(image);
        ((TextView) vg.findViewById(R.id.coin_value_text_view)).setText(coinValue);

        vg.setOnClickListener(this);
    }

    protected void setTitle(String title) {
        getSupportActionBar().setTitle(title == null ? "" : title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_cards:
                Intent cardIntent = new Intent(this, MainActivity.class);
                cardIntent.putExtra("position", 0);
                startActivity(cardIntent);
                drawer.closeDrawers();
                break;
            case R.id.drawer_calendar:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                drawer.closeDrawers();
                break;
            case R.id.drawer_favorites:
                startActivity(new Intent(this, FavoriteActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.drawer_manage_holidays:
                startActivity(new Intent(this, ManageActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.drawer_vip:
                Intent vipIntent = new Intent(this, VipAndPremiumActivity.class);
                vipIntent.putExtra(VipAndPremiumActivity.TAB_POSITION, 0);
                startActivity(vipIntent);
                drawer.closeDrawers();
                break;
            case R.id.drawer_premium:
                Intent premIntent = new Intent(this, VipAndPremiumActivity.class);
                premIntent.putExtra(VipAndPremiumActivity.TAB_POSITION, 1);
                startActivity(premIntent);
                drawer.closeDrawers();
                break;
            case R.id.drawer_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.logout_button: {
                onClickLogInOutButton();
                break;
            }

        }
        //drawer.closeDrawers();
    }

    private void logInOutButtonInit() {
      /*  logoutButton = (Button) findViewById(R.id.logout_button);
        if (!TextUtils.isEmpty(sharedHelper.getAccessToken())) {
            logoutButton.setText(R.string.log_out);
        } else {
            logoutButton.setText(R.string.login);
        }
        */
        addItem(R.id.logout_button, logInOut, R.drawable.login);
        if (!TextUtils.isEmpty(sharedHelper.getAccessToken())) {
            logInOut = getString(R.string.log_out);
        } else {
            logInOut = getString(R.string.log_in);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_REQUEST_CODE) {
            logInOutButtonInit();
        }
    }

    private void onClickLogInOutButton() {
        if (!TextUtils.isEmpty(sharedHelper.getAccessToken())) {
            sharedHelper.cleanAccessToken();
            regLayout.setVisibility(View.INVISIBLE);
            unregLogoImageView.setVisibility(View.VISIBLE);
            logInOut = getString(R.string.log_in);
            addItem(R.id.logout_button, logInOut, R.drawable.login);
        } else {
            drawer.closeDrawers();
            AuthActivity.login(DrawerActivity.this, AUTH_REQUEST_CODE);

        }
    }
}