package com.xuzhiyong.comego.Model;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadBus {
	
	// keep id [0, 100w] 
	
	public static final int Whatever = 0;	// more ??
	public static final int Main = 1; 		// more ui
	public static final int Working = 2;	// more cpu
	public static final int IO = 3;			// more io 
	public static final int Net = 4;		// more net
	public static final int Db = 5;			// more db
	public static final int Shit = 6;		// more ......
	public static final int Pool = 7;		// more fresh thread
	public static final int PoolTiming = 8; // caculate the timing for pool

	private String busname;
	private ConcurrentHashMap<Integer, ThreadBusAsyncAdapter> bus = new ConcurrentHashMap<Integer, ThreadBusAsyncAdapter>();
	private ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 5, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	public ThreadBus(String name){
		busname = name;
		bus.put(Pool, new ThreadPoolHandler(pool));
		bus.put(Main, new ThreadBusHandler(new Handler(Looper.getMainLooper())));
		addThread(Working, "Working");
		addThread(IO, 		"IO");
		addThread(Net, 		"Net");
		addThread(Db, 		"Db");
		addThread(Shit, 	"Shit");
		addThread(PoolTiming,"PoolTiming");
	}
	
	public void addThread(int idx, String name){
		HandlerThread thread = new HandlerThread("Bus("+busname+"):"+name);
		thread.start();
		bus.put(idx, new ThreadBusHandler(thread));
	}
	
	public void addHandler(int idx, Handler handler){
		bus.put(idx, new ThreadBusHandler(handler));
	}
	
	public void removeHandler(int idx){
		bus.remove(idx);
	}
	
	public ThreadBusAsyncAdapter handler(int idx){
		return bus.get(idx);
	}
	
	public final boolean post(int idx, Runnable r){
		 return handler(idx).post(r);
	}
	
    public final boolean postDelayed(int idx, Runnable r, long delayMillis){
    	return handler(idx).postDelayed(r, delayMillis);
    }
    
    public final boolean postAtTime(int idx, Runnable r, long uptimeMillis){
    	return handler(idx).postAtTime(r, uptimeMillis);
    }
    
    public final void removeCallbacks(int idx, Runnable r, Object token){
    	handler(idx).removeCallbacks(r, token);
    }
    
    public final boolean callThreadSafe(int idx, Runnable r){
    	ThreadBusAsyncAdapter adapter = bus.get(idx);
    	if(adapter != null ) {
    		if (adapter instanceof ThreadBusHandler){
    			if(Looper.myLooper() == ((ThreadBusHandler)adapter).looper()){
    				r.run();
    			}else{
    				adapter.post(r);
    			}
    		} else {
	    		adapter.post(r);
    		}
	    	return true;
    	}
    	return false;
    }

    public Handler getHandler(int idx) {
        return handler(idx).getHandler();
    }

    static Object dbusLock = new Object();
    static ThreadBus dbus = null;
    public static final ThreadBus bus(){
    	synchronized(dbusLock){
    	if(dbus == null){
    		dbus = new ThreadBus("JB");
    	}}
    	return dbus;
    }
    
    //*************************************************************************************
    //*************************************************************************************
    //*************************************************************************************
    public static int BusThreadBegin = 10000000;
    public static int gen(){
    	return ++BusThreadBegin;
    }

    //*************************************************************************************
    //*************************************************************************************
    //*************************************************************************************
	public interface ThreadBusAsyncAdapter{
		boolean post(Runnable r);
	    boolean postDelayed(Runnable r, long delayMillis);
	    boolean postAtTime(Runnable r, long uptimeMillis);
	    void removeCallbacks(Runnable r, Object token);
        Handler getHandler();
	}
	
	public static class ThreadPoolHandler implements ThreadBusAsyncAdapter {
		
		private ThreadPoolExecutor pool;
		
		public ThreadPoolHandler(ThreadPoolExecutor p){
			pool = p;
		}
		
		@Override
		public boolean post(Runnable r){
			pool.execute(r);
			return true;
		}
		
		@Override
	    public boolean postDelayed(final Runnable r, long delayMillis){
			bus().postDelayed(PoolTiming, new Runnable() {
				@Override
				public void run() {
					post(r);
				}
			}, delayMillis);
	    	return true;
	    }
	    
		@Override
	    public boolean postAtTime(final Runnable r, long uptimeMillis){
			bus().postAtTime(PoolTiming, new Runnable() {
				@Override
				public void run() {
					post(r);
				}
			}, uptimeMillis);
	    	return true;
	    }

		@Override
		public void removeCallbacks(Runnable r, Object token) {
			pool.remove(r);
		}

        @Override
        public Handler getHandler() {
            return null;
        }
    }
	
	public static class ThreadBusHandler implements ThreadBusAsyncAdapter {
		@SuppressWarnings("unused")
		private HandlerThread thread;
		private Handler handler;
		
		public ThreadBusHandler(Handler h){
			handler = h;
		}
		
		public ThreadBusHandler(HandlerThread t){
			thread = t;
			handler = new Handler(t.getLooper());
		}
		
		@Override
		public boolean post(Runnable r) {
			return handler.post(r);
		}
		
		@Override
		public boolean postDelayed(Runnable r, long delayMillis) {
			return handler.postDelayed(r, delayMillis);
		}
		
		@Override
		public boolean postAtTime(Runnable r, long uptimeMillis) {
			return handler.postAtTime(r, uptimeMillis);
		}
		
		public Looper looper(){
			return handler.getLooper();
		}

		@Override
		public void removeCallbacks(Runnable r, Object token) {
			handler.removeCallbacks(r, token);
		}

        @Override
        public Handler getHandler() {
            return handler;
        }
    }
}
