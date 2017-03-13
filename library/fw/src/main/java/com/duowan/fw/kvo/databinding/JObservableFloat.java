package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableFloat;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hydra on 2016/4/3.
 *
 */
public class JObservableFloat extends ObservableFloat implements JDatabindingObservable<Float> {

    public JObservableFloat(float value) {
        super(value);
    }

    public JObservableFloat() {
        super(0.f);
    }

    @Override
    public void setValue(Float value) {
        set(value);
    }

    @Override
    public Float getValue() {
        return get();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(get());
    }

    public static final Parcelable.Creator<JObservableFloat> CREATOR
            = new Parcelable.Creator<JObservableFloat>() {

        @Override
        public JObservableFloat createFromParcel(Parcel source) {
            return new JObservableFloat(source.readFloat());
        }

        @Override
        public JObservableFloat[] newArray(int size) {
            return new JObservableFloat[size];
        }
    };
}
