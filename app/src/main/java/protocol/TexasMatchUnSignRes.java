// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class TexasMatchUnSignRes extends Message {

  public static final Integer DEFAULT_ACTID = 0;
  public static final Long DEFAULT_UID = 0L;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer actId;

  @ProtoField(tag = 2, type = UINT64)
  public final Long uid;

  private TexasMatchUnSignRes(Builder builder) {
    this.actId = builder.actId;
    this.uid = builder.uid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasMatchUnSignRes)) return false;
    TexasMatchUnSignRes o = (TexasMatchUnSignRes) other;
    return equals(actId, o.actId)
        && equals(uid, o.uid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = actId != null ? actId.hashCode() : 0;
      result = result * 37 + (uid != null ? uid.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasMatchUnSignRes> {

    public Integer actId;
    public Long uid;

    public Builder() {
    }

    public Builder(TexasMatchUnSignRes message) {
      super(message);
      if (message == null) return;
      this.actId = message.actId;
      this.uid = message.uid;
    }

    public Builder actId(Integer actId) {
      this.actId = actId;
      return this;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    @Override
    public TexasMatchUnSignRes build() {
      return new TexasMatchUnSignRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
