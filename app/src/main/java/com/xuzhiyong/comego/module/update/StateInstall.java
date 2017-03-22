package com.xuzhiyong.comego.module.update;

import com.duowan.fw.FwEvent.EventArg;

class StateInstall implements IState {

    private static final class Holder {
        private static final StateInstall sInstance = new StateInstall();
    }

    static StateInstall instance() {
        return Holder.sInstance;
    }

    @Override
    public void handleEvent(UpdateModule update, EventArg eventArg) {
        UpdateEvent.Event event = UpdateEvent.Event.class.cast(eventArg.event);
        switch (event) {
            case install:
                update.installPackage(eventArg.arg0(String.class));
                break;
            default:
                break;
        }
    }
}
