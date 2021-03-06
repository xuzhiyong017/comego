// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 购买筹码
 */
public final class BuyMChipReq extends Message {

  public static final Integer DEFAULT_ITEMID = 0;

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer itemId;

  private BuyMChipReq(Builder builder) {
    this.itemId = builder.itemId;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof BuyMChipReq)) return false;
    return equals(itemId, ((BuyMChipReq) other).itemId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = itemId != null ? itemId.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<BuyMChipReq> {

    public Integer itemId;

    public Builder() {
    }

    public Builder(BuyMChipReq message) {
      super(message);
      if (message == null) return;
      this.itemId = message.itemId;
    }

    public Builder itemId(Integer itemId) {
      this.itemId = itemId;
      return this;
    }

    @Override
    public BuyMChipReq build() {
      checkRequiredFields();
      return new BuyMChipReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
