package com.xuzhiyong.comego.module.app;

import android.app.Application;

import com.duowan.fw.Module;
import com.duowan.fw.ModuleCenter;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JFileUtils;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.Login.LoginModule;
import com.xuzhiyong.comego.module.analysis.AnalysisModule;
import com.xuzhiyong.comego.module.datacenter.DataCenterModule;
import com.xuzhiyong.comego.module.download.DownloadModule;
import com.xuzhiyong.comego.module.net.NetModule;
import com.xuzhiyong.comego.module.push.PushModule;
import com.xuzhiyong.comego.module.update.UpdateModule;


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

	    // initialize NetClient int Thread Net, to prevent NetworkOnMainThreadException in StrictMode

        // register all modules, be carefully the sequence
        DModule.ModuleApp.link(new AppModule("ada", "duowan", "android_ada"));

        DModule.ModuleDataCenter.link(new DataCenterModule());

        DModule.ModuleNet.link(new NetModule());

        DModule.ModuleAnalysis.link(new AnalysisModule());

        DModule.ModuleLogin.link(new LoginModule());

        DModule.ModulePush.link(new PushModule());

        DModule.ModuleDownload.link(new DownloadModule());

        DModule.ModuleUpdate.link(new UpdateModule());

        ModuleCenter.sendEventTo(DEvent.E_AllModuleCreated);

        ModuleCenter.gCenter.startWithContext(app);
    }
}
