package com.devtonix.amerricard.model;

import com.bumptech.glide.load.model.GlideUrl;

public interface BaseEvent {

    int TYPE_EVENT = 0;
    int TYPE_CONTACT = 1;
    int TYPE_CELEBRITY = 2;

    String getEventDate();
    Name getEventName();
    int getEventType();
    GlideUrl getEventImage();
    GlideUrl getThumbImageUrl();
}
