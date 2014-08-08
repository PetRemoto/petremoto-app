
package com.thiner.model;

import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Person implements Comparable<Person> {
    private final String mID;
    private final String mFirstName;
    private final String mSecondName;
    private final String mEmail;
    private final String mUsername;
    private final String mOperadora;
    private final ArrayList<Contact> mContacts;

    public Person(final String id, final String firstName, final String secondName,
            final String username,
            final String email, final String operadora) {
        this(id, firstName, secondName, username, email, operadora, new ArrayList<Contact>());
    }

    public Person(final String id, final String firstName, final String secondName,
            final String username,
            final String email, final String operadora, final ArrayList<Contact> contacts) {

        mID = id;
        mFirstName = Preconditions.checkNotNull(firstName);
        mSecondName = Preconditions.checkNotNull(secondName);
        mUsername = Preconditions.checkNotNull(username);
        mEmail = Preconditions.checkNotNull(email);
        mOperadora = Preconditions.checkNotNull(operadora);
        mContacts = Preconditions.checkNotNull(contacts);
    }

    public String getID() {
        return mID;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getSecondName() {
        return mSecondName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getOperadora() {
        return mOperadora;
    }

    public ArrayList<Contact> getContacts() {
        return mContacts;
    }

    @Override
    public int compareTo(final Person another) {
        return getFirstName().compareTo(another.getFirstName());
    }

}
