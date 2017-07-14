package com.devtonix.amerricard.network;

import com.devtonix.amerricard.network.request.CreateEventRequest;
import com.devtonix.amerricard.network.request.EditEventRequest;
import com.devtonix.amerricard.network.response.CardResponseNew;
import com.devtonix.amerricard.network.response.CelebrityResponse;
import com.devtonix.amerricard.network.response.EventResponse;
import com.devtonix.amerricard.network.response.SimpleResponse;
import com.devtonix.amerricard.storage.SharedHelper;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    public Call<SimpleResponse> shareCard(long cardId){
        if(helper.getCurrentServer()){
            return server1.shareCard(cardId);
        }else {
            return server2.shareCard(cardId);
        }
    }

    public Call<SimpleResponse> addFavoriteCard(long cardId){
        if(helper.getCurrentServer()){
            return server1.addFavoriteCard(cardId);
        }else {
            return server2.addFavoriteCard(cardId);
        }
    }

    public Call<SimpleResponse> deleteFavoriteCard(long cardId){
        if(helper.getCurrentServer()){
            return server1.deleteFavoriteCard(cardId);
        }else {
            return server2.deleteFavoriteCard(cardId);
        }
    }

    public Call<CardResponseNew> getCard(){
        if(helper.getCurrentServer()){
            return server1.getCard();
        }else {
            return server2.getCard();
        }
    }

    public Call<SimpleResponse> createEvent(CreateEventRequest createEventRequest){
        if(helper.getCurrentServer()){
            return server1.createEvent(createEventRequest);
        }else {
            return server2.createEvent(createEventRequest);
        }
    }

    public Call<SimpleResponse> editEvent(EditEventRequest editEventRequest){
        if(helper.getCurrentServer()){
            return server1.editEvent(editEventRequest);
        }else {
            return server2.editEvent(editEventRequest);
        }
    }

    public Call<SimpleResponse> deleteEvent(long eventId){
        if(helper.getCurrentServer()){
            return server1.deleteEvent(eventId);
        }else {
            return server2.deleteEvent(eventId);
        }
    }

    public Call<EventResponse> getEvents(){
        if(helper.getCurrentServer()){
            return server1.getEvents();
        }else {
            return server2.getEvents();
        }
    }

    public Call<CelebrityResponse> getCelebrities(){
        if(helper.getCurrentServer()){
            return server1.getCelebrities();
        }else {
            return server2.getCelebrities();
        }
    }
}
