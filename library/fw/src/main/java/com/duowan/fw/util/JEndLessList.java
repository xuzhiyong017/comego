package com.duowan.fw.util;

import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.kvo.KvoMainThreadList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: JerryZhou@outlook.com
 * */
public class JEndLessList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * EndLess TotalNum
	 * */
	private int mTotalNum;
	
	/**
	 * EndLess Default TotalNum: Means the list is truely endless
	 * */
	public final static int EndLessTotalNum = -1;
	
	/**
	 * combine kvo feature
	 * */
	public KvoMainThreadList<T> mKvoList;
	
	/**
	 * Default Constructor
	 * */
	public JEndLessList() {
		mTotalNum = EndLessTotalNum;
	}
	
	/**
	 * Default Constructor
	 * */
	public JEndLessList(KvoSource source, String name) {
		mTotalNum = EndLessTotalNum;
		mKvoList = new KvoMainThreadList<T>(source, name, this);
	}
	
	/**
	 * */
	public int curIndex(){
		return size();
	}
	
	/**
	 * */
	public void combineResult(int startIndex, List<T> results){
		if(startIndex == 0){
			combineResultWithTotal(startIndex, results, EndLessTotalNum);
		}else{
			combineResultWithTotal(startIndex, results, null);
		}
	}

    public void combineResultWithFetch(int startIndex, List<T> results, Integer fetchs, Boolean hasMore) {
        int total = EndLessTotalNum;
        if ((fetchs != null && results.size() < fetchs)
                || (null != hasMore && !hasMore)) {
            total = results.size() + this.size();
        }

        combineResultWithTotal(startIndex, results, total);
    }

	/**
	 * */
	public void combineResultWithFetch(int startIndex, List<T> results, Integer fetchs) {
		int total = EndLessTotalNum;
		if (fetchs != null && results.size() < fetchs) {
			total = results.size() + this.size();
		}

		combineResultWithTotal(startIndex, results, total);
	}
	
	/**
	 * */
	public void combineResultWithTotal(int startIndex, List<T> results, Integer total){
		
		if(total != null){
			mTotalNum = total;
		}
		
		// keep it no accept null
		if (results == null) {
			return;
		}
		
		JUtils.jAssert(startIndex >= 0);
		
		if (startIndex == 0) {
			this.jset(results);
		}else {
			if (size() == startIndex) {
				this.jaddAll(results);
			}else {
				this.jkvoappend(startIndex, results);
			}
		}
	}
	
	/**
	 * */
	public boolean hasMore(){
		if (mTotalNum == EndLessTotalNum || mTotalNum > size()) {
			return true;
		}
		return false;
	}
	
	/**
	 * */
	public void flushCapacity(){
		mTotalNum = size();
	}
	
	/**
	 * */
	public void flushCapacityWithTotal(final int total){
		if(mKvoList != null){
			mKvoList.callKvo(new Runnable() {
				
				@Override
				public void run() {
					if(total <= size()){
						mTotalNum = size();
						
						mKvoList.notifyChange();
					}		
				}
			});
		}else{
			if(total <= size()){
				mTotalNum = size();

				mKvoList.notifyChange();
			}
		}
	}
	
	/**
	 * */
	public void flushEndLess(){
		mTotalNum = EndLessTotalNum;
	}
	
	/**
	 * */
	public int totalNum(){
		return mTotalNum;
	}
	
	/***/
	public void jclear(){
		if(mKvoList != null){
			mKvoList.clear();
		}else{
			super.clear();
		}
	}
	
	public boolean jaddAll(Collection<? extends T> collection) {
		if(mKvoList != null){
			return mKvoList.addAll(collection);
		}else{
			return super.addAll(collection);
		}
	}
	
	public T jset(int index, T object) {
		if(mKvoList != null){
			return mKvoList.set(index, object);
		}else{
			return super.set(index, object);
		}
	}
	
	public boolean jadd(T object) {
		if(mKvoList != null){
			return mKvoList.add(object);
		}else{
			return super.add(object);
		}
	}

    public void jadd(int location, T object) {
        if(mKvoList != null) {
            mKvoList.add(location, object);
        } else {
            super.add(location, object);
        }
    }

    public boolean jremove(T object) {
        if(mKvoList != null){
            return mKvoList.remove(object);
        }else{
            return super.add(object);
        }
    }

	public boolean jset(Collection<? extends T> collection){
		if(mKvoList != null){
			mKvoList.set(collection);
		}else{
			super.clear();
			super.addAll(collection);
		}
		return true;
	}
	
	public void jkvoappend(final int startIndex, final Collection<? extends T> collection){
		if(mKvoList != null){
			mKvoList.callKvo(new Runnable() {
				@Override
				public void run() {
					jappend(startIndex, collection);	
				}
			});
		}else{
			jappend(startIndex, collection);
		}
	}
	
	public void jappend(int startIndex, Collection<? extends T> collection){
        int size = size();
		for (T t : collection) {
			if (size > startIndex) {
				this.jset(startIndex, t);
                startIndex++;
			}else {
				this.jadd(t);
			}
		}
	}
}
