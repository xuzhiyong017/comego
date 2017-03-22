package com.xuzhiyong.comego.module.datacenter;

/**
 * Created by hdyra on 2015/10/12.
 *
 */
public interface DataCenterInterface {

    public JDb userDb();
    public JDb appDb();

	public void clearDataBase();
}
