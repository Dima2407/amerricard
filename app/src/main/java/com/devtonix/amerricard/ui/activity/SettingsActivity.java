package com.devtonix.amerricard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.receivers.HolidaysBroadcastReceiver;
import com.devtonix.amerricard.utils.LanguageUtils;

public class SettingsActivity extends DrawerActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private SwitchCompat notificationSwitch;
    private SwitchCompat celebritiesSwitch;
    private ViewGroup timerContainer;
    private TextView timerText;
    private ViewGroup languageContainer;
    private TextView languageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_settings);
        setTitle(getString(R.string.settings));

        initViews();
//        initListeners();
//        manageNotifications();
//        manageCelebrities();
//        setLanguageSelected((SharedHelper.getInstance().getLanguage()));

//        timerText.setText(SharedHelper.getInstance().getNotificationsTime());
    }

    private void initViews(){
        languageText = (TextView) findViewById(R.id.setting_language_value);
        languageContainer = (ViewGroup) findViewById(R.id.language_container);
        timerContainer = (ViewGroup) findViewById(R.id.settings_timer_container);
        celebritiesSwitch = (SwitchCompat) findViewById(R.id.settings_celebrity_switcher);
        notificationSwitch = (SwitchCompat) findViewById(R.id.settings_notification_switcher);
        timerText = (TextView) findViewById(R.id.setting_timer_value);

    }

//    private void initListeners(){
//        timerContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onTimeClick();
//            }
//        });
//        celebritiesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                SharedHelper.getInstance().setCelebrities(b);
//                manageCelebrities();
//            }
//        });
//        languageContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onLanguageClick();
//            }
//        });
//        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                SharedHelper.getInstance().setNotification(b);
//                manageNotifications();
//            }
//        });
//    }
//
//    private void manageNotifications() {
//        notificationSwitch.setChecked(SharedHelper.getInstance().getNotification());
//    }
//
//    private void manageCelebrities() {
//        celebritiesSwitch.setChecked(SharedHelper.getInstance().getCelebrities());
//    }

//    private void onTimeClick() {
//        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
//                final String time = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
//                timerText.setText(time);
//                SharedHelper.getInstance().saveNotificationsTime(time);
//
//                startNotificationReceiver();
//            }
//        }, 8, 0, true);
//        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
//        timePickerDialog.show(getFragmentManager(), "timer");
//    }

    private void onLanguageClick() {
        new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title(R.string.language)
                .items(R.array.languages)
                .itemsCallbackSingleChoice(LanguageUtils.getLanguagePositionInList(this), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String[] langs = getResources().getStringArray(R.array.language_codes);
                        LanguageUtils.setLanguage(SettingsActivity.this, langs[which]);
                        SettingsActivity.this.recreate();
                        setLanguageSelected(langs[which]);
                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                .show();
    }

    private void setLanguageSelected(String language) {
        languageText.setText(LanguageUtils.getLanguageByCode(this, language));
    }

    private void startNotificationReceiver(){
        Log.d(TAG, "startNotificationReceiver @(^_^)@");
        Intent startReceiver = new Intent(this, HolidaysBroadcastReceiver.class);
        getApplicationContext().sendBroadcast(startReceiver);
    }
}