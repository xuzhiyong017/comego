// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class RechargeRes extends Message {

  public static final Long DEFAULT_ORDERID = 0L;
  public static final String DEFAULT_PAYURL = "";

  @ProtoField(tag = 1, type = UINT64)
  public final Long orderid;

  @ProtoField(tag = 2, type = STRING)
  public final String payurl;

  private RechargeRes(Builder builder) {
    this.orderid = builder.orderid;
    this.payurl = builder.payurl;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RechargeRes)) return false;
    RechargeRes o = (RechargeRes) other;
    return equals(orderid, o.orderid)
        && equals(payurl, o.payurl);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = orderid != null ? orderid.hashCode() : 0;
      result = result * 37 + (payurl != null ? payurl.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<RechargeRes> {

    public Long orderid;
    public String payurl;

    public Builder() {
    }

    public Builder(RechargeRes message) {
      super(message);
      if (message == null) return;
      this.orderid = message.orderid;
      this.payurl = message.payurl;
    }

    public Builder orderid(Long orderid) {
      this.orderid = orderid;
      return this;
    }

    public Builder payurl(String payurl) {
      this.payurl = payurl;
      return this;
    }

    @Override
    public RechargeRes build() {
      return new RechargeRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
