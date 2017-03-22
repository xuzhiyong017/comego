// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class TexasRoomWarRecord extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final String DEFAULT_LOGOURL = "";
  public static final String DEFAULT_NAME = "";
  public static final Long DEFAULT_CHIPS = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long uid;

  @ProtoField(tag = 2, type = STRING, label = REQUIRED)
  public final String logourl;

  @ProtoField(tag = 3, type = STRING, label = REQUIRED)
  public final String name;

  @ProtoField(tag = 4, type = INT64, label = REQUIRED)
  public final Long chips;

  private TexasRoomWarRecord(Builder builder) {
    this.uid = builder.uid;
    this.logourl = builder.logourl;
    this.name = builder.name;
    this.chips = builder.chips;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasRoomWarRecord)) return false;
    TexasRoomWarRecord o = (TexasRoomWarRecord) other;
    return equals(uid, o.uid)
        && equals(logourl, o.logourl)
        && equals(name, o.name)
        && equals(chips, o.chips);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (logourl != null ? logourl.hashCode() : 0);
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (chips != null ? chips.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasRoomWarRecord> {

    public Long uid;
    public String logourl;
    public String name;
    public Long chips;

    public Builder() {
    }

    public Builder(TexasRoomWarRecord message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.logourl = message.logourl;
      this.name = message.name;
      this.chips = message.chips;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder logourl(String logourl) {
      this.logourl = logourl;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder chips(Long chips) {
      this.chips = chips;
      return this;
    }

    @Override
    public TexasRoomWarRecord build() {
      checkRequiredFields();
      return new TexasRoomWarRecord(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}