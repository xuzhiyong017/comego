package com.xuzhiyong.comego.module.update;

import com.duowan.fw.FwEvent.EventArg;
import com.xuzhiyong.comego.module.update.UpdateModule;

interface IState {
    void handleEvent(UpdateModule update, EventArg eventArg);
}
