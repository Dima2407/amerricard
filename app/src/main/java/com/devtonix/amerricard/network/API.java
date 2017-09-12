package com.devtonix.amerricard.network;

import android.text.TextUtils;

import com.devtonix.amerricard.network.request.BuyCreditRequest;
import com.devtonix.amerricard.network.request.CreateEventRequest;
import com.devtonix.amerricard.network.request.EditEventRequest;
import com.devtonix.amerricard.network.request.ForgotPasswordRequest;
import com.devtonix.amerricard.network.request.LoginRequest;
import com.devtonix.amerricard.network.request.RegistrationRequest;
import com.devtonix.amerricard.network.response.CardResponseNew;
import com.devtonix.amerricard.network.response.CelebrityResponse;
import com.devtonix.amerricard.network.response.EventResponse;
import com.devtonix.amerricard.network.response.CreditsResponse;
import com.devtonix.amerricard.network.response.LoginResponse;
import com.devtonix.amerricard.network.response.RegistrationResponse;
import com.devtonix.amerricard.network.response.SettingsResponse;
import com.devtonix.amerricard.network.response.SimpleResponse;
import com.devtonix.amerricard.storage.SharedHelper;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static final String BASE_URL_1 = "http://188.226.178.46:8888/amerricards/api/";
    private static final String BASE_URL_2 = "http://67.205.182.69:8080/amerricards/api/";

    private RequestApi server1;
    private RequestApi server2;
    private SharedHelper helper;

    public API(OkHttpClient client, SharedHelper helper) {

        this.server1 = new Retrofit.Builder()
                .baseUrl(BASE_URL_1)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RequestApi.class);
        this.server2 = new Retrofit.Builder()
                .baseUrl(BASE_URL_2)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RequestApi.class);
        this.helper = helper;
    }

    public Call<CreditsResponse> shareCard(String token, long cardId) {
        if (helper.getCurrentServer()) {
            if(TextUtils.isEmpty(token)){
                return server1.shareCard(cardId);
            }
            return server1.shareCard(token, cardId);
        } else {
            if(TextUtils.isEmpty(token)){
                return server2.shareCard(cardId);
            }
            return server2.shareCard(token, cardId);
        }
    }

    public Call<SimpleResponse> addFavoriteCard(long cardId) {
        if (helper.getCurrentServer()) {
            return server1.addFavoriteCard(cardId);
        } else {
            return server2.addFavoriteCard(cardId);
        }
    }

    public Call<SimpleResponse> deleteFavoriteCard(long cardId) {
        if (helper.getCurrentServer()) {
            return server1.deleteFavoriteCard(cardId);
        } else {
            return server2.deleteFavoriteCard(cardId);
        }
    }

    public Call<CardResponseNew> getCard() {
        if (helper.getCurrentServer()) {
            return server1.getCard();
        } else {
            return server2.getCard();
        }
    }

    public Call<SimpleResponse> createEvent(CreateEventRequest createEventRequest) {
        if (helper.getCurrentServer()) {
            return server1.createEvent(createEventRequest);
        } else {
            return server2.createEvent(createEventRequest);
        }
    }

    public Call<SimpleResponse> editEvent(EditEventRequest editEventRequest) {
        if (helper.getCurrentServer()) {
            return server1.editEvent(editEventRequest);
        } else {
            return server2.editEvent(editEventRequest);
        }
    }

    public Call<SimpleResponse> deleteEvent(long eventId) {
        if (helper.getCurrentServer()) {
            return server1.deleteEvent(eventId);
        } else {
            return server2.deleteEvent(eventId);
        }
    }

    public Call<EventResponse> getEvents() {
        if (helper.getCurrentServer()) {
            return server1.getEvents();
        } else {
            return server2.getEvents();
        }
    }

    public Call<CelebrityResponse> getCelebrities() {
        if (helper.getCurrentServer()) {
            return server1.getCelebrities();
        } else {
            return server2.getCelebrities();
        }
    }

    public Call<SettingsResponse> getSettings() {
        if (helper.getCurrentServer()) {
            return server1.getSettings();
        } else {
            return server2.getSettings();
        }
    }

    public Call<LoginResponse> login(LoginRequest loginRequest) {
        if (helper.getCurrentServer()) {
            return server1.login(loginRequest);
        } else {
            return server2.login(loginRequest);
        }
    }

    public Call<RegistrationResponse> registration(RegistrationRequest registrationRequest) {
        if (helper.getCurrentServer()) {
            return server1.registration(registrationRequest);
        } else {
            return server2.registration(registrationRequest);
        }
    }

    public Call<SimpleResponse> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        if (helper.getCurrentServer()) {
            return server1.forgotPassword(forgotPasswordRequest);
        } else {
            return server2.forgotPassword(forgotPasswordRequest);
        }
    }

    public Call<CreditsResponse> getCredits(String token) {
        if (helper.getCurrentServer()) {
            return server1.getCredits(token);
        } else {
            return server2.getCredits(token);
        }
    }

    public Call<CreditsResponse> buyCredits(String token, BuyCreditRequest buyCreditRequest) {
        if (helper.getCurrentServer()) {
            return server1.buyCredits(token, buyCreditRequest);
        } else {
            return server2.buyCredits(token, buyCreditRequest);
        }
    }
}
