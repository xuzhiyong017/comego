// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class TexasMatchRankItem extends Message {

  public static final Long DEFAULT_CHIPS = 0L;
  public static final Long DEFAULT_UID = 0L;

  @ProtoField(tag = 1)
  public final UserInfo userinfo;

  @ProtoField(tag = 2, type = INT64)
  public final Long chips;

  @ProtoField(tag = 9, type = UINT64)
  public final Long Uid;

  private TexasMatchRankItem(Builder builder) {
    this.userinfo = builder.userinfo;
    this.chips = builder.chips;
    this.Uid = builder.Uid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasMatchRankItem)) return false;
    TexasMatchRankItem o = (TexasMatchRankItem) other;
    return equals(userinfo, o.userinfo)
        && equals(chips, o.chips)
        && equals(Uid, o.Uid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = userinfo != null ? userinfo.hashCode() : 0;
      result = result * 37 + (chips != null ? chips.hashCode() : 0);
      result = result * 37 + (Uid != null ? Uid.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasMatchRankItem> {

    public UserInfo userinfo;
    public Long chips;
    public Long Uid;

    public Builder() {
    }

    public Builder(TexasMatchRankItem message) {
      super(message);
      if (message == null) return;
      this.userinfo = message.userinfo;
      this.chips = message.chips;
      this.Uid = message.Uid;
    }

    public Builder userinfo(UserInfo userinfo) {
      this.userinfo = userinfo;
      return this;
    }

    public Builder chips(Long chips) {
      this.chips = chips;
      return this;
    }

    public Builder Uid(Long Uid) {
      this.Uid = Uid;
      return this;
    }

    @Override
    public TexasMatchRankItem build() {
      return new TexasMatchRankItem(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}