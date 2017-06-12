package com.devtonix.amerricard.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.devtonix.amerricard.AmerriCardsApp;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.api.event.CardSuccessEvent;
import com.devtonix.amerricard.api.event.EventSuccessEvent;
import com.devtonix.amerricard.api.event.FailureEvent;
import com.devtonix.amerricard.api.event.RxBus;
import com.devtonix.amerricard.api.request.CreateEventRequest;
import com.devtonix.amerricard.api.request.EditEventRequest;
import com.devtonix.amerricard.api.response.ServerResponse;
import com.devtonix.amerricard.api.response.SimpleResponse;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.utils.SystemUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NetworkService {
    private static final String TAG = NetworkService.class.getSimpleName();

    public static void getCards(Context context) {
        try {
            NetworkServiceProvider provider = new NetworkServiceProvider();

            if (!SystemUtils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.getCard().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ServerResponse<Item>>() {
                        @Override
                        public final void onCompleted() {
                        }

                        @Override
                        public final void onError(Throwable e) {
//                            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
                        }

                        @Override
                        public final void onNext(ServerResponse<Item> response) {
                            RxBus.getInstance().send(new CardSuccessEvent(response.data));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
        }
    }

    public static void getEvents(Context context) {
        try {
            NetworkServiceProvider provider = new NetworkServiceProvider();

            if (!SystemUtils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.getEvents().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ServerResponse<Item>>() {
                        @Override
                        public final void onCompleted() {
                        }

                        @Override
                        public final void onError(Throwable e) {
//                            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
                        }

                        @Override
                        public final void onNext(ServerResponse<Item> response) {
                            RxBus.getInstance().send(new EventSuccessEvent(response.data));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
        }
    }

    public static void shareCard(Context context, long cardId) {
        try {
            final NetworkServiceProvider provider = new NetworkServiceProvider();

            if (!SystemUtils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.shareCard(cardId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SimpleResponse>() {

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
//                            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
                        }

                        @Override
                        public void onNext(SimpleResponse response) {
//                            RxBus.getInstance().send();
                            Log.d(TAG, "getStatus = " + response.getStatus() + "  getCode = " + response.getCode());

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            RxBus.getInstance().send(e.getMessage());
        }
    }

    public static void createEvent(CreateEventRequest createEventRequest, Context context) {
        try {
            final NetworkServiceProvider provider = new NetworkServiceProvider();
            if (!SystemUtils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.createEvent(createEventRequest)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SimpleResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            RxBus.getInstance().send(e.getMessage());
        }
    }

    public static void editEvent(EditEventRequest editEventRequest, Context context) {
        try {
            final NetworkServiceProvider provider = new NetworkServiceProvider();
            if (!SystemUtils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.editEvent(editEventRequest)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SimpleResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            RxBus.getInstance().send(e.getMessage());
        }
    }

    public static void deleteEvent(long id, Context context) {
        try {
            final NetworkServiceProvider provider = new NetworkServiceProvider();
            if (!SystemUtils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.deleteEvent(id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SimpleResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            RxBus.getInstance().send(e.getMessage());
        }
    }

    public static void addFavoriteCard(long id, Context context) {
        try {
            final NetworkServiceProvider provider = new NetworkServiceProvider();
            if (!SystemUtils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.addFavoriteCard(id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SimpleResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            RxBus.getInstance().send(e.getMessage());
        }
    }

    public static void deleteFavoriteCard(long id, Context context) {
        try {
            final NetworkServiceProvider provider = new NetworkServiceProvider();
            if (!SystemUtils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.deleteFavoriteCard(id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SimpleResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            RxBus.getInstance().send(e.getMessage());
        }
    }

    private static void showErrorConnection() {
        Toast.makeText(AmerriCardsApp.getInstance(), R.string.error_no_internet, Toast.LENGTH_SHORT).show();
    }
}

