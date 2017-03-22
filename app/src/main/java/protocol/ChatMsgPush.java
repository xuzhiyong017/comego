// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 消息推送
 */
public final class ChatMsgPush extends Message {

  public static final Long DEFAULT_FROMUID = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long fromUid;

  @ProtoField(tag = 2, label = REQUIRED)
  public final ChatMsgInfo msgInfo;

  private ChatMsgPush(Builder builder) {
    this.fromUid = builder.fromUid;
    this.msgInfo = builder.msgInfo;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ChatMsgPush)) return false;
    ChatMsgPush o = (ChatMsgPush) other;
    return equals(fromUid, o.fromUid)
        && equals(msgInfo, o.msgInfo);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = fromUid != null ? fromUid.hashCode() : 0;
      result = result * 37 + (msgInfo != null ? msgInfo.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<ChatMsgPush> {

    public Long fromUid;
    public ChatMsgInfo msgInfo;

    public Builder() {
    }

    public Builder(ChatMsgPush message) {
      super(message);
      if (message == null) return;
      this.fromUid = message.fromUid;
      this.msgInfo = message.msgInfo;
    }

    public Builder fromUid(Long fromUid) {
      this.fromUid = fromUid;
      return this;
    }

    public Builder msgInfo(ChatMsgInfo msgInfo) {
      this.msgInfo = msgInfo;
      return this;
    }

    @Override
    public ChatMsgPush build() {
      checkRequiredFields();
      return new ChatMsgPush(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}