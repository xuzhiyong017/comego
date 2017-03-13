package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableBoolean;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hydra on 2016/4/3.
 *
 */
public class JObservableBoolean extends ObservableBoolean implements JDatabindingObservable<Boolean> {

    public JObservableBoolean(boolean value) {
        super(value);
    }

    public JObservableBoolean() {
        super(false);
    }

    @Override
    public void setValue(Boolean value) {
        set(value);
    }

    @Override
    public Boolean getValue() {
        return get();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(get() ? 1 : 0);
    }

    public static final Parcelable.Creator<JObservableBoolean> CREATOR
            = new Parcelable.Creator<JObservableBoolean>() {

        @Override
        public JObservableBoolean createFromParcel(Parcel source) {
            return new JObservableBoolean(source.readInt() == 1);
        }

        @Override
        public JObservableBoolean[] newArray(int size) {
            return new JObservableBoolean[size];
        }
    };
}
