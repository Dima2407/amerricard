package com.devtonix.amerricard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.BillingProductType;
import com.devtonix.amerricard.network.request.BuyCreditRequest;
import com.devtonix.amerricard.network.response.CreditsResponse;
import com.devtonix.amerricard.repository.UserRepository;
import com.devtonix.amerricard.ui.activity.VipAndPremiumActivity;
import com.devtonix.amerricard.ui.activity.auth.AuthActivity;
import com.devtonix.amerricard.ui.callback.GetCreditsCallback;

import javax.inject.Inject;

public abstract class VipAndPremiumAbstractFragment extends BaseFragment {

    protected static final int REQUEST_AUTH_CODE = 10;
    @Inject
    UserRepository userRepository;

    private static final String TAG = VipAndPremiumAbstractFragment.class.getSimpleName();

    protected FrameLayout layoutContainer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutContainer = (FrameLayout) view.findViewById(R.id.layout_login_container);

    }

    private void send(String productId, String orderId, String purchaseToken, Runnable complete) {

        String accessToken = sharedHelper.getAccessToken();
        BuyCreditRequest request = new BuyCreditRequest();
        request.setCreditType(getProductType().getType());
        request.setCredits(getProductType().getCredits());
        request.setProductId(productId);
        request.setPurchaseTransactionId(orderId);
        request.setPurchaseToken(purchaseToken);
        userRepository.buyCredits(accessToken, request, new GetCreditCallbackImpl(complete));
    }

    protected final void buy() {
        if (TextUtils.isEmpty(sharedHelper.getAccessToken())) {
            AuthActivity.login(getActivity(), REQUEST_AUTH_CODE);
            return;
        }
        VipAndPremiumActivity activity = (VipAndPremiumActivity) getActivity();
        activity.payFromGoogle(getProductType().getProductId());
    }

    public final void buy(String productId, String orderId, String purchaseToken){
        if (TextUtils.isEmpty(productId)) {
            buy();
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

    protected abstract BillingProductType getProductType();

    protected abstract void onCoinsUpdated(int vipCoinsCount, int premiumCoinsCount);
}
