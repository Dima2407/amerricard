package com.devtonix.amerricard.repository;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.network.API;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.network.request.BuyCreditRequest;
import com.devtonix.amerricard.network.request.ForgotPasswordRequest;
import com.devtonix.amerricard.network.request.LoginRequest;
import com.devtonix.amerricard.network.request.RegistrationRequest;
import com.devtonix.amerricard.network.response.GetCreditsResponse;
import com.devtonix.amerricard.network.response.LoginResponse;
import com.devtonix.amerricard.network.response.RegistrationResponse;
import com.devtonix.amerricard.network.response.SimpleResponse;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.callback.ForgotPasswordCallback;
import com.devtonix.amerricard.ui.callback.GetCreditsCallback;
import com.devtonix.amerricard.ui.callback.LoginCallback;
import com.devtonix.amerricard.ui.callback.RegistrationCallback;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    @Inject
    API api;
    @Inject
    SharedHelper sharedHelper;

    private Context context;

    public UserRepository(Context context) {
        ACApplication.getMainComponent().inject(this);
        this.context = context;
    }

    public void login(String email, String password, final LoginCallback callback) {
        Call<LoginResponse> call = api.login(new LoginRequest(email, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().getData().getToken());
                    sharedHelper.setName(response.body().getData().getName());
                    sharedHelper.setEmail(response.body().getData().getEmail());

                    Log.d("MyLog", "onResponse: " + response.body().getData().getName());
                    Log.d("MyLog", "onResponse: " + response.body().getData().getEmail());
                    Log.d("MyLog", "onResponse: " + response.body().getData().getToken());
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void registration(String email, String password, String name, final RegistrationCallback callback) {
        Call<RegistrationResponse> call = api.registration(new RegistrationRequest(name, password, email));
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().getToken());
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void forgotPassword(String login, String email, final ForgotPasswordCallback callback) {
        Call<SimpleResponse> call = api.forgotPassword(new ForgotPasswordRequest(login, email));
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void getCredits(String token, final GetCreditsCallback callback) {
        Call<GetCreditsResponse> call = api.getCredits(token);
        call.enqueue(new Callback<GetCreditsResponse>() {
            @Override
            public void onResponse(Call<GetCreditsResponse> call, Response<GetCreditsResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<GetCreditsResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void buyCredits(String token, BuyCreditRequest buyCreditRequest, final GetCreditsCallback callback) {
        Call<GetCreditsResponse> call = api.buyCredits(token, buyCreditRequest);
        call.enqueue(new Callback<GetCreditsResponse>() {
            @Override
            public void onResponse(Call<GetCreditsResponse> call, Response<GetCreditsResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<GetCreditsResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public boolean isAuthorized() {
        return !TextUtils.isEmpty(sharedHelper.getAccessToken());
    }
}
