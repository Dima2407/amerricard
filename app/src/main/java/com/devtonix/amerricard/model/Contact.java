package com.devtonix.amerricard.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class Contact {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

    private String name;
    private String birthday;
    private String photoUri;

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

//    public String getFormattedDate() {
//        long d = ;
//        return dateFormat.format(new Date(d));
//    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (getName() != null ? !getName().equals(contact.getName()) : contact.getName() != null)
            return false;
        if (getBirthday() != null ? !getBirthday().equals(contact.getBirthday()) : contact.getBirthday() != null)
            return false;
        return getPhotoUri() != null ? getPhotoUri().equals(contact.getPhotoUri()) : contact.getPhotoUri() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getBirthday() != null ? getBirthday().hashCode() : 0);
        result = 31 * result + (getPhotoUri() != null ? getPhotoUri().hashCode() : 0);
        return result;
    }
}
