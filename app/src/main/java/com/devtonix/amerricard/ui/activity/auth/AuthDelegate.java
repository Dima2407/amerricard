package com.devtonix.amerricard.ui.activity.auth;

public interface AuthDelegate {
    void close();

    void forgotPassword();

    void registration();

    void cancel();
}
