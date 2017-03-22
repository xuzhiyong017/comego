package com.xuzhiyong.comego.module.datacenter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JConstCache;
import com.duowan.fw.util.JConstCache.CacheResult;
import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.analysis.StatsConst;

import java.util.concurrent.Callable;

/**
 * 注意, 数据库升级请知会：yujian@yy.com
 *
 * <p>将JDb分成JAppDb和JUserDb，默认了，两个数据库中，不能存在相同的表
 * */
public abstract class JDb extends SQLiteOpenHelper {

	public static synchronized void post(Runnable r){
		post(r, 0L);
	}

	public static synchronized void postSafe(Runnable r){
		ThreadBus.bus().callThreadSafe(ThreadBus.Db, r);
	}

	public static synchronized void post(Runnable r, long delayMillis){
		ThreadBus.bus().postDelayed(ThreadBus.Db, r, delayMillis);
	}

    //表的个数
    public static final int sCacheSize = 30;

    // 缓存对象的标志
    public static final int sCacheFlag_LoadDb = 1;
    public static final int sCacheFlag_LoadNet = 2;
    public static final int sCacheFlag_SelectDb = 4;

    /*****************************Instance Fields and Methods*****************************************/

    protected JConstCache[] mSlabCache = new JConstCache[sCacheSize];

    protected SQLiteDatabase mDb, mRDb, mWDb;

    public abstract void onCreate();
    public abstract void setupCache();
	
	public JDb(Context context, String name, int version){
		super(context, name, null, version);

        setupCache();
	}

	public void open(){
		mDb = this.getReadableDatabase();
		mRDb = this.getReadableDatabase();
		mWDb = this.getWritableDatabase();

        if(Build.VERSION.SDK_INT >= 16) {
            setWriteAheadLoggingEnabled(true);
        }
    }

    @Override
    public synchronized void close() {
        postSafe(new Runnable() {
            @Override
            public void run() {
                JDb.super.close();
            }
        });
    }
	
	@Override
    public void onCreate(SQLiteDatabase database) {
		mDb = database;
		
		onCreate();
	}

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	onCreate(db);
    }

    public CacheResult cache(int cacheId, Object key){
        JConstCache cache = mSlabCache[cacheId];
        return cache.cacheResultForKey(key, true);
    }

    public CacheResult cache(int cacheId, Object key, boolean autoCreate){
        JConstCache cache = mSlabCache[cacheId];
        return cache.cacheResultForKey(key, autoCreate);
    }
    
    private Cursor rawQuery(String sql, String[] selectionArgs) {
        if (null == mRDb) {
            JLog.error(this, "JDb mRDb is NULL! sql : " + sql
                    + " \n" + JLog.getCurrentStackTrace());

            DModule.ModuleDataCenter.module().sendEvent(DEvent.E_DB_Error_Report,
                    StatsConst.NULL_RDB);
            return null;
        }

        Cursor cursor = null;
        try {
            cursor = mRDb.rawQuery(sql, selectionArgs);
        } catch (Exception e) {
        	JLog.error(this, e.toString());

            DModule.ModuleDataCenter.module().sendEvent(DEvent.E_DB_Error_Report, e.toString());
        }
        return cursor;
    }

    public Cursor queryAll(String table){
    	return rawQuery("Select * From " + table, null);
    }
    
    public Cursor rawSelect(String sql, String[] selectionArgs){
    	return rawQuery(sql, selectionArgs);
    }
    
    public Cursor selectFrom(String tableName, String key, String value){
    	return rawQuery("SELECT * FROM " + tableName + " WHERE " + key + "=?", new String[]{value});
    }
    
    public Cursor selectFrom(String tableName, String[] keys, String[] values){
    	return rawQuery("SELECT * FROM " + tableName + DataCenterHelper.buildSql_WhereEqual(keys), values);
    }
    
    public void execSQL(String sql, Object[] bindArgs) {
        if (null == mWDb) {
            JLog.error(this, "JDb mWDb is NULL!");

            DModule.ModuleDataCenter.module().sendEvent(DEvent.E_DB_Error_Report, StatsConst.NULL_WDB);
            return;
        }
        try {
        	if(bindArgs == null) {
        		mWDb.execSQL(sql);
        	} else {
        		mWDb.execSQL(sql, bindArgs);
        	}
        } catch (Exception e) {
            JLog.error(this, "JDb execSQL Exception:" + e.toString());

            DModule.ModuleDataCenter.module().sendEvent(DEvent.E_DB_Error_Report, e.toString());
        }
    }

    public void insertOrReplace(String tableName, String[] keys, Object[] values){
    	execSQL(DataCenterHelper.buildSql_insertOrReplace(tableName, keys), values);
    }

    public void save(String keys, Object[] values) {
        execSQL(keys, values);
    }

    public void delete(String tableName, String key, Object value){
    	execSQL("DELETE FROM " + tableName + " WHERE " + key + "=?", new Object[]{value});
    }
    
    public void delete(String tableName, String[] keys, Object[] values){
    	StringBuilder sb = new StringBuilder();
    	sb.append("DELETE FROM ");
    	sb.append(tableName);
    	sb.append(DataCenterHelper.buildSql_WhereEqual(keys));
    	execSQL(sb.toString(), values);
    }
    
    public void deleteAll(String tableName){
        execSQL("DELETE FROM " + tableName, null);
    }
    
    /**
     * 这个函数一般用来创建表时使用，其他时候执行sql语句用execSQL
     */
    public void execSQLWithReadableDatabase(String sql) {
        if (null == mDb) {
            JLog.error(this, "JDb mDb is NULL!");
            return;
        }

        try {
        	mDb.execSQL(sql);
        } catch (Exception e) {
            JLog.error(this, "JDb execSQL Exception:" + e.toString());

            DModule.ModuleDataCenter.module().sendEvent(DEvent.E_DB_Error_Report, e.toString());
        }
    }
    
    /**
     * @param call
     * @return
     */
    public <T> T callTransaction(Callable<T> call){
        if (null == mWDb) {
            JLog.error(this, "JDb mWDb is NULL!");

            DModule.ModuleDataCenter.module().sendEvent(DEvent
                    .E_DB_Error_Report, StatsConst.NULL_WDB);
            return null;
        }

        T result = null;

        try {
            mWDb.beginTransactionNonExclusive();
        } catch (Exception e) {
            JLog.error(this, "callTransaction error : " + e);

            DModule.ModuleDataCenter.module().sendEvent(DEvent.E_DB_Error_Report, e.toString());
            return null;
        }
        try {
            result = call.call();
            mWDb.setTransactionSuccessful();
        } catch (Exception e) {
            JLog.error(this, "callTransaction error : " + e);

            DModule.ModuleDataCenter.module().sendEvent(DEvent.E_DB_Error_Report, e.toString());
        } finally {
            try {
                mWDb.endTransaction();
            } catch (Exception e) {
                JLog.error(this, "callTransaction error : " + e);

                DModule.ModuleDataCenter.module().sendEvent(DEvent.E_DB_Error_Report, e.toString());
            }
        }
        return result;
    }
}
