// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class LikeItMsgRes extends Message {

  private LikeItMsgRes(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof LikeItMsgRes;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<LikeItMsgRes> {

    public Builder() {
    }

    public Builder(LikeItMsgRes message) {
      super(message);
    }

    @Override
    public LikeItMsgRes build() {
      return new LikeItMsgRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
