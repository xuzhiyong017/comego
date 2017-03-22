package com.xuzhiyong.comego.module.update;

import com.duowan.fw.util.JUtils;

public enum States {
    idle,
    check,
    download,
    apply,
    install;

    static IState state(States state) {
        switch (state) {
            case idle:
                return StateIdle.instance();
            case check:
                return StateCheck.instance();
            case download:
                return StateDownload.instance();
            case apply:
                return StateApply.instance();
            case install:
                return StateInstall.instance();
            default:
                JUtils.jAssert(false);
                return null;
        }
    }
}
