package com.xuzhiyong.comego.module;

import android.support.v4.util.LongSparseArray;

import com.duowan.fw.ThreadBus;

public class DKeepMeRunnable {
	public static enum KeepMode {
		KeepMode_RightNow,					// 有新数据立刻执行，然后开始缓存
		KeepMode_CacheIt,					// 有新数据，开始缓存，等时间到一并执行
	}
	public class KeepState {
		public volatile boolean runnging; 	// 是否在异步检查
		public volatile boolean live; 		// 挂了，就不再进行下一次
		public volatile long loop; 			// 循环次数

		public Runnable run;				// 执行体
		public long interval;				// 运行的间隔
		public volatile long tick;					// 最近一次运行的时间
		public long id;						// Keep 结构体的唯一ID

		public Runnable longrun;			// 检查
		public int thread;					// 运行的线程
		public volatile long changetick;				// 执行体有数据变更的时间戳
		
		public KeepMode mode; 				// mode == 0, mode == 1

        public final byte[] mRunningLock = new byte[0];
	}
	final byte[] mKeepLocker = new byte[0];

	LongSparseArray<KeepState> mAllKeeps = new LongSparseArray<KeepState>();
	
	public KeepState register(Runnable runnable, long interval, int thread, KeepMode mode) {
		final KeepState keep = new KeepState();
		keep.run = runnable;
		keep.interval = interval;
		keep.live = true;
		keep.thread = thread;
		keep.mode = mode;
		keep.loop = 0;
		keep.runnging = false;
		keep.longrun = new Runnable() {

			@Override
			public void run() {
				if (keep.changetick > keep.tick || keep.loop > 0) {
					runitThenPost(keep);

					if (keep.loop > 0) {
						--keep.loop;
					}
				} else {
					keep.runnging = false;
				}
			}
			
		};
		
		synchronized(mKeepLocker){
			keep.id = mAllKeeps.size();
			mAllKeeps.put(keep.id, keep);
		}

		return keep;
	}

	public void unregister(KeepState keep) {
		synchronized(mKeepLocker){
			if (keep != null) {
				keep.live = false;
				mAllKeeps.remove(keep.id);
			}
		}
	}

	public KeepState keepOf(long id) {
		synchronized(mKeepLocker){
			return mAllKeeps.get(id);
		}
	}

    public void cancelRun(KeepState keep) {
        if (keep == null) {
            return;
        }
        synchronized (keep.mRunningLock) {
            if (keep.runnging) {
                ThreadBus.bus().removeCallbacks(keep.thread, keep.longrun, null);
                keep.runnging = false;
            }
        }
    }

	public void needrun(long id) {
		needrun(keepOf(id));
	}

	public void needrun(KeepState keep) {
		if (keep == null) {
			return;
		}
		
        synchronized (keep.mRunningLock) {
            keep.changetick = System.currentTimeMillis();
            if (keep.runnging) {
                return;
            }

            runMode(keep);
        }
	}

	void runMode(final KeepState keep) {
		if (keep.mode == KeepMode.KeepMode_RightNow) {
			// thread safe call
			ThreadBus.bus().callThreadSafe(keep.thread, new Runnable(){

				@Override
				public void run() {
					runit(keep);
				}
			});
		}

		// in async
		keep.runnging = true;

        ThreadBus.bus().removeCallbacks(keep.thread, keep.longrun, null);
		ThreadBus.bus().postDelayed(keep.thread, keep.longrun, keep.interval);
	}

	void runit(KeepState keep) {
		// log run tick
		keep.tick = System.currentTimeMillis();
		// run
		if (keep.live) {
			keep.run.run();
		}
	}

	void runitThenPost(KeepState keep) {
		// run
		runit(keep);
		// post
		if (keep.live) {
            ThreadBus.bus().removeCallbacks(keep.thread, keep.longrun, null);
			ThreadBus.bus().postDelayed(keep.thread, keep.longrun, keep.interval);
		}
	}

    static final DKeepMeRunnable _kmr = new DKeepMeRunnable();
	public static DKeepMeRunnable kmr() {
		return _kmr;
	}
}
