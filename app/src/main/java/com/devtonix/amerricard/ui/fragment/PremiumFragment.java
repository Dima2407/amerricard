package com.devtonix.amerricard.ui.fragment;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.devtonix.amerricard.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PremiumFragment extends VipAndPremiumAbstractFragment {


    private static final String TAG = PremiumFragment.class.getSimpleName();
    private static final String CREDIT_TYPE_PREMIUM = "premium";

    private RadioButton radioButton_1;
    private RadioButton radioButton_3;
    private RadioButton radioButton_5;
    private RadioButton radioButton_10;
    private Button btnPay;
    private int amountOfCredits = 10;

    public static CategoryFragment getInstance(String url) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle b = new Bundle();
        b.putString("url", url);
        categoryFragment.setArguments(b);
        return categoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_premium, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnPay = (Button) view.findViewById(R.id.btn_pay_premium);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(amountOfCredits, CREDIT_TYPE_PREMIUM);
            }
        });

        radioButton_1 = (RadioButton) view.findViewById(R.id.radio_btn_premium_1_card);
        radioButton_3 = (RadioButton) view.findViewById(R.id.radio_btn_premium_3_cards);
        radioButton_5 = (RadioButton) view.findViewById(R.id.radio_btn_premium_5_cards);
        radioButton_10 = (RadioButton) view.findViewById(R.id.radio_btn_premium_10_cards);

        radioButton_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    amountOfCredits = 1;
                    radioButton_3.setChecked(false);
                    radioButton_5.setChecked(false);
                    radioButton_10.setChecked(false);
                }
            }
        });
        radioButton_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    amountOfCredits = 3;
                    radioButton_1.setChecked(false);
                    radioButton_5.setChecked(false);
                    radioButton_10.setChecked(false);
                }
            }
        });
        radioButton_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    amountOfCredits = 5;
                    radioButton_1.setChecked(false);
                    radioButton_3.setChecked(false);
                    radioButton_10.setChecked(false);
                }
            }
        });
        radioButton_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    amountOfCredits = 10;
                    radioButton_3.setChecked(false);
                    radioButton_5.setChecked(false);
                    radioButton_1.setChecked(false);
                }
            }
        });
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

            case REQUEST_AUTH_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    send(amountOfCredits, CREDIT_TYPE_PREMIUM);
                }
                break;
        }
    }

}

