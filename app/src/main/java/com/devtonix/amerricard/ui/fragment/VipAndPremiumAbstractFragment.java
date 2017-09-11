package com.devtonix.amerricard.ui.fragment;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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

import java.util.ArrayList;

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

    private TextView valueCoinsTextView;

    protected IabHelper iabHelper;
    protected Bundle skuDetails;
    protected Bundle buyIntentBundle;
    protected FrameLayout layoutContainer;
    protected IInAppBillingService mService;
    protected Handler handler;
    protected Thread thread1;
    protected Thread thread2;
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
    protected PopupWindow popupWindow;
    protected EditText editEmailRegistration;
    private LayoutInflater inflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutContainer = (FrameLayout) view.findViewById(R.id.layout_login_container);

        iabHelper = new IabHelper(getActivity(), VipAndPremiumActivity.base64EncodedPublicKey);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
                Log.d(TAG, "In-app Billing setting up success: " + result);
            }
        });

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        handler = new Handler();
    }

    protected void showAllProducts() {

        ArrayList<String> skuList = new ArrayList<>();
        skuList.add(PREMIUM);
        skuList.add(VIP);
        final Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    skuDetails = mService.getSkuDetails(3, getContext().getPackageName(), "subs", querySkus);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int response = skuDetails.getInt("RESPONSE_CODE");
                if (response == 0) {
                    ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");

                    for (String thisResponse : responseList) {
                        JSONObject object = null;
                        String sku = null;
                        String price = null;
                        try {
                            object = new JSONObject(thisResponse);
                            sku = object.getString("productId");
                            price = object.getString("price");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "price = " + price);
                        Log.d(TAG, "sku = " + sku);

                    }
                }

            }
        });

        handler.post(thread1);
        handler.post(thread2);
    }

    protected void initPopupWindow(View view) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutContainer.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.showAtLocation(layoutContainer, Gravity.CENTER, 0, 0);
    }

    protected void toPayStatus(String status) {

        showAllProducts();

        try {
            buyIntentBundle = mService.getBuyIntent(3, getContext().getPackageName(), status, "subs", VipAndPremiumActivity.base64EncodedPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
        try {
            getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
                    REQUEST_CODE_BUY, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (iabHelper != null) {
            try {
                iabHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        iabHelper = null;

        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }
    }

    protected void send(final int amountOfCredits, final String creditType) {

        String accessToken = sharedHelper.getAccessToken();
        if (!TextUtils.isEmpty(accessToken)) {
            BuyCreditRequest request = new BuyCreditRequest(creditType, amountOfCredits, PURCHASE_TRANSACTION_ID, APP_TYPE);
            userRepository.buyCredits(accessToken, request, new GetCreditCallbackImpl());


        } else {
            AuthActivity.login(getActivity(), REQUEST_AUTH_CODE);
        }
    }

    private class GetCreditCallbackImpl implements GetCreditsCallback {

        @Override
        public void onSuccess(CreditsResponse creditsResponse) {
            if ("OK".equals(creditsResponse.getStatus())) {
                if (creditsResponse != null) {
                    Log.i(TAG, "onSuccess: dataCreditResponse.vip = " +
                            creditsResponse.getData().getCredit().getVip() +
                            ", dataCreditResponse.premium = " + creditsResponse.getData().getCredit().getPremium());
                } else {
                    Log.i(TAG, "onSuccess: dataCreditResponse == null");
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

}
