package com.duowan.fw.kvo.databinding;

/**
 * Created by yujian on 2016/4/4.
 *
 */
public interface JDatabindingObservable<T> {
	void setValue(T value);
	T getValue();
}
