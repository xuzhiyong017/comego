// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class WinChipRankReq extends Message {

  public static final Integer DEFAULT_RANKID = 0;

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer rankId;

  private WinChipRankReq(Builder builder) {
    this.rankId = builder.rankId;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof WinChipRankReq)) return false;
    return equals(rankId, ((WinChipRankReq) other).rankId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = rankId != null ? rankId.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<WinChipRankReq> {

    public Integer rankId;

    public Builder() {
    }

    public Builder(WinChipRankReq message) {
      super(message);
      if (message == null) return;
      this.rankId = message.rankId;
    }

    public Builder rankId(Integer rankId) {
      this.rankId = rankId;
      return this;
    }

    @Override
    public WinChipRankReq build() {
      checkRequiredFields();
      return new WinChipRankReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
