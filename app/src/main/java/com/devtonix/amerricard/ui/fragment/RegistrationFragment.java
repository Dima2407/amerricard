package com.devtonix.amerricard.ui.fragment;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.repository.UserRepository;
import com.devtonix.amerricard.ui.activity.auth.AuthDelegate;
import com.devtonix.amerricard.ui.callback.RegistrationCallback;
import com.google.android.gms.common.AccountPicker;

import javax.inject.Inject;

public class RegistrationFragment extends BaseFragment {
    private static final String TAG = RegistrationFragment.class.getSimpleName();
    private static final int REQUEST_ACCOUNT_PICK_CODE = 99;

    @Inject
    UserRepository userRepository;

    private EditText editEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACApplication.getMainComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText editLogin = (EditText) view.findViewById(R.id.edit_login_registration);
        editEmail = (EditText) view.findViewById(R.id.edit_email_registration);
        final EditText editPassword = (EditText) view.findViewById(R.id.edit_password_registration);
        final EditText editName = (EditText) view.findViewById(R.id.edit_name_registration);
        view.findViewById(R.id.btn_back_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOwnerActivity().cancel();
            }
        });
        view.findViewById(R.id.btn_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRepository.registration(editEmail.getText().toString(),
                        editPassword.getText().toString(),
                        editLogin.getText().toString(),
                        editName.getText().toString(),
                        new RegistrationCallbackImpl());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google", "com.google.android.legacymap"},
                false, null, null, null, null);
        startActivityForResult(intent, REQUEST_ACCOUNT_PICK_CODE);
    }

    private AuthDelegate getOwnerActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof AuthDelegate) {
            return (AuthDelegate) activity;
        }
        throw new UnsupportedOperationException("Activity mast implement " + AuthDelegate.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACCOUNT_PICK_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                editEmail.setText(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
            }
        }
    }

    private class RegistrationCallbackImpl implements RegistrationCallback {
        @Override
        public void onSuccess(String token, String status) {
            switch (status) {
                case "OK": {
                    Toast.makeText(getActivity(), R.string.registration_successful, Toast.LENGTH_LONG).show();
                    sharedHelper.setAccessToken(token);
                    Log.i(TAG, "RegistrationCallbackImpl onSuccess: token = " + token);
                    getOwnerActivity().close();
                    return;
                }
                case "generalError": {
                    Toast.makeText(getActivity(), R.string.service_unavailable, Toast.LENGTH_LONG).show();
                    break;
                }
                case "missingData": {
                    Toast.makeText(getActivity(), R.string.missing_fields, Toast.LENGTH_LONG).show();
                    break;
                }
                case "loginAlreadyExists": {
                    Toast.makeText(getActivity(), R.string.login_already_exists, Toast.LENGTH_LONG).show();
                    break;
                }
                case "emailAlreadyExists": {
                    Toast.makeText(getActivity(), R.string.email_already_exists, Toast.LENGTH_LONG).show();
                    break;
                }
                case "invalidPassword": {
                    Toast.makeText(getActivity(), R.string.invalid_password, Toast.LENGTH_LONG).show();
                    break;
                }
                case "invalidEmail": {
                    Toast.makeText(getActivity(), R.string.invalid_email, Toast.LENGTH_LONG).show();
                    break;
                }
                default: {
                    getOwnerActivity().cancel();
                }
            }

        }

        @Override
        public void onError() {
            Toast.makeText(getActivity(), "Service is temporarily unavailable", Toast.LENGTH_LONG).show();
            Log.i(TAG, "RegistrationCallbackImpl onError: ");
        }

        @Override
        public void onRetrofitError(String message) {
            Toast.makeText(getActivity(), "Service is temporarily unavailable", Toast.LENGTH_LONG).show();
            Log.i(TAG, "RegistrationCallbackImpl onRetrofitError: message = " + message);
        }
    }
}
