package com.xuzhiyong.comego.module.update;

import com.duowan.fw.FwEvent.EventArg;

interface IState {
    void handleEvent(UpdateModule update, EventArg eventArg);
}
