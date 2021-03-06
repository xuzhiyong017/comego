// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;

public final class TexasUserStatReq extends Message {

  public static final Long DEFAULT_UID = 0L;

  @ProtoField(tag = 1, type = UINT64)
  public final Long uid;

  private TexasUserStatReq(Builder builder) {
    this.uid = builder.uid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasUserStatReq)) return false;
    return equals(uid, ((TexasUserStatReq) other).uid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = uid != null ? uid.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<TexasUserStatReq> {

    public Long uid;

    public Builder() {
    }

    public Builder(TexasUserStatReq message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    @Override
    public TexasUserStatReq build() {
      return new TexasUserStatReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
