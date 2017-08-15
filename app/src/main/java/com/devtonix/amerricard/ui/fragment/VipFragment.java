package com.devtonix.amerricard.ui.fragment;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devtonix.amerricard.R;

import org.json.JSONException;
import org.json.JSONObject;

public class VipFragment extends VipAndPremiumAbstractFragment {

    private static final String TAG = VipFragment.class.getSimpleName();

    public static CategoryFragment getInstance(String url) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle b = new Bundle();
        b.putString("url", url);
        categoryFragment.setArguments(b);
        return categoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vip, null);
        tvBecomeVipOpPremium = (TextView) view.findViewById(R.id.tvBecomeVip);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_BUY:
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
                break;

            case REQUEST_ACCOUNT_PICK_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    editEmailRegistration.setText(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                }
                break;
        }
    }

}
