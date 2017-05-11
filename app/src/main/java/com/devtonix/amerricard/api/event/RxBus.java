package com.devtonix.amerricard.api.event;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    private static RxBus instance;

    private Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());


    public static RxBus getInstance() {
        if (instance == null) {
           instance = new RxBus();
        }
        return instance;
    }

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}