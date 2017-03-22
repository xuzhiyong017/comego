package com.xuzhiyong.comego.module.update;
import com.duowan.fw.FwEvent.EventArg;

class StateCheck implements IState {

    private static final class Holder {
        private static final StateCheck sInstance = new StateCheck();
    }

    static StateCheck instance() {
        return Holder.sInstance;
    }

    @Override
    public void handleEvent(UpdateModule update, EventArg eventArg) {
        UpdateEvent.Event event = UpdateEvent.Event.class.cast(eventArg.event);
        switch (event) {
            case download:
                update.download();
                break;
            default:
                break;
        }
    }

}
