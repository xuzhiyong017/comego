// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 玩家准备
 */
public final class TexasMyReadyReq extends Message {

  public static final Long DEFAULT_ROOMID = 0L;
  public static final Integer DEFAULT_SEATID = 0;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long roomId;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer seatId;

  private TexasMyReadyReq(Builder builder) {
    this.roomId = builder.roomId;
    this.seatId = builder.seatId;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasMyReadyReq)) return false;
    TexasMyReadyReq o = (TexasMyReadyReq) other;
    return equals(roomId, o.roomId)
        && equals(seatId, o.seatId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = roomId != null ? roomId.hashCode() : 0;
      result = result * 37 + (seatId != null ? seatId.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasMyReadyReq> {

    public Long roomId;
    public Integer seatId;

    public Builder() {
    }

    public Builder(TexasMyReadyReq message) {
      super(message);
      if (message == null) return;
      this.roomId = message.roomId;
      this.seatId = message.seatId;
    }

    public Builder roomId(Long roomId) {
      this.roomId = roomId;
      return this;
    }

    public Builder seatId(Integer seatId) {
      this.seatId = seatId;
      return this;
    }

    @Override
    public TexasMyReadyReq build() {
      checkRequiredFields();
      return new TexasMyReadyReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
