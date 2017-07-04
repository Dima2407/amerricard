package com.devtonix.amerricard.model;

public class Contact implements BaseEvent {

    private String name;
    private String birthday;
    private String photoUri;
    private boolean isCancelled = false;

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

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }


    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
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

    @Override
    public String getEventDate() {
        return getBirthday();
    }

    @Override
    public Name getEventName() {
        Name name = new Name();
        name.setBaseName(getName());
        return name;
    }

    @Override
    public int getEventType() {
        return TYPE_CONTACT;
    }

    @Override
    public String getEventImage() {
        return getPhotoUri();
    }
}
