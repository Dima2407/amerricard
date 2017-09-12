package com.devtonix.amerricard.network;


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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestApi {

    /**
     * Sharing and retrieving cards
     * */

    @POST("card/{cardId}/share")
    Call<CreditsResponse> shareCard(@Header("X-AMCA-APP-USER-TOKEN") String token, @Path("cardId") long cardId);


    @POST("card/{cardId}/share")
    Call<CreditsResponse> shareCard(@Path("cardId") long cardId);

    @POST("card/{cardId}/favorite")
    Call<SimpleResponse> addFavoriteCard(@Path("cardId") long cardId);

    @DELETE("card/{cardId}/favorite")
    Call<SimpleResponse> deleteFavoriteCard(@Path("cardId") long cardId);

    @GET("card")
    Call<CardResponseNew> getCard();

    /**
     * CRUD event
     * */

    @POST("event")
    Call<SimpleResponse> createEvent(@Body CreateEventRequest createEventRequest);

    @PUT("event/{eventId}")
    Call<SimpleResponse> editEvent(@Body EditEventRequest editEventRequest);

    @DELETE("event/{eventId}")
    Call<SimpleResponse> deleteEvent(@Path("eventId") long eventId);

    @GET("event")
    Call<EventResponse> getEvents();

    /**
     * Celebrities
     * */

    @GET("celebrity")
    Call<CelebrityResponse> getCelebrities();

    /**
     * Settings
     * */

    @GET("settings")
    Call<SettingsResponse> getSettings();

    /**
     * User
     * */

    @POST("appuser/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("appuser/registration")
    Call<RegistrationResponse> registration(@Body RegistrationRequest registrationRequest);

    @POST("appuser/forgotPassword")
    Call<SimpleResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @GET("appuser/credit")
    Call<CreditsResponse> getCredits(@Header("X-AMCA-APP-USER-TOKEN") String token);

    @POST("appuser/credit")
    Call<CreditsResponse> buyCredits(@Header("X-AMCA-APP-USER-TOKEN") String token, @Body BuyCreditRequest buyCreditRequest);

}
