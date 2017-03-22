// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 开始游戏
 */
public final class TexasStartReq extends Message {

  public static final Long DEFAULT_ROOMID = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long roomId;

  private TexasStartReq(Builder builder) {
    this.roomId = builder.roomId;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasStartReq)) return false;
    return equals(roomId, ((TexasStartReq) other).roomId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = roomId != null ? roomId.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<TexasStartReq> {

    public Long roomId;

    public Builder() {
    }

    public Builder(TexasStartReq message) {
      super(message);
      if (message == null) return;
      this.roomId = message.roomId;
    }

    public Builder roomId(Long roomId) {
      this.roomId = roomId;
      return this;
    }

    @Override
    public TexasStartReq build() {
      checkRequiredFields();
      return new TexasStartReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
