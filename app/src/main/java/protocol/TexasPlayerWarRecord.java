// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class TexasPlayerWarRecord extends Message {

  public static final Long DEFAULT_ROOMID = 0L;
  public static final Long DEFAULT_THETIME = 0L;
  public static final Long DEFAULT_CHIPS = 0L;
  public static final String DEFAULT_ROOMNAME = "";

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long roomId;

  @ProtoField(tag = 2, type = INT64, label = REQUIRED)
  public final Long theTime;

  @ProtoField(tag = 3, type = INT64, label = REQUIRED)
  public final Long chips;

  @ProtoField(tag = 4, type = STRING, label = REQUIRED)
  public final String roomName;

  private TexasPlayerWarRecord(Builder builder) {
    this.roomId = builder.roomId;
    this.theTime = builder.theTime;
    this.chips = builder.chips;
    this.roomName = builder.roomName;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasPlayerWarRecord)) return false;
    TexasPlayerWarRecord o = (TexasPlayerWarRecord) other;
    return equals(roomId, o.roomId)
        && equals(theTime, o.theTime)
        && equals(chips, o.chips)
        && equals(roomName, o.roomName);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = roomId != null ? roomId.hashCode() : 0;
      result = result * 37 + (theTime != null ? theTime.hashCode() : 0);
      result = result * 37 + (chips != null ? chips.hashCode() : 0);
      result = result * 37 + (roomName != null ? roomName.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasPlayerWarRecord> {

    public Long roomId;
    public Long theTime;
    public Long chips;
    public String roomName;

    public Builder() {
    }

    public Builder(TexasPlayerWarRecord message) {
      super(message);
      if (message == null) return;
      this.roomId = message.roomId;
      this.theTime = message.theTime;
      this.chips = message.chips;
      this.roomName = message.roomName;
    }

    public Builder roomId(Long roomId) {
      this.roomId = roomId;
      return this;
    }

    public Builder theTime(Long theTime) {
      this.theTime = theTime;
      return this;
    }

    public Builder chips(Long chips) {
      this.chips = chips;
      return this;
    }

    public Builder roomName(String roomName) {
      this.roomName = roomName;
      return this;
    }

    @Override
    public TexasPlayerWarRecord build() {
      checkRequiredFields();
      return new TexasPlayerWarRecord(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
