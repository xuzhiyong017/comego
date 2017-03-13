package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableChar;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hasee on 2016/4/3.
 *
 */
public class JObservableChar extends ObservableChar implements JDatabindingObservable<Character> {

    public JObservableChar(char value) {
        super(value);
    }

    public JObservableChar() {
        super((char) 0);
    }

    @Override
    public void setValue(Character value) {
        set(value);
    }

    @Override
    public Character getValue() {
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

    public static final Parcelable.Creator<JObservableChar> CREATOR
            = new Parcelable.Creator<JObservableChar>() {

        @Override
        public JObservableChar createFromParcel(Parcel source) {
            return new JObservableChar((char) source.readInt());
        }

        @Override
        public JObservableChar[] newArray(int size) {
            return new JObservableChar[size];
        }
    };
}
