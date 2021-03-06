// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 发送消息
 */
public final class SendMsgReq extends Message {

  public static final Long DEFAULT_TOUID = 0L;
  public static final Long DEFAULT_CLIENTTIME = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long toUid;

  @ProtoField(tag = 2, label = REQUIRED)
  public final ChatMsgInfo msgInfo;

  @ProtoField(tag = 3, type = INT64)
  public final Long clientTime;

  private SendMsgReq(Builder builder) {
    this.toUid = builder.toUid;
    this.msgInfo = builder.msgInfo;
    this.clientTime = builder.clientTime;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SendMsgReq)) return false;
    SendMsgReq o = (SendMsgReq) other;
    return equals(toUid, o.toUid)
        && equals(msgInfo, o.msgInfo)
        && equals(clientTime, o.clientTime);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = toUid != null ? toUid.hashCode() : 0;
      result = result * 37 + (msgInfo != null ? msgInfo.hashCode() : 0);
      result = result * 37 + (clientTime != null ? clientTime.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<SendMsgReq> {

    public Long toUid;
    public ChatMsgInfo msgInfo;
    public Long clientTime;

    public Builder() {
    }

    public Builder(SendMsgReq message) {
      super(message);
      if (message == null) return;
      this.toUid = message.toUid;
      this.msgInfo = message.msgInfo;
      this.clientTime = message.clientTime;
    }

    public Builder toUid(Long toUid) {
      this.toUid = toUid;
      return this;
    }

    public Builder msgInfo(ChatMsgInfo msgInfo) {
      this.msgInfo = msgInfo;
      return this;
    }

    public Builder clientTime(Long clientTime) {
      this.clientTime = clientTime;
      return this;
    }

    @Override
    public SendMsgReq build() {
      checkRequiredFields();
      return new SendMsgReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
