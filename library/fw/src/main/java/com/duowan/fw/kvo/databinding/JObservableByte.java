package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableByte;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hydra on 2016/4/3.
 *
 */
public class JObservableByte extends ObservableByte implements JDatabindingObservable<Byte> {

    public JObservableByte(byte value) {
        super(value);
    }

    public JObservableByte() {
        super((byte)0);
    }

    @Override
    public void setValue(Byte value) {
        set(value);
    }

    @Override
    public Byte getValue() {
        return get();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(get());
    }

    public static final Parcelable.Creator<JObservableByte> CREATOR
            = new Parcelable.Creator<JObservableByte>() {

        @Override
        public JObservableByte createFromParcel(Parcel source) {
            return new JObservableByte(source.readByte());
        }

        @Override
        public JObservableByte[] newArray(int size) {
            return new JObservableByte[size];
        }
    };
}
