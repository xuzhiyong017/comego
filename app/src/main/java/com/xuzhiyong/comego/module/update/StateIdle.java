package com.xuzhiyong.comego.module.update;

import com.duowan.fw.FwEvent.EventArg;

class StateIdle implements IState {

    private static final class Holder {
        private static final StateIdle sInstance = new StateIdle();
    }

    static StateIdle instance() {
        return Holder.sInstance;
    }

    @Override
    public void handleEvent(UpdateModule update, EventArg eventArg) {
        UpdateEvent.Event event = UpdateEvent.Event.class.cast(eventArg.event);
        switch (event) {
            case check_update:
                update.checkUpdate(eventArg.arg0(Boolean.class));
                break;
            case full_download:
//                update.downloadApk(eventArg.arg0(String.class));
                break;
            default:
                break;
        }
    }
}
