package com.duowan.fw.kvo;

import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.util.JThreadUtil;
import com.duowan.fw.util.JUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class KvoMainThreadList<T> implements List<T> {
	private KvoSource mSource;
	private String mName;
	private List<T> mList;

	public KvoMainThreadList(KvoSource source, String name){
		mSource = source;
		mName = name;
		mList = buildList();
	}

	public KvoMainThreadList(KvoSource source, String name, List<T> list){
		mSource = source;
		mName = name;
		mList = list;
	}

	public void notifyChange(){
		mSource.notifyKvoEvent(mName);
	}

	protected List<T> buildList() {
		return new ArrayList<>();
	}

	public List<T> list() {
		return mList;
	}

	public void set(final Collection<? extends T> collection) {
		if (JThreadUtil.isInMainThread()) {
			KvoArray.set(mSource, mName, mList, collection);
		}else {
			JThreadUtil.postMainThread(new Runnable() {
				@Override
				public void run() {
					KvoArray.set(mSource, mName, mList, collection);
				}
			});
		}
	}

	public void move(final int oldLocation, final int newLocation) {
		if (JThreadUtil.isInMainThread()) {
			KvoArray.move(mSource, mName, mList, oldLocation, newLocation);
		}else {
			JThreadUtil.postMainThread(new Runnable() {
				@Override
				public void run() {
					KvoArray.move(mSource, mName, mList, oldLocation, newLocation);
				}
			});
		}
	}

	@Override
	public boolean add(final T object) {
		if(JThreadUtil.isInMainThread()){
			KvoArray.add(mSource, mName, mList, object);
		}else {
			JThreadUtil.postMainThread(new Runnable() {
				@Override
				public void run() {
					KvoArray.add(mSource, mName, mList, object);
				}
			});
		}
		return true;
	}

	@Override
	public void add(final int location, final T object) {
		if(JThreadUtil.isInMainThread()){
			ArrayList<T> collection = new ArrayList<T>(); collection.add(object);
			KvoArray.insert(mSource, mName, mList, location, collection);
		}else {
			JThreadUtil.postMainThread(new Runnable() {
				@Override
				public void run() {
					ArrayList<T> collection = new ArrayList<T>(); collection.add(object);
					KvoArray.insert(mSource, mName, mList, location, collection);
				}
			});
		}
	}

	@Override
	public boolean addAll(final int location, final Collection<? extends T> collection) {
		if(JThreadUtil.isInMainThread()){
			KvoArray.insert(mSource, mName, mList, location, collection);
		}else {
			JThreadUtil.postMainThread(new Runnable() {
				@Override
				public void run() {
					KvoArray.insert(mSource, mName, mList, location, collection);
				}
			});
		}
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends T> collection) {
		if(JThreadUtil.isInMainThread()){
			KvoArray.insert(mSource, mName, mList, mList.size(), collection);
		}else {
			JThreadUtil.postMainThread(new Runnable() {
				@Override
				public void run() {
					KvoArray.insert(mSource, mName, mList, mList.size(), collection);
				}
			});
		}
		return true;
	}

	@Override
	public void clear() {
		ArrayList<T> collection = new ArrayList<T>();
		set(collection);
	}

	@Override
	public boolean contains(Object object) {
		return mList.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return mList.containsAll(collection);
	}

	@Override
	public T get(int location) {
		return mList.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return mList.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return mList.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return mList.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return mList.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return mList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int location) {
		return mList.listIterator(location);
	}

	@Override
	public T remove(int location) {
		T object = get(location);
		if (object != null) {
			remove(object);
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(final Object object) {
		if(JThreadUtil.isInMainThread()){
			KvoArray.remove(mSource, mName, mList, (T)object);
		}else {
			JThreadUtil.postMainThread(new Runnable() {
				@Override
				public void run() {
					KvoArray.remove(mSource, mName, mList, (T)object);
				}
			});
		}
		return true;
	}

	@Override
	public boolean removeAll(final Collection<?> collection) {
		if(JThreadUtil.isInMainThread()){
			KvoArray.remove(mSource, mName, mList, collection);
		}else {
			JThreadUtil.postMainThread(new Runnable() {
				@Override
				public void run() {
					KvoArray.remove(mSource, mName, mList, collection);
				}
			});
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		JUtils.jAssert(false);
		return false;
	}

	@Override
	public T set(int location, T object) {
		KvoArray.replace(mSource, mName, mList, object);
		return null;
	}

	@Override
	public int size() {
		return mList.size();
	}

	@Override
	public List<T> subList(int start, int end) {
		return mList.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return mList.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] array) {
		return mList.toArray(array);
	}

	public void callKvo(Runnable runnable) {
		if(JThreadUtil.isInMainThread()){
			runnable.run();
		}else {
			JThreadUtil.postMainThread(runnable);
		}
	}
}
