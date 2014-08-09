
package com.petremoto.model;

import com.google.common.base.Preconditions;

public class Dispenser implements Comparable<Dispenser> {
    private final String mSerial, mName, mId, mLastTimeChecked, mLastTimeFed;
    private final String mStatus;
    private final boolean mFeed;

    public Dispenser(final String serial, final String name, final String id,
            final String lastTimeChecked, final String lastTimeFed,
            final boolean feed, final String status) {
        mSerial = Preconditions.checkNotNull(serial);
        mName = Preconditions.checkNotNull(name);
        mId = Preconditions.checkNotNull(id);
        mLastTimeChecked = Preconditions.checkNotNull(lastTimeChecked);
        mLastTimeFed = Preconditions.checkNotNull(lastTimeFed);
        mStatus = Preconditions.checkNotNull(status);
        mFeed = feed;
    }

    public String getSerial() {
        return mSerial;
    }

    public String getName() {
        return mName;
    }

    public String getLastTimeFed() {
        return mLastTimeFed;
    }

    @Override
    public int compareTo(final Dispenser another) {
        return getSerial().compareTo(another.getSerial());
    }

}
