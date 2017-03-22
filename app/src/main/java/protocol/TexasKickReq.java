// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 踢出玩家
 */
public final class TexasKickReq extends Message {

  public static final Long DEFAULT_ROOMID = 0L;
  public static final Integer DEFAULT_SEATID = 0;
  public static final Long DEFAULT_UID = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long roomId;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer seatId;

  @ProtoField(tag = 3, type = UINT64, label = REQUIRED)
  public final Long uid;

  private TexasKickReq(Builder builder) {
    this.roomId = builder.roomId;
    this.seatId = builder.seatId;
    this.uid = builder.uid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasKickReq)) return false;
    TexasKickReq o = (TexasKickReq) other;
    return equals(roomId, o.roomId)
        && equals(seatId, o.seatId)
        && equals(uid, o.uid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = roomId != null ? roomId.hashCode() : 0;
      result = result * 37 + (seatId != null ? seatId.hashCode() : 0);
      result = result * 37 + (uid != null ? uid.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasKickReq> {

    public Long roomId;
    public Integer seatId;
    public Long uid;

    public Builder() {
    }

    public Builder(TexasKickReq message) {
      super(message);
      if (message == null) return;
      this.roomId = message.roomId;
      this.seatId = message.seatId;
      this.uid = message.uid;
    }

    public Builder roomId(Long roomId) {
      this.roomId = roomId;
      return this;
    }

    public Builder seatId(Integer seatId) {
      this.seatId = seatId;
      return this;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    @Override
    public TexasKickReq build() {
      checkRequiredFields();
      return new TexasKickReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
