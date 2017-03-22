package com.xuzhiyong.comego.module.datacenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xuzhiyong.comego.module.datacenter.tables.JAppData;
import com.xuzhiyong.comego.module.datacenter.tables.JLoginHistoryItem;
import com.xuzhiyong.comego.module.datacenter.tables.JUserInfo;
import com.xuzhiyong.comego.module.datacenter.tables.ProtoStatistics;


public class JAppDb extends JDb {

	//版本号递增不能用日期
	public final static int JAppDbVersion1 = 100001;    // 初始版本

	public final static int JAppDbVersion = JAppDbVersion1;

	// 缓存的索引，如果有表是不需要内存cache的，就不需要创建cacheId
	public static final int sCacheId_JUserInfo = 0;
	public static final int sCacheId_JLoginHistoryItem = 1;

	public JAppDb(Context context, String name) {
		super(context, name, JAppDbVersion);
	}

	@Override
	public void onCreate() {
		JLoginHistoryItem.create(this);
		ProtoStatistics.create(this);
		JUserInfo.create(this);
		JAppData.create(this);
	}

	@Override
	public void setupCache() {
		mSlabCache[sCacheId_JUserInfo] = JUserInfo.buildCache();
		mSlabCache[sCacheId_JLoginHistoryItem] = JLoginHistoryItem.buildCache();
	}

	/**
	 * Attention : 在upgrade里某一个版本创建新表时，要写当时那个版本的create sql
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 16-10-25
//		mDb = db;
//
//		try {
//			for (int i = oldVersion; i < newVersion; i++) {
//				switch (i) {
//					case JAppDbVersion1:
//                        mDb.execSQL("ALTER TABLE UserInfo ADD COLUMN level INT");
//                        mDb.execSQL("ALTER TABLE UserInfo ADD COLUMN exp INT");
//                        mDb.execSQL("ALTER TABLE UserInfo ADD COLUMN nextExp INT");
//						mDb.execSQL("ALTER TABLE ContactInfo ADD COLUMN subscribe INT");
//						mDb.execSQL("ALTER TABLE GroupInfo ADD COLUMN gameIconUrl TEXT");
//						break;
//                    case JAppDbVersion2:
//                        mDb.execSQL("ALTER TABLE GiftInfo ADD COLUMN type INT");
//                        break;
//					case JAppDbVersion3:
//						mDb.execSQL("ALTER TABLE UserInfo ADD COLUMN groupUrl TEXT");
//						mDb.execSQL("ALTER TABLE UserInfo ADD COLUMN extendJson TEXT");
//						break;
//                    case JAppDbVersion4:
//                        mDb.execSQL("ALTER TABLE GiftInfo ADD COLUMN sendType INT");
//                        break;
//				}
//			}
//		} catch (Exception e) {
//			onCreate(db);
//
//			JLog.error(this, "ERROR ON CREATE: " + e.toString());
//		}
	}
}
