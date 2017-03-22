package com.xuzhiyong.comego.module.update;

import com.duowan.fw.FwEvent.EventArg;
import com.xuzhiyong.comego.module.download.DownloadSetup;


class StateApply implements IState {

    private static final class Holder {
        private static final StateApply sInstance = new StateApply();
    }

    static StateApply instance() {
        return Holder.sInstance;
    }

    @Override
    public void handleEvent(UpdateModule update, EventArg eventArg) {
        UpdateEvent.Event event = UpdateEvent.Event.class.cast(eventArg.event);
        switch (event) {
            case apply:
                update.applyDownload(eventArg.arg0(DownloadSetup.JDownloadInfo.class));
                break;
            default:
                break;
        }
    }
}
