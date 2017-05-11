package com.devtonix.amerricard.api.event;

public class SuccessEvent {
    private String message;


    public SuccessEvent() {
        this.message = "";
    }

    public SuccessEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
