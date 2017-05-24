package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.devtonix.amerricard.api.event.CardSuccessEvent;
import com.devtonix.amerricard.api.event.EventSuccessEvent;
import com.devtonix.amerricard.api.event.FailureEvent;
import com.devtonix.amerricard.api.event.RxBus;
import com.devtonix.amerricard.api.event.SuccessEvent;
import com.devtonix.amerricard.model.Item;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

    private CompositeSubscription subscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscriptions = new CompositeSubscription();
        subscriptions.add(RxBus.getInstance().toObserverable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {

                        if (event instanceof CardSuccessEvent) {
                            handleCardSuccessEvent(((CardSuccessEvent) event).getItem());
                        } else if (event instanceof EventSuccessEvent) {
                            handleEventSuccessEvent(((EventSuccessEvent) event).getItem());
                        } else if (event instanceof SuccessEvent) {
                            handleSuccessEvent(((SuccessEvent) event).getMessage());
                        } else if (event instanceof FailureEvent) {
                            handleFailureFound(((FailureEvent) event).getMessage());
                        }
                    }
                }));
    }

    protected void handleEventSuccessEvent(List<Item> item) {

    }

    protected void handleCardSuccessEvent(List<Item> item) {
        Log.d("handleCardSuccessEvent", "item " + item.size());
    }

    protected void handleFailureFound(String message) {

    }

    protected void handleSuccessEvent(String message) {

    }



    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.clear();
    }

}
