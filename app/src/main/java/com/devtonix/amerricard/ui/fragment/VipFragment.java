package com.devtonix.amerricard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.devtonix.amerricard.R;


public class VipFragment extends VipAndPremiumAbstractFragment {

    private static final String TAG = VipFragment.class.getSimpleName();
    private static final String CREDIT_TYPE_VIP = "vip";

    private RadioButton radioButton_1;
    private RadioButton radioButton_3;
    private RadioButton radioButton_5;
    private RadioButton radioButton_10;
    private Button btnPay;
    private int amountOfCredits = 10;
    private TextView coinsTextView;


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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coinsTextView = (TextView) view.findViewById(R.id.txt_available_for_sending_vip);
        coinsTextView.setText(String.valueOf(sharedHelper.getValueVipCoins()));

        btnPay = (Button) view.findViewById(R.id.btn_pay_vip);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(null, null, null);

            }
        });

        radioButton_1 = (RadioButton) view.findViewById(R.id.radio_btn_vip_1_card);
        radioButton_3 = (RadioButton) view.findViewById(R.id.radio_btn_vip_3_cards);
        radioButton_5 = (RadioButton) view.findViewById(R.id.radio_btn_vip_5_cards);
        radioButton_10 = (RadioButton) view.findViewById(R.id.radio_btn_vip_10_cards);


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
    protected int getAmountOfCredits() {
        return amountOfCredits;
    }

    @Override
    protected String getCreditsType() {
        return CREDIT_TYPE_VIP;
    }

    @Override
    protected void onCoinsUpdated(int vipCoinsCount, int premiumCoinsCount) {
        coinsTextView.setText(String.valueOf(vipCoinsCount));
    }
}
