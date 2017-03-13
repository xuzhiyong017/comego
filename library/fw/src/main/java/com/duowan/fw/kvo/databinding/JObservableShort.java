package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableShort;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hydra on 2016/4/3.
 *
 */
public class JObservableShort extends ObservableShort implements JDatabindingObservable<Short> {

    public JObservableShort(short value) {
        super(value);
    }

    public JObservableShort() {
        super((short) 0);
    }

    @Override
    public void setValue(Short value) {
        set(value);
    }

    @Override
    public Short getValue() {
        return get();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(get());
    }

    public static final Parcelable.Creator<JObservableShort> CREATOR
            = new Parcelable.Creator<JObservableShort>() {

        @Override
        public JObservableShort createFromParcel(Parcel source) {
            return new JObservableShort((short) source.readInt());
        }

        @Override
        public JObservableShort[] newArray(int size) {
            return new JObservableShort[size];
        }
    };
}
