package com.devtonix.amerricard.api;

import com.devtonix.amerricard.api.request.CreateEventRequest;
import com.devtonix.amerricard.api.request.EditEventRequest;
import com.devtonix.amerricard.api.response.ServerResponse;
import com.devtonix.amerricard.api.response.SimpleResponse;
import com.devtonix.amerricard.model.Item;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface IBackendService {


    @GET("card")
    Observable<ServerResponse<Item>> getCard();

    /**
     * share and favorite
     * */

    @POST("card/{cardId}/share")
    Observable<SimpleResponse> shareCard(@Path("cardId") long cardId);

    @POST("card/{cardId}/favorite")
    Observable<SimpleResponse> addFavoriteCard(@Path("cardId") long cardId);

    @DELETE("card/{cardId}/favorite")
    Observable<SimpleResponse> deleteFavoriteCard(@Path("cardId") long cardId);


    /**
     * CRUD event
     * */

    @POST("event")
    Observable<SimpleResponse> createEvent(@Body CreateEventRequest createEventRequest);

    @PUT("event/{eventId}")
    Observable<SimpleResponse> editEvent(@Body EditEventRequest editEventRequest);

    @DELETE("event/{eventId}")
    Observable<SimpleResponse> deleteEvent(@Path("eventId") long eventId);

    @GET("event")
    Observable<ServerResponse<Item>> getEvents();
}
