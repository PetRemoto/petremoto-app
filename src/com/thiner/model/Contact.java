package com.thiner.model;

import com.google.common.base.Preconditions;

public class Contact {

    private final String mOperadora;
    private final int mNumber;
    private final int mDDD;

    public Contact(final int firstName, final int secondName, final String operadora) {
        mDDD = Preconditions.checkNotNull(firstName);
        mNumber = Preconditions.checkNotNull(secondName);
        mOperadora = Preconditions.checkNotNull(operadora);
    }

    public String getOperadora() {
        return mOperadora;
    }

    public int getNumber() {
        return mNumber;
    }

    public int getDDD() {
        return mDDD;
    }

    @Override
    public boolean equals(final Object other) {
        final Contact contact;
        if (other instanceof Contact) {
            contact = (Contact) other;
        } else {
            return false;
        }

        return getDDD() == contact.getDDD() && getNumber() == contact.getNumber()
                && getOperadora().equals(contact.getOperadora());
    }
}
