package com.duowan.fw.kvo;

import com.duowan.fw.kvo.Kvo.KvoEvent;
import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.util.JLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Design by JerryZhou@outlook.com
 * v 1.0.0
 * use the kvo to extend the array filed, the name and effect just link the objective-c array kvo
 */
// @todo: add batch notify for array operator
public class KvoArray{
	public static final String NSKeyValueChangeKindKey = "changeKind";
	public static final String NSKeyValueChangeIndexesKey = "changeIndexesKey";
	public static final String NSKeyValueChangeNewKey = "changeNewKey";
	public static final String NSKeyValueChangeOldKey = "changeOldKey";
	public static final String NSKeyValueChangePatch = "changePatch";
	
	public static enum NSKeyValueSetMutationKind{
		NSKeyValueChangeSetting ,
		NSKeyValueChangeInsertion ,
		NSKeyValueChangeRemoval ,
		NSKeyValueChangeReplacement ,
		NSKeyValueChangeMoved
	}
	
	public static class NSRange{
		public int location;
		public int length;
		
		public static NSRange makeRange(int loc, int len){
			NSRange range = new NSRange();
			range.length = len;
			range.location = loc;
			return range;
		}
	}
	
	public static <T> void set(KvoSource source, String name, List<T> mList, Collection<? extends T> collection){
		mList.clear();
		mList.addAll(collection);
		NSRange range = NSRange.makeRange(0, collection.size());
		
		KvoEvent event = new KvoEvent();
		event.event = name;
		event.source = source;
		
		event.from = source;
		event.name = name;
		event.newValue = mList;
		event.oldValue = mList;
		event.args = new HashMap<String, Object>();
		event.args.put(NSKeyValueChangeKindKey, NSKeyValueSetMutationKind.NSKeyValueChangeSetting);
		event.args.put(NSKeyValueChangeIndexesKey, range);
		event.args.put(NSKeyValueChangeNewKey, mList);
		event.args.put(NSKeyValueChangeOldKey, mList);
		event.args.put(NSKeyValueChangePatch, collection);
		
		source.notifyKvoEvent(event);
	}
	
	public static <T> void insert(KvoSource source, String name, List<T> mList, int location, Collection<? extends T> collection){
		if(location > mList.size() || location < 0){
			JLog.error(null, "WRONG LOCATION " + location + " IN LIST " + source.getClass().getSimpleName() + "with "+ name);
			return;
		}
		NSRange range = NSRange.makeRange(location, collection.size());
		mList.addAll(location, collection);
		
		KvoEvent event = new KvoEvent();
		event.event = name;
		event.source = source;
		
		event.from = source;
		event.name = name;
		event.newValue = mList;
		event.oldValue = mList;
		event.args = new HashMap<String, Object>();
		event.args.put(NSKeyValueChangeKindKey, NSKeyValueSetMutationKind.NSKeyValueChangeInsertion);
		event.args.put(NSKeyValueChangeIndexesKey, range);
		event.args.put(NSKeyValueChangeNewKey, mList);
		event.args.put(NSKeyValueChangeOldKey, mList);
		event.args.put(NSKeyValueChangePatch, collection);
		
		source.notifyKvoEvent(event);
	}

	public static <T> void move(KvoSource source, String name, List<T> mList, int oldLocation, int newLocation){
		if(newLocation > mList.size() || newLocation < 0){
			JLog.error(null, "WRONG LOCATION " + newLocation + " IN LIST " + source.getClass().getSimpleName() + "with "+ name);
			return;
		}
		if(oldLocation > mList.size() || oldLocation < 0){
			JLog.error(null, "WRONG LOCATION " + oldLocation + " IN LIST " + source.getClass().getSimpleName() + "with "+ name);
			return;
		}

		NSRange range = NSRange.makeRange(oldLocation, newLocation - oldLocation);

		T t = mList.remove(oldLocation);

		if(oldLocation < newLocation) {
			mList.add(newLocation - 1, t);
		} else {
			mList.add(newLocation, t);
		}

		Collection<T> collection = new ArrayList<>();
		collection.add(t);

		KvoEvent event = new KvoEvent();
		event.event = name;
		event.source = source;

		event.from = source;
		event.name = name;
		event.newValue = mList;
		event.oldValue = mList;
		event.args = new HashMap<String, Object>();
		event.args.put(NSKeyValueChangeKindKey, NSKeyValueSetMutationKind.NSKeyValueChangeMoved);
		event.args.put(NSKeyValueChangeIndexesKey, range);
		event.args.put(NSKeyValueChangeNewKey, mList);
		event.args.put(NSKeyValueChangeOldKey, mList);
		event.args.put(NSKeyValueChangePatch, collection);

		source.notifyKvoEvent(event);
	}
	
	public static <T> void remove(KvoSource source, String name, List<T> mList, Collection<?> collection){
        NSRange range = null;
		ArrayList<Integer> indexs = new ArrayList<Integer>();
		for (Object e : collection) {
			int index = mList.indexOf(e);
			if (index >= 0) {
				indexs.add(index);
                if (collection.size() == 1) {
                    range = NSRange.makeRange(index, 1);
                }
            }
		}
		mList.removeAll(collection);
		
		KvoEvent event = new KvoEvent();
		event.event = name;
		event.source = source;
		
		event.from = source;
		event.name = name;
		event.newValue = mList;
		event.oldValue = mList;
		event.args = new HashMap<String, Object>();
		event.args.put(NSKeyValueChangeKindKey, NSKeyValueSetMutationKind.NSKeyValueChangeRemoval);
		event.args.put(NSKeyValueChangeIndexesKey, null != range ? range : indexs);
		event.args.put(NSKeyValueChangeNewKey, mList);
		event.args.put(NSKeyValueChangeOldKey, mList);
		event.args.put(NSKeyValueChangePatch, collection);
		
		source.notifyKvoEvent(event);
	}

    public static <T> void remove(KvoSource source, String name, List<T> mList, int location, int length){
        NSRange range = NSRange.makeRange(location, length);
        List<T> removed = mList.subList(location, location + length);
        mList.removeAll(removed);

        KvoEvent event = new KvoEvent();
        event.event = name;
        event.source = source;

        event.from = source;
        event.name = name;
        event.newValue = mList;
        event.oldValue = mList;
        event.args = new HashMap<String, Object>();
        event.args.put(NSKeyValueChangeKindKey, NSKeyValueSetMutationKind.NSKeyValueChangeRemoval);
        event.args.put(NSKeyValueChangeIndexesKey, range);
        event.args.put(NSKeyValueChangeNewKey, mList);
        event.args.put(NSKeyValueChangeOldKey, mList);
        event.args.put(NSKeyValueChangePatch, removed);

        source.notifyKvoEvent(event);
    }

	public static <E> void replace(KvoSource source, String name, List<E> sourceArray, ArrayList<Integer> indexs, Collection<E> collection){
		int i = 0;
		for (E e : collection) {
			int index = indexs.get(i);
			if(index >=0 && index < sourceArray.size()){
				sourceArray.set(index, e);
			}else{
				JLog.error(null, "WRONG INDEX " + index + " IN LIST " + source.getClass().getSimpleName() + " with " + name);
			}
            i++;
		}
		
		KvoEvent event = new KvoEvent();
		event.event = name;
		event.source = source;
		
		event.from = source;
		event.name = name;
		event.newValue = sourceArray;
		event.oldValue = sourceArray;
		event.args = new HashMap<String, Object>();
		event.args.put(NSKeyValueChangeKindKey, NSKeyValueSetMutationKind.NSKeyValueChangeReplacement);
		event.args.put(NSKeyValueChangeIndexesKey, indexs);
		event.args.put(NSKeyValueChangeNewKey, sourceArray);
		event.args.put(NSKeyValueChangeOldKey, sourceArray);
		event.args.put(NSKeyValueChangePatch, collection);
		
		source.notifyKvoEvent(event);
	}
	
	///----------------------------------------------------------------------------------
	///------------------quick array op interface: base on the batch operator------------
	///----------------------------------------------------------------------------------
	public static <E> void add(KvoSource source, String name, List<E> sourceArray, E e){
		ArrayList<E> collection = new ArrayList<E>(); collection.add(e);
		insert(source, name, sourceArray, sourceArray.size(), collection);
	}
	
	public static <E> void addLocation(KvoSource source, String name, List<E> sourceArray, E e, int location){
		ArrayList<E> collection = new ArrayList<E>(); collection.add(e);
		insert(source, name, sourceArray, location, collection);
	}
	
	public static <E> void remove(KvoSource source, String name, List<E> sourceArray, E e){
		ArrayList<E> collection = new ArrayList<E>(); collection.add(e);
		remove(source, name, sourceArray, collection);
	}
	
	public static <E> void replace(KvoSource source, String name, List<E> sourceArray, E e){
		int index = sourceArray.indexOf(e);
		if (index >= 0) {
			replace(source, name, sourceArray, index, e);
		}
	}
	
	public static <E> void replace(KvoSource source, String name, List<E> sourceArray, int index, E e){
		ArrayList<Integer> indexs = new ArrayList<Integer>(); indexs.add(index);
		ArrayList<E> collection = new ArrayList<E>(); collection.add(e);
		replace(source, name, sourceArray, indexs, collection);
	}
	
	///----------------------------------------------------------------------------------
	///------------------batch notify: just make the array dirty and reload it-----------
	///----------------------------------------------------------------------------------
	public static <E> void notifyArraySettingChange(KvoSource source, String name, List<E> sourceArray){
		NSRange range = NSRange.makeRange(0, sourceArray.size());
		
		KvoEvent event = new KvoEvent();
		event.event = name;
		event.source = source;
		
		event.from = source;
		event.name = name;
		event.newValue = sourceArray;
		event.oldValue = sourceArray;
		event.args = new HashMap<String, Object>();
		event.args.put(NSKeyValueChangeKindKey, NSKeyValueSetMutationKind.NSKeyValueChangeSetting);
		event.args.put(NSKeyValueChangeIndexesKey, range);
		event.args.put(NSKeyValueChangeNewKey, sourceArray);
		event.args.put(NSKeyValueChangeOldKey, sourceArray);
		event.args.put(NSKeyValueChangePatch, sourceArray);
		
		source.notifyKvoEvent(event);
	}

    public static NSKeyValueSetMutationKind getKvoMutationType(KvoEvent event) {
        try {
            NSKeyValueSetMutationKind type = event.argT(KvoArray.NSKeyValueChangeKindKey,
                    NSKeyValueSetMutationKind.class);
            if (null == type) {
                type = NSKeyValueSetMutationKind.NSKeyValueChangeSetting;
            }
            return type;
        } catch (ClassCastException e) {
            JLog.error("KvoArray", "getKvoMutationType exception:" + e.getMessage());
            return NSKeyValueSetMutationKind.NSKeyValueChangeSetting;
        }
    }

    public static NSRange getNSRange(KvoEvent event) {
        try {
            return event.argT(KvoArray.NSKeyValueChangeIndexesKey, NSRange.class);
        } catch (ClassCastException e) {
            JLog.error("KvoArray", "getNSRange exception:" + e.getMessage());
            return null;
        }
    }
}