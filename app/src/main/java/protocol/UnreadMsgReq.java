// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

/**
 * 请求未读消息数
 */
public final class UnreadMsgReq extends Message {

  private UnreadMsgReq(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof UnreadMsgReq;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<UnreadMsgReq> {

    public Builder() {
    }

    public Builder(UnreadMsgReq message) {
      super(message);
    }

    @Override
    public UnreadMsgReq build() {
      return new UnreadMsgReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
