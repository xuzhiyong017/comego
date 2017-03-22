// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class YYBabyPromotionReq extends Message {

  public static final Long DEFAULT_YYNO = 0L;
  public static final Integer DEFAULT_ACTID = 0;

  @ProtoField(tag = 1, type = UINT64)
  public final Long yyno;

  /**
   * YY号
   */
  @ProtoField(tag = 2, type = UINT32)
  public final Integer actId;

  private YYBabyPromotionReq(Builder builder) {
    this.yyno = builder.yyno;
    this.actId = builder.actId;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof YYBabyPromotionReq)) return false;
    YYBabyPromotionReq o = (YYBabyPromotionReq) other;
    return equals(yyno, o.yyno)
        && equals(actId, o.actId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = yyno != null ? yyno.hashCode() : 0;
      result = result * 37 + (actId != null ? actId.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<YYBabyPromotionReq> {

    public Long yyno;
    public Integer actId;

    public Builder() {
    }

    public Builder(YYBabyPromotionReq message) {
      super(message);
      if (message == null) return;
      this.yyno = message.yyno;
      this.actId = message.actId;
    }

    public Builder yyno(Long yyno) {
      this.yyno = yyno;
      return this;
    }

    /**
     * YY号
     */
    public Builder actId(Integer actId) {
      this.actId = actId;
      return this;
    }

    @Override
    public YYBabyPromotionReq build() {
      return new YYBabyPromotionReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
