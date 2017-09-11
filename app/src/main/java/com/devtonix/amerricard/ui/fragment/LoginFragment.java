package com.devtonix.amerricard.ui.fragment;

import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.repository.UserRepository;
import com.devtonix.amerricard.ui.activity.auth.AuthDelegate;
import com.devtonix.amerricard.ui.callback.LoginCallback;

import javax.inject.Inject;

public class LoginFragment extends BaseFragment {
    private static final String TAG = LoginFragment.class.getSimpleName();

    @Inject
    UserRepository userRepository;

    private EditText textLogin;
    private EditText textPassword;
    private TextInputLayout textPasswordWrapper;
    private TextInputLayout textLoginWrapper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textLogin = (EditText) view.findViewById(R.id.edit_login);
        textPassword = (EditText) view.findViewById(R.id.edit_password);
        textLoginWrapper = (TextInputLayout) view.findViewById(R.id.edit_login_wrapper);
        textPasswordWrapper = (TextInputLayout) view.findViewById(R.id.edit_password_wrapper);

        textLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 30) {
                    textLoginWrapper.setError("Login is too long.");
                } else {
                    textLoginWrapper.setError("");
                }
            }
        });

        textPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doLogin();
                    return true;
                }

                return false;
            }
        });
        /*Button buttonBack = (Button) view.findViewById(R.id.btn_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOwnerActivity().cancel();
            }
        });*/
        Button buttonLogin = (Button) view.findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        view.findViewById(R.id.text_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOwnerActivity().forgotPassword();
            }
        });
        view.findViewById(R.id.btn_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOwnerActivity().registration();
            }
        });

    }

    private void doLogin() {
        if (textLogin.getText().length() > 25) {
            textLoginWrapper.setError("Username is too long.");
        } else {
            textLoginWrapper.setError("");
        }

        userRepository.login(textLogin.getText().toString(),
                textPassword.getText().toString(), new LoginCallbackImpl());


    }

    private AuthDelegate getOwnerActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof AuthDelegate) {
            return (AuthDelegate) activity;
        }
        throw new UnsupportedOperationException("Activity mast implement " + AuthDelegate.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private class LoginCallbackImpl implements LoginCallback {

        @Override
        public void onSuccess(String token, String status) {
            switch (status) {
                case "OK": {
                    Toast.makeText(getActivity(), R.string.ok_login, Toast.LENGTH_LONG).show();
                    getOwnerActivity().close();
                    sharedHelper.setAccessToken(token);

                    break;
                }
                case "INVALID_LOGIN_PASSWORD": {
                    Toast.makeText(getActivity(), R.string.invalid_login_email, Toast.LENGTH_LONG).show();
                    break;
                }
                case "GENERAL_ERROR": {
                    Toast.makeText(getActivity(), R.string.general_error, Toast.LENGTH_LONG).show();
                    break;
                }
                default: {
                    getOwnerActivity().cancel();
                }
            }
        }

        @Override
        public void onError() {
            Toast.makeText(getActivity(), R.string.service_unavailable, Toast.LENGTH_LONG).show();
            Log.i(TAG, "LoginCallbackImpl onError: ");
        }

        @Override
        public void onRetrofitError(String message) {
            Toast.makeText(getActivity(), R.string.service_unavailable, Toast.LENGTH_LONG).show();
            Log.i(TAG, "LoginCallbackImpl onRetrofitError: message = " + message);
        }
    }

}
