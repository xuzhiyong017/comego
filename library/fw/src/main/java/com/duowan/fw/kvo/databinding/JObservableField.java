package com.duowan.fw.kvo.databinding;

import android.databinding.ObservableField;

/**
 * Created by hasee on 2016/4/10.
 *
 */
public class JObservableField<T> extends ObservableField<T> implements JDatabindingObservable<T> {

    public JObservableField(T value) {
        super(value);
    }

    public JObservableField() {
    }

    @Override
    public void setValue(T value) {
        set(value);
    }

    @Override
    public T getValue() {
        return get();
    }
}
