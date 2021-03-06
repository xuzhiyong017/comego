// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

public final class SetRootRes extends Message {

  public static final Integer DEFAULT_RES = 0;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer res;

  private SetRootRes(Builder builder) {
    this.res = builder.res;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SetRootRes)) return false;
    return equals(res, ((SetRootRes) other).res);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = res != null ? res.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<SetRootRes> {

    public Integer res;

    public Builder() {
    }

    public Builder(SetRootRes message) {
      super(message);
      if (message == null) return;
      this.res = message.res;
    }

    public Builder res(Integer res) {
      this.res = res;
      return this;
    }

    @Override
    public SetRootRes build() {
      return new SetRootRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
