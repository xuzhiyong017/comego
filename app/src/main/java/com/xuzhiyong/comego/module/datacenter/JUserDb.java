package com.xuzhiyong.comego.module.datacenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.module.datacenter.tables.JUserData;

public class JUserDb extends JDb {

    //版本号递增不能用日期
    public final static int JUserDbVersion1 = 200001;    // 初始版本
    public final static int JUserDbVersion = JUserDbVersion1;

    //缓存的索引，如果表是不需要内存cache的，就不需要创建cacheId


    public JUserDb(Context context, String name) {
        super(context, name, JUserDbVersion);
    }

    @Override
    public void onCreate() {
        JUserData.create(this);
    }

    @Override
    public void setupCache() {

    }

    /**
     * Attention : 在upgrade里某一个版本创建新表时，要写当时那个版本的create sql
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 16-10-25
//        mDb = db;

//        try {
//            for (int i = oldVersion; i < newVersion; i++) {
//                switch (i) {
//                    case JUserDbVersion1:
//                        break;
//                }
//            }
//        } catch (Exception e) {
//            //recreate
//            onCreate(db);
//
//            JLog.error(this, "ERROR ON CREATE: " + e.toString());
//        }
    }
}
