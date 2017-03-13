package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableLong;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hydra on 2016/4/3.
 *
 */
public class JObservableLong extends ObservableLong implements JDatabindingObservable<Long> {

    public JObservableLong(long value) {
        super(value);
    }

    public JObservableLong() {
        super(0L);
    }

    @Override
    public void setValue(Long value) {
        set(value);
    }

    @Override
    public Long getValue() {
        return get();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(get());
    }

    public static final Parcelable.Creator<JObservableLong> CREATOR
            = new Parcelable.Creator<JObservableLong>() {

        @Override
        public JObservableLong createFromParcel(Parcel source) {
            return new JObservableLong(source.readLong());
        }

        @Override
        public JObservableLong[] newArray(int size) {
            return new JObservableLong[size];
        }
    };
}