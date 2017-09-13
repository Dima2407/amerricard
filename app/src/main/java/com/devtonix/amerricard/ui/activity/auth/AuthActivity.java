package com.devtonix.amerricard.ui.activity.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.ui.activity.BaseActivity;
import com.devtonix.amerricard.ui.fragment.ForgetPasswordFragment;
import com.devtonix.amerricard.ui.fragment.LoginFragment;
import com.devtonix.amerricard.ui.fragment.RegistrationFragment;

public class AuthActivity extends BaseActivity implements AuthDelegate {

    @Override
    public void close() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void forgotPassword() {
        String tag = ForgetPasswordFragment.class.getSimpleName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new ForgetPasswordFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void registration() {
        String tag = RegistrationFragment.class.getSimpleName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new RegistrationFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, tag)
                .addToBackStack(null
                )
                .commit();
    }

    @Override
    public void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public static void login(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, AuthActivity.class), requestCode);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            LoginFragment fragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, LoginFragment.class.getSimpleName())
                    .commit();
        }
    }
}
