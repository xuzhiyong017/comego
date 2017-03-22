package com.xuzhiyong.comego.module.net;

import com.duowan.fw.FwEvent;
import com.duowan.fw.util.JStringUtils;
import com.duowan.fw.util.JUtils;
import com.squareup.wire.ProtoEnum;
import com.xuzhiyong.comego.module.DModule;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import protocol.PType;
import protocol.ProtoBody;
import protocol.SPEncrypt;
import protocol.SPLogin;

public class Proto {

    /// proto data wrap
    public ProtoBody body;
    /// lenght and uri
    public ProtoHead head;
    /// send tick
    public long ts;

    public Proto() {

    }

    public Proto(ProtoHead _head, ProtoBody _body) {
        head = _head;
        body = _body;
    }

    @Override
    public String toString() {
        if (head == null || body == null) {
            return "[##NULL##]";
        }
        return JStringUtils.combineStr("[##Head##]: ", this.head.toString(),
                " [##Body##]: ", this.body.toString());
    }

    /// get protocol body
    public ProtoBody getBody() {
        return body;
    }

    /// get protocol head
    public ProtoHead getHead() {
        return head;
    }

    /// eight byte
    public static class ProtoHead {
        /// const
        public final static int HeadSize = 12;

        /// data members
        public byte group;
        public byte sub;
        public byte flag1;
        public byte flag2;
        public int length;// little endian
        public int seq;

        @Override
        public String toString() {
            return "group: " + getGroupProtocolName()
                    + " sub:" + getSubProtocolName()
                    + " seq:" + seq
                    + " len:" + length;
        }

        /// get uri of proto
        public int getUri() {
            return ((group & 0xFF) << 24 | (sub & 0xFF) << 16);
        }

        /// get len of proto
        public int getLen() {
            return length;
        }

        /// get group
        public int getGroup() {
            return group & 0xFF;
        }

        /// get sub
        public int getSub() {
            return sub & 0xFF;
        }

        /// get seq
        public int getSeq() {
            return seq;
        }

        /// get protocol type
        public PType getProtocolType() {
            return PType.valueOf(getGroup());
        }

        ///
        public String getProtocolTypeName() {
            PType type = getProtocolType();
            if (type != null) {
                return type.toString();
            }
            return "Unknow group protocol: " + getGroup();
        }

        ///
        public String getGroupProtocolName() {
            return getProtocolTypeName();
        }

        ///
        public String getSubProtocolName() {
	        ProtoEnum e = getSubProtolType(getGroup(), getSub());
            if (e != null) {
                return e.toString();
            }
            return "Unknow sub protocol: " + getSub();
        }

        ///
        public static ProtoEnum getSubProtolType(int group, int sub) {
            switch (group) {
	            case PType.PEncrypt_VALUE:
                    return SPEncrypt.valueOf(sub);
	            case PType.PLogin_VALUE:
                    return SPLogin.valueOf(sub);
                // FIXME: 16-10-25
            }
            return null;
        }

        /// serializale to byte array
        public byte[] toByteArray() {
            byte[] data = new byte[HeadSize];
            ByteBuffer buffer = ByteBuffer.wrap(data);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.position(0);
            buffer.put(group);
            buffer.put(sub);
            buffer.put(flag1);
            buffer.put(flag2);
            buffer.putInt(length);
            buffer.putInt(seq);

            return data;
        }

        /**
         * eight byte head: little endian
         * first four byte: group | sub | flag1 | flag2 |
         * second four byte: len & 0xFF | len >>> 8 & 0xFF | len >>> 16 & 0xFF | len >>> 24 & 0xFF
         */
        public static ProtoHead parseFrom(byte[] data) {
            JUtils.jAssert(data != null && data.length >= HeadSize);
            ProtoHead head = new ProtoHead();

            ByteBuffer buffer = ByteBuffer.wrap(data);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            head.group = buffer.get();
            head.sub = buffer.get();
            head.flag1 = buffer.get();
            head.flag2 = buffer.get();
            head.length = buffer.getInt();
            head.seq = buffer.getInt();

            return head;
        }
    }

    // auto binding all annotation events
    public static final void autoBindingProto(Object target) {
        FwEvent.autoBindingEvent(DModule.ModuleNet.cast(NetInterface.class).netDispatcher(), DNetDelegateDestination.BUILDER, target);
    }

    // auto remove all annotation events
    public static final void autoRemoveProto(Object target) {
        FwEvent.autoRemoveEvent(DModule.ModuleNet.cast(NetInterface.class).netDispatcher(), DNetDelegateDestination.BUILDER, target);
    }
}