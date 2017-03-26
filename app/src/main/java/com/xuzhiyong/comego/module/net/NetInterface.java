package com.xuzhiyong.comego.module.net;

import com.duowan.fw.FwEvent;

/**
 * Created by Administrator on 2015/10/12.
 *
 */
public interface NetInterface {

    // / send proto to net
    public void sendProto(final Proto proto);

    // / send proto to net, do not check is online
    public void sendProtoDirect(final Proto proto);

    // / net dispatcher
    public FwEvent.FwEventDispatcher netDispatcher();

    void switchEnvironment();

    void sendProtoWithToken(final Proto proto);

    /// add proto event
    void addProtoDelegate(Integer uri, Object obj, String n);

    /// remvoe proto event
    void removeProtoDelegate(Integer uri, Object obj, String n);
}
