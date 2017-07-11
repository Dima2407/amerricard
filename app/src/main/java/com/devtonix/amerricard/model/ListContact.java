package com.devtonix.amerricard.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListContact {

    private Set<Contact> contacts;

    public ListContact(List<Contact> contactList) {
        contacts = new HashSet<>();
        for (Contact contact : contactList) {
            contacts.add(contact);
        }
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getListContacs() {
        List<Contact> contactList = new ArrayList<>();
        for (Contact contact : contacts) {
            contactList.add(contact);
        }
        return contactList;
    }

}
