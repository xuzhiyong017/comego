package com.duowan.fw.kvo;

import android.databinding.ObservableArrayList;

import com.duowan.fw.kvo.Kvo.KvoSource;

import java.util.List;

/**
 * Created by yujian on 2016/4/5.
 *
 */
public class JObservableKvoMainThreadList<T> extends KvoMainThreadList<T> {

	public JObservableKvoMainThreadList(KvoSource source, String name) {
		super(source, name);
	}

	public JObservableKvoMainThreadList(KvoSource source, String name, List<T> list) {
		super(source, name, list);
	}

	@Override
	protected List<T> buildList() {
		return new ObservableArrayList<>();
	}

	@Override
	public ObservableArrayList<T> list() {
		return (ObservableArrayList<T>) super.list();
	}
}
