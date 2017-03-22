// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 添加 减少 财富
 */
public final class ChangeWealthReq extends Message {

  public static final List<Long> DEFAULT_UIDS = Collections.emptyList();
  public static final Integer DEFAULT_CHANGETYPE = 0;
  public static final Long DEFAULT_CHANGENUM = 0L;
  public static final Integer DEFAULT_REASON = 0;

  @ProtoField(tag = 1, type = UINT64, label = REPEATED)
  public final List<Long> uids;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer changeType;

  /**
   * 1mGold 2mBean 3mChip
   */
  @ProtoField(tag = 3, type = INT64, label = REQUIRED)
  public final Long changeNum;

  @ProtoField(tag = 4, type = UINT32, label = REQUIRED)
  public final Integer reason;

  private ChangeWealthReq(Builder builder) {
    this.uids = immutableCopyOf(builder.uids);
    this.changeType = builder.changeType;
    this.changeNum = builder.changeNum;
    this.reason = builder.reason;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ChangeWealthReq)) return false;
    ChangeWealthReq o = (ChangeWealthReq) other;
    return equals(uids, o.uids)
        && equals(changeType, o.changeType)
        && equals(changeNum, o.changeNum)
        && equals(reason, o.reason);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uids != null ? uids.hashCode() : 1;
      result = result * 37 + (changeType != null ? changeType.hashCode() : 0);
      result = result * 37 + (changeNum != null ? changeNum.hashCode() : 0);
      result = result * 37 + (reason != null ? reason.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<ChangeWealthReq> {

    public List<Long> uids;
    public Integer changeType;
    public Long changeNum;
    public Integer reason;

    public Builder() {
    }

    public Builder(ChangeWealthReq message) {
      super(message);
      if (message == null) return;
      this.uids = copyOf(message.uids);
      this.changeType = message.changeType;
      this.changeNum = message.changeNum;
      this.reason = message.reason;
    }

    public Builder uids(List<Long> uids) {
      this.uids = checkForNulls(uids);
      return this;
    }

    public Builder changeType(Integer changeType) {
      this.changeType = changeType;
      return this;
    }

    /**
     * 1mGold 2mBean 3mChip
     */
    public Builder changeNum(Long changeNum) {
      this.changeNum = changeNum;
      return this;
    }

    public Builder reason(Integer reason) {
      this.reason = reason;
      return this;
    }

    @Override
    public ChangeWealthReq build() {
      checkRequiredFields();
      return new ChangeWealthReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
