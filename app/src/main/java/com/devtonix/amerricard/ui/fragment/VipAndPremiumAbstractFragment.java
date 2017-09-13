package com.devtonix.amerricard.ui.fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.billing.IabHelper;
import com.devtonix.amerricard.billing.IabResult;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.network.request.BuyCreditRequest;
import com.devtonix.amerricard.network.response.CreditsResponse;
import com.devtonix.amerricard.repository.UserRepository;
import com.devtonix.amerricard.ui.activity.VipAndPremiumActivity;
import com.devtonix.amerricard.ui.activity.auth.AuthActivity;
import com.devtonix.amerricard.ui.callback.GetCreditsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public abstract class VipAndPremiumAbstractFragment extends BaseFragment {

    protected static final int REQUEST_AUTH_CODE = 10;
    @Inject
    UserRepository userRepository;

    public static final String TAG = VipAndPremiumAbstractFragment.class.getSimpleName();
    protected final static String VIP = "vip_test";
    protected final static String PREMIUM = "premium_test";
    protected final static String RESPONSE_CODE = "RESPONSE_CODE";
    protected final static int REQUEST_CODE_BUY = 1001;
    protected final static String APP_TYPE = "android";
    protected final static String PURCHASE_TRANSACTION_ID = "12345";

    protected Bundle buyIntentBundle;
    protected FrameLayout layoutContainer;
    protected IInAppBillingService mService;
    protected Handler handler;
    protected ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutContainer = (FrameLayout) view.findViewById(R.id.layout_login_container);
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        handler = new Handler();
    }

    protected void payFromGoogle(int count, String type) {

        String productId = String.format("%s_%d", type, count);

        try {
            buyIntentBundle = mService.getBuyIntent(3, getContext().getPackageName(), productId, "inapp", VipAndPremiumActivity.base64EncodedPublicKey);
        } catch (Exception e) {
            Log.e(TAG, "payFromGoogle: ", e);
            return;
        }

        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
        if (pendingIntent == null) {
            Toast.makeText(getActivity(), R.string.service_unavailable, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
                    REQUEST_CODE_BUY, new Intent(), 0, 0, 0);
        } catch (Exception e) {
            Log.e(TAG, "payFromGoogle: ",  e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }
    }

    private void send(String productId, String orderId, String purchaseToken, Runnable complete) {

        String accessToken = sharedHelper.getAccessToken();
        BuyCreditRequest request = new BuyCreditRequest();
        request.setCreditType(getCreditsType());
        request.setCredits(getAmountOfCredits());
        request.setProductId(productId);
        request.setPurchaseTransactionId(orderId);
        request.setPurchaseToken(purchaseToken);
        userRepository.buyCredits(accessToken, request, new GetCreditCallbackImpl(complete));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_BUY:
                int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
                String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");

                if (responseCode == Activity.RESULT_OK) {
                    try {
                        JSONObject jo = new JSONObject(purchaseData);
                        String productId = jo.optString("productId");
                        String orderId = jo.optString("orderId");
                        String purchaseToken = jo.optString("purchaseToken");
                        pay(productId, orderId, purchaseToken);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    protected final void pay(String productId, String orderId, String purchaseToken) {
        if(TextUtils.isEmpty(sharedHelper.getAccessToken())){
            AuthActivity.login(getActivity(), REQUEST_AUTH_CODE);
            return;
        }
        if (TextUtils.isEmpty(productId)) {
            payFromGoogle(getAmountOfCredits(), getCreditsType());
        } else {
            send(productId, orderId, purchaseToken, new Runnable() {
                @Override
                public void run() {
                    onCoinsUpdated(sharedHelper.getValueVipCoins(), sharedHelper.getValuePremiumCoins());
                }
            });
        }
    }

    private class GetCreditCallbackImpl implements GetCreditsCallback {

        private Runnable complete;

        public GetCreditCallbackImpl(Runnable complete) {

            this.complete = complete;
        }

        @Override
        public void onSuccess(CreditsResponse creditsResponse) {
            if (creditsResponse != null && creditsResponse.isSuccess()) {
                sharedHelper.setValuePremiumCoins(creditsResponse.getPremiumCoins());
                sharedHelper.setValueVipCoins(creditsResponse.getVipCoins());
                if (complete != null) {
                    complete.run();
                }

            }
        }

        @Override
        public void onError() {
            Log.i(TAG, "onError: ");
        }

        @Override
        public void onRetrofitError(String message) {
            Log.i(TAG, "onRetrofitError: message = " + message);
        }

    }

    protected abstract int getAmountOfCredits();

    protected abstract String getCreditsType();

    protected abstract void onCoinsUpdated(int vipCoinsCount, int premiumCoinsCount);
}
