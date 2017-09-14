package com.devtonix.amerricard.ui.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.devtonix.amerricard.R;

import com.devtonix.amerricard.ui.adapter.VipPagerAdapter;
import com.devtonix.amerricard.utils.BillingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.devtonix.amerricard.billing.IabHelper.BILLING_RESPONSE_RESULT_OK;

public class VipAndPremiumActivity extends DrawerActivity {

    private static final String TAG = VipAndPremiumActivity.class.getSimpleName();


    public static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzLiI3hjLJQjyFbj1JK8U6QM46jX7ZEW7xsKAqGgrVuXejydWOi+f0tyAVyRM6UPQOEygJWfnVNr5Fabg43KacO6HMPytpJt83wL3s0hwcx8jicqj/L3WIQfGPEYrvLXDubtzwDT4Wqc6YtY17BQNyEXDOEZ6PXMQBiIxNDvYALGpVXChS8xNmadeTlbkiUeBrvl75eJUs6CMc/wzKiB2P5JH1geHMk2dg6/+p+v7UdtRDzkfWiPKAJZ5Vm6h7WPL4VdeRB+KlIHD6+2AAxqKLbyicUbe27NL3ihTcr9+YTMuvgB0acWmPejMrICGOz7XG5WHSqo4anV2L62iJVUjqQIDAQAB";
    public static final String TAB_POSITION = "tab_position";
    public static final String SHOW_VIP_ACTION = "action_vip";
    public static final String SHOW_PREMIUM_ACTION = "action_premium";

    protected final static int REQUEST_CODE_BUY = 1001;

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


    protected IInAppBillingService mService;

    private VipPagerAdapter adapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_vip_and_premium);

        setTitle(getString(R.string.become_vip_title));

        pager = (ViewPager) findViewById(R.id.vip_view_pager);
        adapter = new VipPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        TabLayout tab = (TabLayout) findViewById(R.id.vip_tab_layout);
        tab.setTabTextColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(android.R.color.white));
        tab.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tab.setupWithViewPager(pager);

        final int tabPosition = getIntent().getIntExtra(TAB_POSITION, 0);
        pager.setCurrentItem(tabPosition);

        if (getIntent().getAction() != null) {
            switch (getIntent().getAction()) {
                case SHOW_VIP_ACTION:
                    pager.setCurrentItem(0);
                    break;
                case SHOW_PREMIUM_ACTION:
                    pager.setCurrentItem(1);
                    break;
            }
        }

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        payFromServer(productId, orderId, purchaseToken);

                    } catch (JSONException e) {
                        Log.e(TAG, "onActivityResult: ", e);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void payFromServer(String productId, String orderId, String purchaseToken) {
        adapter.getActiveFragment(pager.getCurrentItem()).buy(productId, orderId, purchaseToken);
    }


    public void payFromGoogle(int count, String type) {

        String productId = String.format("%s_%d", type, count);

        Bundle buyIntentBundle;
        try {
            buyIntentBundle = mService.getBuyIntent(3, getPackageName(), productId, "inapp", base64EncodedPublicKey);
        } catch (Exception e) {
            Log.e(TAG, "payFromGoogle: ", e);
            return;
        }
        int responseCode = buyIntentBundle.getInt("RESPONSE_CODE");

        if(responseCode == BillingUtils.BILLING_RESPONSE_RESULT_OK) {

            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            if (pendingIntent == null) {
                Toast.makeText(this, R.string.google_service_unavailable, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                startIntentSenderForResult(pendingIntent.getIntentSender(),
                        REQUEST_CODE_BUY, new Intent(), 0, 0, 0);
            } catch (Exception e) {
                Log.e(TAG, "payFromGoogle: ", e);
            }
        }else {
            Toast.makeText(this, BillingUtils.getError(responseCode), Toast.LENGTH_LONG).show();
        }
    }
}
