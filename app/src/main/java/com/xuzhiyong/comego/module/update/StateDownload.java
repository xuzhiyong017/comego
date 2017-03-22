package com.xuzhiyong.comego.module.update;

import com.duowan.fw.FwEvent.EventArg;

class StateDownload implements IState {

    private static final class Holder {
        private static final StateDownload sInstance = new StateDownload();
    }

    static StateDownload instance() {
        return Holder.sInstance;
    }

    @Override
    public void handleEvent(UpdateModule update, EventArg eventArg) {

    }
}
