package com.devtonix.amerricard.model;

public interface BaseEvent {

    int TYPE_EVENT = 0;
    int TYPE_CONTACT = 1;
    int TYPE_CELEBRITY = 2;

    String getEventDate();
    Name getEventName();
    int getEventType();
    String getEventImage();
}
