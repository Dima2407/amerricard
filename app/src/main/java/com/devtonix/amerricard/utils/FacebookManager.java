package com.devtonix.amerricard.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

class FacebookManager {

    public static final int FB_REQUEST_CODE = FacebookSdk.getCallbackRequestCodeOffset();
    private Context context;
    private Fragment fragment;
    private CallbackManager callbackManager;
    private SocialCallback callback;

    public interface SocialCallback {
        void onLoginFacebook(LoginResult loginResult);
        void onCancel();
        void onError(FacebookException error);
    }

    public FacebookManager(Context context, SocialCallback callback) {
        FacebookSdk.sdkInitialize(context);
        this.context = context;
        this.callback = callback;
        this.callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,new FBcallback());
    }

    public void doFacebookLogin(Fragment fragment){
        this.fragment = fragment;
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile", "email"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private class FBcallback implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            callback.onLoginFacebook(loginResult);
        }

        @Override
        public void onCancel() {
            callback.onCancel();
        }

        @Override
        public void onError(FacebookException error) {
            callback.onError(error);
        }
    }

}
