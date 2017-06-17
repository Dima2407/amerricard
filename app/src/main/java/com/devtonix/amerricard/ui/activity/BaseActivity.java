package com.devtonix.amerricard.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.storage.SharedHelper;

import java.util.Locale;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    protected ProgressDialog progressDialog;
    private Locale currentLocale;

    @Inject
    SharedHelper sharedHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setCancelable(false);

        setLocale();
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentLocale = getResources().getConfiguration().locale;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        final Locale locale = getLocale();

        if (!locale.equals(currentLocale)){
            currentLocale = locale;
            recreate();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setLocale();
    }

    private void setLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            final Resources resources = getResources();
            final Configuration configuration = resources.getConfiguration();
            final Locale locale = getLocale();
            if (!configuration.locale.equals(locale)) {
                configuration.setLocale(locale);
            }
            resources.updateConfiguration(configuration, null);
        }
    }

    public Locale getLocale() {
        final String currLang = sharedHelper.getLanguage();
        return new Locale(currLang);
    }
}
