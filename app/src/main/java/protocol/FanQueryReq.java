// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class FanQueryReq extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final Long DEFAULT_PEERUID = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long uid;

  @ProtoField(tag = 3, type = UINT64)
  public final Long peeruid;

  private FanQueryReq(Builder builder) {
    this.uid = builder.uid;
    this.peeruid = builder.peeruid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof FanQueryReq)) return false;
    FanQueryReq o = (FanQueryReq) other;
    return equals(uid, o.uid)
        && equals(peeruid, o.peeruid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (peeruid != null ? peeruid.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<FanQueryReq> {

    public Long uid;
    public Long peeruid;

    public Builder() {
    }

    public Builder(FanQueryReq message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.peeruid = message.peeruid;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder peeruid(Long peeruid) {
      this.peeruid = peeruid;
      return this;
    }

    @Override
    public FanQueryReq build() {
      checkRequiredFields();
      return new FanQueryReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}