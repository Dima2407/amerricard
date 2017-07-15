package com.devtonix.amerricard.ui.fragment;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.billing.IabHelper;
import com.devtonix.amerricard.billing.IabResult;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.activity.BillingActivity;
import com.devtonix.amerricard.ui.activity.VipAndPremiumActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

public class VipFragment extends BaseFragment {

    private static final String TAG = VipFragment.class.getSimpleName();
    private final static String VIP = "vip_test";
    private final static String PREMIUM = "premium_test";
    private static final String RESPONSE_CODE = "RESPONSE_CODE";

    private IabHelper iabHelper;
    private Bundle skuDetails;
    private Bundle buyIntentBundle;
    private IInAppBillingService mService;
    private Handler handler;
    private Thread thread1;
    private Thread thread2;
    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Inject
    SharedHelper sharedHelper;


    private TextView tvBecomeVip;

    public static CategoryFragment getInstance(String url) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle b = new Bundle();
        b.putString("url", url);
        categoryFragment.setArguments(b);
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vip, null);
        tvBecomeVip = (TextView) view.findViewById(R.id.tvBecomeVip);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        tvBecomeVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPayVip();
            }
        });

    }

    private void showAllProducts() {

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

    private void toPayVip() {

        showAllProducts();

        try {
            buyIntentBundle = mService.getBuyIntent(3, getContext().getPackageName(), VIP, "subs", VipAndPremiumActivity.base64EncodedPublicKey);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
        try {
            getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
                    1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == Activity.RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
}
