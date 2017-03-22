// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 请求充值
 */
public final class RechargeReq extends Message {

  public static final Integer DEFAULT_ITEMID = 0;
  public static final Integer DEFAULT_NUM = 0;
  public static final String DEFAULT_CHID = "Zfb";
  public static final String DEFAULT_PAYMETHOD = "WapApp";
  public static final String DEFAULT_TEL = "";
  public static final String DEFAULT_CARDNUM = "";
  public static final String DEFAULT_CARDPASS = "";
  public static final Long DEFAULT_UID = 0L;
  public static final String DEFAULT_OPENID = "";

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer itemId;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer num;

  @ProtoField(tag = 3, type = STRING)
  public final String chId;

  /**
   * Zfb-WapApp | apple | Unionpay | Szf/19Pay |Weixin-WapApp|Weixin-Mp
   */
  @ProtoField(tag = 4, type = STRING)
  public final String payMethod;

  /**
   * WapAlipay | WapApp | appStore | Lt | Szx
   */
  @ProtoField(tag = 5, type = STRING)
  public final String tel;

  @ProtoField(tag = 6, type = STRING)
  public final String cardNum;

  @ProtoField(tag = 7, type = STRING)
  public final String cardPass;

  @ProtoField(tag = 8, type = UINT64)
  public final Long uid;

  @ProtoField(tag = 9, type = STRING)
  public final String openid;

  private RechargeReq(Builder builder) {
    this.itemId = builder.itemId;
    this.num = builder.num;
    this.chId = builder.chId;
    this.payMethod = builder.payMethod;
    this.tel = builder.tel;
    this.cardNum = builder.cardNum;
    this.cardPass = builder.cardPass;
    this.uid = builder.uid;
    this.openid = builder.openid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RechargeReq)) return false;
    RechargeReq o = (RechargeReq) other;
    return equals(itemId, o.itemId)
        && equals(num, o.num)
        && equals(chId, o.chId)
        && equals(payMethod, o.payMethod)
        && equals(tel, o.tel)
        && equals(cardNum, o.cardNum)
        && equals(cardPass, o.cardPass)
        && equals(uid, o.uid)
        && equals(openid, o.openid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = itemId != null ? itemId.hashCode() : 0;
      result = result * 37 + (num != null ? num.hashCode() : 0);
      result = result * 37 + (chId != null ? chId.hashCode() : 0);
      result = result * 37 + (payMethod != null ? payMethod.hashCode() : 0);
      result = result * 37 + (tel != null ? tel.hashCode() : 0);
      result = result * 37 + (cardNum != null ? cardNum.hashCode() : 0);
      result = result * 37 + (cardPass != null ? cardPass.hashCode() : 0);
      result = result * 37 + (uid != null ? uid.hashCode() : 0);
      result = result * 37 + (openid != null ? openid.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<RechargeReq> {

    public Integer itemId;
    public Integer num;
    public String chId;
    public String payMethod;
    public String tel;
    public String cardNum;
    public String cardPass;
    public Long uid;
    public String openid;

    public Builder() {
    }

    public Builder(RechargeReq message) {
      super(message);
      if (message == null) return;
      this.itemId = message.itemId;
      this.num = message.num;
      this.chId = message.chId;
      this.payMethod = message.payMethod;
      this.tel = message.tel;
      this.cardNum = message.cardNum;
      this.cardPass = message.cardPass;
      this.uid = message.uid;
      this.openid = message.openid;
    }

    public Builder itemId(Integer itemId) {
      this.itemId = itemId;
      return this;
    }

    public Builder num(Integer num) {
      this.num = num;
      return this;
    }

    public Builder chId(String chId) {
      this.chId = chId;
      return this;
    }

    /**
     * Zfb-WapApp | apple | Unionpay | Szf/19Pay |Weixin-WapApp|Weixin-Mp
     */
    public Builder payMethod(String payMethod) {
      this.payMethod = payMethod;
      return this;
    }

    /**
     * WapAlipay | WapApp | appStore | Lt | Szx
     */
    public Builder tel(String tel) {
      this.tel = tel;
      return this;
    }

    public Builder cardNum(String cardNum) {
      this.cardNum = cardNum;
      return this;
    }

    public Builder cardPass(String cardPass) {
      this.cardPass = cardPass;
      return this;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder openid(String openid) {
      this.openid = openid;
      return this;
    }

    @Override
    public RechargeReq build() {
      checkRequiredFields();
      return new RechargeReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}