// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.FLOAT;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class WealthDrawMoneyRes extends Message {

  public static final Long DEFAULT_MBEAN = 0L;
  public static final Integer DEFAULT_DRAWNUM = 0;
  public static final Float DEFAULT_MONEY = 0F;
  public static final Float DEFAULT_TAXMONEY = 0F;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long mBean;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer drawNum;

  @ProtoField(tag = 3, type = FLOAT)
  public final Float money;

  /**
   * 提取钱数值
   */
  @ProtoField(tag = 4, type = FLOAT)
  public final Float taxMoney;

  private WealthDrawMoneyRes(Builder builder) {
    this.mBean = builder.mBean;
    this.drawNum = builder.drawNum;
    this.money = builder.money;
    this.taxMoney = builder.taxMoney;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof WealthDrawMoneyRes)) return false;
    WealthDrawMoneyRes o = (WealthDrawMoneyRes) other;
    return equals(mBean, o.mBean)
        && equals(drawNum, o.drawNum)
        && equals(money, o.money)
        && equals(taxMoney, o.taxMoney);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = mBean != null ? mBean.hashCode() : 0;
      result = result * 37 + (drawNum != null ? drawNum.hashCode() : 0);
      result = result * 37 + (money != null ? money.hashCode() : 0);
      result = result * 37 + (taxMoney != null ? taxMoney.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<WealthDrawMoneyRes> {

    public Long mBean;
    public Integer drawNum;
    public Float money;
    public Float taxMoney;

    public Builder() {
    }

    public Builder(WealthDrawMoneyRes message) {
      super(message);
      if (message == null) return;
      this.mBean = message.mBean;
      this.drawNum = message.drawNum;
      this.money = message.money;
      this.taxMoney = message.taxMoney;
    }

    public Builder mBean(Long mBean) {
      this.mBean = mBean;
      return this;
    }

    public Builder drawNum(Integer drawNum) {
      this.drawNum = drawNum;
      return this;
    }

    public Builder money(Float money) {
      this.money = money;
      return this;
    }

    /**
     * 提取钱数值
     */
    public Builder taxMoney(Float taxMoney) {
      this.taxMoney = taxMoney;
      return this;
    }

    @Override
    public WealthDrawMoneyRes build() {
      checkRequiredFields();
      return new WealthDrawMoneyRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
