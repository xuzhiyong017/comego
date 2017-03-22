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

    // / add proto event
    public void addProtoDelegate(Integer uri, Object obj, String n);

    // / remvoe proto event
    public void removeProtoDelegate(Integer uri, Object obj, String n);

    // / net dispatcher
    public FwEvent.FwEventDispatcher netDispatcher();

    public void clearClient();

    public void setClient(NetClient client);

    public NetClient newClient();

    public void startHeartBeat();

    public void stopHeartBeat();

    public void sendHeartBeat();
}
