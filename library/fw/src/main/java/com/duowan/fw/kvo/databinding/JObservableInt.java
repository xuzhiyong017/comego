package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableInt;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hydra on 2016/4/3.
 *
 */
public class JObservableInt extends ObservableInt implements JDatabindingObservable<Integer> {

    public JObservableInt(int value) {
        super(value);
    }

    public JObservableInt() {
        super(0);
    }

    @Override
    public void setValue(Integer value) {
        set(value);
    }

    @Override
    public Integer getValue() {
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

    public static final Parcelable.Creator<JObservableInt> CREATOR
            = new Parcelable.Creator<JObservableInt>() {

        @Override
        public JObservableInt createFromParcel(Parcel source) {
            return new JObservableInt(source.readInt());
        }

        @Override
        public JObservableInt[] newArray(int size) {
            return new JObservableInt[size];
        }
    };
}
