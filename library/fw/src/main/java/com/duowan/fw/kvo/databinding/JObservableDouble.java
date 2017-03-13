package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableDouble;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hydra on 2016/4/3.
 *
 */
public class JObservableDouble extends ObservableDouble implements JDatabindingObservable<Double> {

    public JObservableDouble(double value) {
        super(value);
    }

    public JObservableDouble() {
        super(0.f);
    }

    @Override
    public void setValue(Double value) {
        set(value);
    }

    @Override
    public Double getValue() {
        return get();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(get());
    }

    public static final Parcelable.Creator<JObservableDouble> CREATOR
            = new Parcelable.Creator<JObservableDouble>() {

        @Override
        public JObservableDouble createFromParcel(Parcel source) {
            return new JObservableDouble(source.readDouble());
        }

        @Override
        public JObservableDouble[] newArray(int size) {
            return new JObservableDouble[size];
        }
    };
}
