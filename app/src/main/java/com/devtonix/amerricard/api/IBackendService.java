package com.devtonix.amerricard.api;

import com.devtonix.amerricard.api.response.ServerResponse;
import com.devtonix.amerricard.api.response.SimpleResponse;
import com.devtonix.amerricard.model.Item;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface IBackendService {

    @GET("card")
    Observable<ServerResponse<Item>> getCard();

    @GET("event")
    Observable<ServerResponse<Item>> getEvents();

    @POST("card/{cardId}/share")
    Observable<SimpleResponse> shareCard(@Path("cardId") long cardId);
}
