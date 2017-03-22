// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class TexasExitRoomRes extends Message {

  public static final Integer DEFAULT_RES = 0;

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer res;

  private TexasExitRoomRes(Builder builder) {
    this.res = builder.res;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasExitRoomRes)) return false;
    return equals(res, ((TexasExitRoomRes) other).res);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = res != null ? res.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<TexasExitRoomRes> {

    public Integer res;

    public Builder() {
    }

    public Builder(TexasExitRoomRes message) {
      super(message);
      if (message == null) return;
      this.res = message.res;
    }

    public Builder res(Integer res) {
      this.res = res;
      return this;
    }

    @Override
    public TexasExitRoomRes build() {
      checkRequiredFields();
      return new TexasExitRoomRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
