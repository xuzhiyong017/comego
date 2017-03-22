package com.xuzhiyong.comego.module.update;

import com.duowan.fw.FwEvent;

public class UpdateEvent {

    enum Event {
        check_update,
        download,
        apply,
        install,
        full_download,
    }

    public static final String UpdateEvent_CheckUpdate = "update_event_check_update";
    public static final String UpdateEvent_Download = "update_event_download";
    public static final String UpdateEvent_Apply = "update_event_apply";
    public static final String UpdateEvent_Install = "update_event_install";

    public static final int CheckUpdate_Started = 1;
    public static final int CheckUpdate_Failed = 2;
    public static final int CheckUpdate_Latest = 3;
    public static final int CheckUpdate_Newer = 4;

    public static final long DownloadUpdate_Started = 1L;
    public static final long DownloadUpdate_Failed = -100L;

    public static final int Apply_Start = 1;
    public static final int Apply_Success = 2;
    public static final int Apply_Failed = 3;

    public static final int Install_Failed = 1;

    public static FwEvent.FwEventDispatcher dispatcher = new FwEvent.FwEventDispatcher();

    public static final void autoBindingEvent(Object target){
        FwEvent.autoBindingEvent(dispatcher, FwEvent.FwEventDestination.BUILDER, target);
    }

    public static final void autoRemoveEvent(Object target){
        FwEvent.autoRemoveEvent(dispatcher, FwEvent.FwEventDestination.BUILDER, target);
    }

    public static void notifyEvent(Object source, String event, Object... objs) {
        dispatcher.notifyEvent(
                FwEvent.EventArg.buildEventWithArg(source, event, objs));
    }

}
