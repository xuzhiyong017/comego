// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class RechargePayGateCallbackRes extends Message {

  private RechargePayGateCallbackRes(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof RechargePayGateCallbackRes;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<RechargePayGateCallbackRes> {

    public Builder() {
    }

    public Builder(RechargePayGateCallbackRes message) {
      super(message);
    }

    @Override
    public RechargePayGateCallbackRes build() {
      return new RechargePayGateCallbackRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
