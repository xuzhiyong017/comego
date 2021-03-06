// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 设置已读
 */
public final class SetReadMsgReq extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final Long DEFAULT_MSGID = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long uid;

  @ProtoField(tag = 2, type = UINT64, label = REQUIRED)
  public final Long msgId;

  private SetReadMsgReq(Builder builder) {
    this.uid = builder.uid;
    this.msgId = builder.msgId;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SetReadMsgReq)) return false;
    SetReadMsgReq o = (SetReadMsgReq) other;
    return equals(uid, o.uid)
        && equals(msgId, o.msgId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (msgId != null ? msgId.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<SetReadMsgReq> {

    public Long uid;
    public Long msgId;

    public Builder() {
    }

    public Builder(SetReadMsgReq message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.msgId = message.msgId;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder msgId(Long msgId) {
      this.msgId = msgId;
      return this;
    }

    @Override
    public SetReadMsgReq build() {
      checkRequiredFields();
      return new SetReadMsgReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
