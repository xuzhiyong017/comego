// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class TexasMatchInfoReq extends Message {

  public static final Integer DEFAULT_ACTID = 0;
  public static final Long DEFAULT_OWNERUID = 0L;
  public static final Integer DEFAULT_GETLABEL = 0;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer actId;

  /**
   * 通过比赛ID查
   */
  @ProtoField(tag = 2, type = UINT64)
  public final Long ownerUid;

  /**
   * 通过主播UID查，只带少量信息
   */
  @ProtoField(tag = 3, type = UINT32)
  public final Integer getLabel;

  private TexasMatchInfoReq(Builder builder) {
    this.actId = builder.actId;
    this.ownerUid = builder.ownerUid;
    this.getLabel = builder.getLabel;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasMatchInfoReq)) return false;
    TexasMatchInfoReq o = (TexasMatchInfoReq) other;
    return equals(actId, o.actId)
        && equals(ownerUid, o.ownerUid)
        && equals(getLabel, o.getLabel);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = actId != null ? actId.hashCode() : 0;
      result = result * 37 + (ownerUid != null ? ownerUid.hashCode() : 0);
      result = result * 37 + (getLabel != null ? getLabel.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasMatchInfoReq> {

    public Integer actId;
    public Long ownerUid;
    public Integer getLabel;

    public Builder() {
    }

    public Builder(TexasMatchInfoReq message) {
      super(message);
      if (message == null) return;
      this.actId = message.actId;
      this.ownerUid = message.ownerUid;
      this.getLabel = message.getLabel;
    }

    public Builder actId(Integer actId) {
      this.actId = actId;
      return this;
    }

    /**
     * 通过比赛ID查
     */
    public Builder ownerUid(Long ownerUid) {
      this.ownerUid = ownerUid;
      return this;
    }

    /**
     * 通过主播UID查，只带少量信息
     */
    public Builder getLabel(Integer getLabel) {
      this.getLabel = getLabel;
      return this;
    }

    @Override
    public TexasMatchInfoReq build() {
      return new TexasMatchInfoReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
