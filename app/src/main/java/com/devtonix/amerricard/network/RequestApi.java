package com.devtonix.amerricard.network;


import com.devtonix.amerricard.network.request.CreateEventRequest;
import com.devtonix.amerricard.network.request.EditEventRequest;
import com.devtonix.amerricard.network.response.CardResponseNew;
import com.devtonix.amerricard.network.response.CelebrityResponse;
import com.devtonix.amerricard.network.response.EventResponse;
import com.devtonix.amerricard.network.response.SettingsResponse;
import com.devtonix.amerricard.network.response.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestApi {

    /**
     * Sharing and retrieving cards
     * */

    @POST("card/{cardId}/share")
    Call<SimpleResponse> shareCard(@Path("cardId") long cardId);

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
}
