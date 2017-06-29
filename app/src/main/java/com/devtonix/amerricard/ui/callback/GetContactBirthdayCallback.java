package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.model.Contact;

import java.util.List;

public interface GetContactBirthdayCallback {

    void onSuccess(List<Contact> contactsAndBirthdays);
}
