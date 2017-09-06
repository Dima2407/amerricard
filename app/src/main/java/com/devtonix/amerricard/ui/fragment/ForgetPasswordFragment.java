package com.devtonix.amerricard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.repository.UserRepository;
import com.devtonix.amerricard.ui.activity.auth.AuthActivity;
import com.devtonix.amerricard.ui.activity.auth.AuthDelegate;
import com.devtonix.amerricard.ui.callback.ForgotPasswordCallback;

import javax.inject.Inject;

public class ForgetPasswordFragment extends BaseFragment {

    private static final String TAG = ForgetPasswordFragment.class.getSimpleName();
    private static final int AUTH_REQUEST_CODE = 20;
    private Button forgotPassButton;

    @Inject
    UserRepository userRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText editLogin = (EditText) view.findViewById(R.id.edit_name_forgot_password);
        final EditText editEmail = (EditText) view.findViewById(R.id.edit_email_forgot_password);
        forgotPassButton = (Button) view.findViewById(R.id.btn_forgot_password);
        view.findViewById(R.id.btn_back_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOwnerActivity().cancel();
            }
        });
        forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRepository.forgotPassword(editLogin.getText().toString(), editEmail.getText().toString(), new ForgotPasswordCallbackImpl());
                forgotPassButton.setClickable(false);
                Log.d("MyButton", "onClick: ");
            }
        });
    }

    private AuthDelegate getOwnerActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof AuthDelegate) {
            return (AuthDelegate) activity;
        }
        throw new UnsupportedOperationException("Activity mast implement " + AuthDelegate.class.getName());
    }

    private class ForgotPasswordCallbackImpl implements ForgotPasswordCallback {

        @Override
        public void onSuccess(String status) {
            Log.i(TAG, "ForgotPasswordCallbackImpl onSuccess: ");
            forgotPassButton.setClickable(true);
            switch (status) {
                case "OK": {
                    Toast.makeText(getActivity(), R.string.ok_forgot_pass, Toast.LENGTH_LONG).show();
                    AuthActivity.login(getActivity(), AUTH_REQUEST_CODE);
                    getOwnerActivity().close();
                    break;
                }
                case "invalidLoginEmail": {
                    Toast.makeText(getActivity(), R.string.invalid_login_email, Toast.LENGTH_LONG).show();
                    break;
                }
                case "generalError": {
                    Toast.makeText(getActivity(), R.string.common_server_error, Toast.LENGTH_LONG).show();
                    break;
                }
                default: {
                    getOwnerActivity().close();
                }
            }

        }

        @Override
        public void onError() {
            Toast.makeText(getActivity(), R.string.service_unavailable, Toast.LENGTH_LONG).show();
            Log.i(TAG, "ForgotPasswordCallbackImpl onError: ");
        }

        @Override
        public void onRetrofitError(String message) {
            Toast.makeText(getActivity(), R.string.service_unavailable, Toast.LENGTH_LONG).show();
            Log.i(TAG, "ForgotPasswordCallbackImpl onRetrofitError: message = " + message);
        }
    }
}
