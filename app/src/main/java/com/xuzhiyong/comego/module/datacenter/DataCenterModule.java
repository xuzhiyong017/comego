package com.xuzhiyong.comego.module.datacenter;

import com.duowan.fw.FwEvent;
import com.duowan.fw.FwEventAnnotation;
import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.Login.LoginHelper;

public class DataCenterModule extends Module implements DataCenterInterface {

    private DataCenterModuleData mData;

    private final Object mUserDbLock = new byte[0];

    private long mCurrentUid;

    private JDb mUserDb, mAppDb;

    public DataCenterModule() {
	    mData = new DataCenterModuleData();
        DData.dataCenterModuleData.link(this, mData);

        mCurrentUid = 0L;

	    initAppDb();

        DEvent.autoBindingEvent(this);
    }

	private void initAppDb() {
		mAppDb = new JAppDb(gMainContext, DataCenterHelper.buildAppDbName());
		mAppDb.open();
	}

    @Override
    public JDb userDb() {
        return mUserDb;
    }

    @Override
    public JDb appDb() {
        return mAppDb;
    }

    @FwEventAnnotation(event=DEvent.E_UserChange_Before)
    public void onUserChangeBefore(FwEvent.EventArg event){
        long uid = event.arg1(Long.class);

        mCurrentUid = uid;

        final JDb oldDb = mUserDb;

        sendEvent(DEvent.E_DataCenter_UserDBChanged_Before, oldDb);

        synchronized (mUserDbLock) {
            if(mUserDb != null) {
                mUserDb.close();
            }

            mUserDb = new JUserDb(gMainContext, DataCenterHelper.buildUserDbName(mCurrentUid));
            mUserDb.open();

            final JDb newDb = mUserDb;

            JDb.post(new Runnable(){

                @Override
                public void run() {
                    // FIXME: 16-10-25
//                    DModule.ModulePush.cast(PushInterface.class).blockPush();

                    /**
                     * 所有数据的全量加载放在这里，
                     * 	1. 因为数据之间会有依赖关系
                     *  2. 同样事情放在一起，可以减少CPU缓存失效
                     */
	                // TODO: 2016/4/13

                    sendEvent(DEvent.E_DataCenter_UserDBChanged, newDb, oldDb);

                    sendEvent(DEvent.E_DataCenter_UserDBChanged_After, newDb);
// FIXME: 16-10-25
//                    DModule.ModulePush.cast(PushInterface.class).unblockPush();
                }
            });
        }
    }

	@Override
    public void clearDataBase() {
        synchronized (mUserDbLock) {
	        if(mUserDb != null) {
		        mUserDb.close();
	        }

            gMainContext.deleteDatabase(mUserDb.getDatabaseName());

            mUserDb = new JUserDb(gMainContext, DataCenterHelper.buildUserDbName(mCurrentUid));
            mUserDb.open();

            ThreadBus.bus().post(ThreadBus.Main, new Runnable() {

                @Override
                public void run() {
                    // FIXME: 16-10-25
//	                DModule.ModuleUser.cast(UserInterface.class).setLocalForceChangeUser();

                    LoginHelper.login(true);
                }
            });
        }
    }
}
