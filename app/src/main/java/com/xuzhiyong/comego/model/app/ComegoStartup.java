package com.xuzhiyong.comego.model.app;

import android.app.Application;

import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JFileUtils;

/**
 * Created by 91299 on 2017/3/12   0012.
 */

public class ComegoStartup {

    static {

		/*code is far away from bug with the animal protecting
        *  ┏┓　　　┏┓
		*┏┛┻━━━┛┻┓
		*┃　　　　　　　┃ 　
		*┃　　　━　　　┃
		*┃　┳┛　┗┳　┃
		*┃　　　　　　　┃
		*┃　　　┻　　　┃
		*┃　　　　　　　┃
		*┗━┓　　　┏━┛
		*　　┃　　　┃神兽保佑
		*　　┃　　　┃代码无BUG！
		*　　┃　　　┗━━━┓
		*　　┃　　　　　　　┣┓
		*　　┃　　　　　　　┏┛
		*　　┗┓┓┏━┳┓┏┛
		*　　　┃┫┫　┃┫┫
		*　　　┗┻┛　┗┻┛
		*　　　
		*/
    }

    // start entry
    public static void startUp(Application app) {
        // keep the main context in modules
        Module.gMainContext = app;

        // setup folders
        JFileUtils.asyncSetupFolders();

        // initialization the thread bus
        ThreadBus.bus();


    }
}
