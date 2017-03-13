package com.duowan.fw.util;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.duowan.fw.util.JConstCache.CLinkedList.CEntry;


/**
 * v2.0 key value object cache, 
 * 1. support lru
 * 2. log the hits and gets
 * 3. every cache object will get a flag
 * */
public class JConstCache {
	
	public static class CacheResult{
		public Object value;
		public boolean isNew;
		public CacheObject cacheObj;
		
		public <T> T valueOf(Class<T> clazz){
			return clazz.cast(value);
		}
		
		public boolean isFlag(int flag){
			return JFlag.isFlag(cacheObj.flag, flag);
		}
		
		public void setFlag(int flag){
			cacheObj.flag = (int)JFlag.setFlag(cacheObj.flag, flag);
		}
		
		public void clearFlag(int flag){
			cacheObj.flag = (int)JFlag.clearFlag(cacheObj.flag, flag);
		}
	}
	
	public static class CacheObject{
		public Object key;
		public Object value;
		
		public int flag;
	}
	
	public static abstract class CacheControl{
		public abstract Object newObject(Object id);
		public void refreshValues(Object src, Object dst){ }
		public void cacheKWB(){ }
		public void onEvicted(Object key, Object value){ }
		public void onHit(Object key, Object value, CEntry<CacheObject> entry){}
    }
	
	public static class CLinkedList<T> {
		
		public static class CEntry<ET>{
			public ET data;
			public CEntry<ET> previous, next;
		}
		
		public int size;
		public CEntry<T> link;
		
		public CLinkedList(){
			size = 0;
			link = new CEntry<T>();
			link.data = null;
			link.previous = link;
			link.next = link;
		}
		
		public int size(){
			return size;
		}
		
		public static <T> CEntry<T> allocEntry(T value){
			CEntry<T> entry = new CEntry<T>();
			entry.data = value;
			
			return entry;
		}
		
		public CEntry<T> addFirst(T value){
			CEntry<T> entry = allocEntry(value);
			addFirst(entry);
			return entry;
		}
		
		public void addFirst(CEntry<T> entry){
			addAfter(link, entry);
			
			++size;
		}
		
		public CEntry<T> addLast(T value){
			
			CEntry<T> entry = new CEntry<T>();
			entry.data = value;
			
			addLast(entry);
			
			return null;
		}
		
		public void addLast(CEntry<T> entry){
			addBefore(link, entry);
			
			++size;
		}
		
		public void remove(CEntry<T> entry){
			if(entry == link){
				return;
			}
			
			entry.previous.next = entry.next;
			entry.next.previous = entry.previous;
			
			--size;
		}
		
		public CEntry<T> last(){
			if(size == 0){
				return null;
			}
			return link.previous;
		}
		
		public CEntry<T> first(){
			if(size == 0){
				return null;
			}
			return link.next;
		}
		
		public static <T> CEntry<T> addLruWithMax(CLinkedList<T> lru, CEntry<T> entry, int maxEntries){
			lru.addFirst(entry);
			if(lru.size() > maxEntries && maxEntries > 0){
				CEntry<T> last = lru.last();
				lru.remove(last);
				return last;
			}
			return null;
		}
		
		public static <T> void updateLru(CLinkedList<T> lru, CEntry<T> entry){
			lru.remove(entry);
			lru.addFirst(entry);
		}
		
		private static <T> void addBefore(CEntry<T> take, CEntry<T> before){
			before.next = take;
			before.previous = take.previous;
			
			take.previous.next = before;
			take.previous = before;
		}
		
		private static <T>  void addAfter(CEntry<T> take, CEntry<T> after){
			after.previous = take; 
			after.next = take.next;
			
			take.next.previous = after;
			take.next = after;
		}
	}
	
	private HashMap<Object, CEntry<CacheObject>> container = new HashMap<Object, CEntry<CacheObject>>();
	private CLinkedList<CacheObject> lru = new CLinkedList<JConstCache.CacheObject>();
	
	public String cacheName;
	public CacheControl cacheControl;
	public boolean autoNewObject = true;
	public final byte[] lock = new byte[0];
	public long flags = 0;
	public int maxEntries = 0;
	public int hits;
	public int gets;
	
	public CacheResult cacheResultForKey(Object id, boolean autoCreate){
		return cacheResultForKeyWithBackup(id, null, autoCreate);
	}
	
	public Object objectForKey(Object id){
		return objectForKey(id, this.autoNewObject);
	}
	
	public Object objectForKey(Object id, boolean autoCreate){
		return cacheResultForKey(id, autoCreate).value;
	}
	
	public boolean contains(Object id){
		synchronized (lock) {
			return container.containsKey(id);
		}
	}

	public void clear(){
		synchronized (lock) {
			container.clear();
		}
	}
	
	public CacheResult smartRefreshCache(Object id, Object value){
		CacheResult result = cacheResultForKeyWithBackup(id, value, true);
		if (result.value != value) {
			cacheControl.refreshValues(value, result.value);
		}
		return result;
	}
	
	private CacheResult cacheResultForKeyWithBackup(Object id, Object backup, boolean autoCreate){
		synchronized (lock) {
			cacheControl.cacheKWB();
			
			++gets;
			
			CacheResult result = new CacheResult();
			result.isNew = false;
			
			CEntry<CacheObject> entry = container.get(id);
			if(entry != null){
				++hits;
				
				cacheControl.onHit(id, entry.data.value, entry);
			}
			
			if (entry == null && autoCreate) {
				entry = CLinkedList.allocEntry(new CacheObject());
				entry.data.key = id;
				
				if (backup != null) {
					entry.data.value = backup;
				}else {
					entry.data.value = cacheControl.newObject(id);
				}
				container.put(id, entry);
				
				CEntry<CacheObject> last = CLinkedList.addLruWithMax(lru, entry, maxEntries);
				if(last != null){
                    container.remove(last.data.key);
					cacheControl.onEvicted(last.data.key, last.data.value);
				}
				
				result.isNew = true;
			}
			
			if(entry != null){
				result.cacheObj = entry.data;
				result.value = entry.data.value;
				
				if(!result.isNew){
					CLinkedList.updateLru(lru, entry);
				}
			}
			
			return result;	
		}
	}
	
	public static ConcurrentHashMap<String, JConstCache> allCaches = new ConcurrentHashMap<String, JConstCache>();
    public static JConstCache constCacheWithNameAndControl(String name, long flags, int maxEntries, CacheControl control){
		JConstCache cache = new JConstCache();
		cache.cacheName = name;
		cache.cacheControl = control;
		cache.flags = flags;
        cache.maxEntries = maxEntries;
		allCaches.put(name, cache);
		return cache;
	}

    public static JConstCache constCacheWithNameAndControl(String name, long flags, CacheControl control){
        return constCacheWithNameAndControl(name, flags, 0, control);
    }

    public static JConstCache constCacheWithNameAndControl(String name, int maxEntries, CacheControl control){
        return constCacheWithNameAndControl(name, 0, maxEntries, control);
    }

	public static JConstCache constCacheWithNameAndControl(String name, CacheControl control){
		return constCacheWithNameAndControl(name, 0, control);
	}
	
}
