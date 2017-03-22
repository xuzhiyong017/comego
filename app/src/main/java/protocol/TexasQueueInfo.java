// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class TexasQueueInfo extends Message {

  public static final Integer DEFAULT_QUEUELEN = 0;
  public static final Integer DEFAULT_QUEUEPOS = 0;
  public static final Long DEFAULT_HAVEAWARD = 0L;
  public static final Long DEFAULT_WAITSEC = 0L;
  public static final Long DEFAULT_REWARDSEC = 0L;
  public static final Long DEFAULT_REWARDMAX = 0L;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer queueLen;

  /**
   * 排队中的人数
   */
  @ProtoField(tag = 2, type = UINT32)
  public final Integer queuePos;

  /**
   * 排第几位
   */
  @ProtoField(tag = 3, type = UINT64)
  public final Long haveAward;

  /**
   * 已获得补偿
   */
  @ProtoField(tag = 4, type = UINT64)
  public final Long waitsec;

  /**
   * 本次排队等待了多少秒
   */
  @ProtoField(tag = 5, type = UINT64)
  public final Long rewardSec;

  /**
   * 每分钟奖励多少
   */
  @ProtoField(tag = 6, type = UINT64)
  public final Long rewardMax;

  private TexasQueueInfo(Builder builder) {
    this.queueLen = builder.queueLen;
    this.queuePos = builder.queuePos;
    this.haveAward = builder.haveAward;
    this.waitsec = builder.waitsec;
    this.rewardSec = builder.rewardSec;
    this.rewardMax = builder.rewardMax;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasQueueInfo)) return false;
    TexasQueueInfo o = (TexasQueueInfo) other;
    return equals(queueLen, o.queueLen)
        && equals(queuePos, o.queuePos)
        && equals(haveAward, o.haveAward)
        && equals(waitsec, o.waitsec)
        && equals(rewardSec, o.rewardSec)
        && equals(rewardMax, o.rewardMax);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = queueLen != null ? queueLen.hashCode() : 0;
      result = result * 37 + (queuePos != null ? queuePos.hashCode() : 0);
      result = result * 37 + (haveAward != null ? haveAward.hashCode() : 0);
      result = result * 37 + (waitsec != null ? waitsec.hashCode() : 0);
      result = result * 37 + (rewardSec != null ? rewardSec.hashCode() : 0);
      result = result * 37 + (rewardMax != null ? rewardMax.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasQueueInfo> {

    public Integer queueLen;
    public Integer queuePos;
    public Long haveAward;
    public Long waitsec;
    public Long rewardSec;
    public Long rewardMax;

    public Builder() {
    }

    public Builder(TexasQueueInfo message) {
      super(message);
      if (message == null) return;
      this.queueLen = message.queueLen;
      this.queuePos = message.queuePos;
      this.haveAward = message.haveAward;
      this.waitsec = message.waitsec;
      this.rewardSec = message.rewardSec;
      this.rewardMax = message.rewardMax;
    }

    public Builder queueLen(Integer queueLen) {
      this.queueLen = queueLen;
      return this;
    }

    /**
     * 排队中的人数
     */
    public Builder queuePos(Integer queuePos) {
      this.queuePos = queuePos;
      return this;
    }

    /**
     * 排第几位
     */
    public Builder haveAward(Long haveAward) {
      this.haveAward = haveAward;
      return this;
    }

    /**
     * 已获得补偿
     */
    public Builder waitsec(Long waitsec) {
      this.waitsec = waitsec;
      return this;
    }

    /**
     * 本次排队等待了多少秒
     */
    public Builder rewardSec(Long rewardSec) {
      this.rewardSec = rewardSec;
      return this;
    }

    /**
     * 每分钟奖励多少
     */
    public Builder rewardMax(Long rewardMax) {
      this.rewardMax = rewardMax;
      return this;
    }

    @Override
    public TexasQueueInfo build() {
      return new TexasQueueInfo(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}