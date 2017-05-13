package com.devtonix.amerricard.api;

import android.content.Context;
import android.widget.Toast;

import com.devtonix.amerricard.AmerriCardsApp;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.api.event.CardSuccessEvent;
import com.devtonix.amerricard.api.event.EventSuccessEvent;
import com.devtonix.amerricard.api.event.FailureEvent;
import com.devtonix.amerricard.api.event.RxBus;
import com.devtonix.amerricard.api.response.ServerResponse;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.utils.Utils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NetworkService {

    public static void getCards(Context context) {
        try {
            NetworkServiceProvider provider = new NetworkServiceProvider();

            if (!Utils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.getCard().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ServerResponse<Item>>() {
                        @Override
                        public final void onCompleted() {}

                        @Override
                        public final void onError(Throwable e) {
                            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
                        }

                        @Override
                        public final void onNext(ServerResponse<Item> response) {
                            RxBus.getInstance().send(new CardSuccessEvent(response.data));
                        }
                    });
        } catch (Exception e) {
            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
        }
    }

    public static void getEvents(Context context) {
        try {
            NetworkServiceProvider provider = new NetworkServiceProvider();

            if (!Utils.isNetworkConnected(context)) {
                showErrorConnection();
                return;
            }

            provider.service.getEvents().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ServerResponse<Item>>() {
                        @Override
                        public final void onCompleted() {}

                        @Override
                        public final void onError(Throwable e) {
                            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
                        }

                        @Override
                        public final void onNext(ServerResponse<Item> response) {
                            RxBus.getInstance().send(new EventSuccessEvent(response.data));
                        }
                    });
        } catch (Exception e) {
            RxBus.getInstance().send(new FailureEvent(e.getMessage()));
        }
    }

    private static void showErrorConnection() {
        Toast.makeText(AmerriCardsApp.getInstance(), R.string.error_no_internet, Toast.LENGTH_SHORT).show();
    }
}

