// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class BuyMChipRes extends Message {

  public static final Integer DEFAULT_ITEMID = 0;
  public static final Long DEFAULT_MCHIP = 0L;
  public static final Long DEFAULT_MGOLD = 0L;

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer itemId;

  @ProtoField(tag = 2, type = UINT64, label = REQUIRED)
  public final Long MChip;

  /**
   * 购买成功后筹码数
   */
  @ProtoField(tag = 3, type = UINT64, label = REQUIRED)
  public final Long MGold;

  private BuyMChipRes(Builder builder) {
    this.itemId = builder.itemId;
    this.MChip = builder.MChip;
    this.MGold = builder.MGold;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof BuyMChipRes)) return false;
    BuyMChipRes o = (BuyMChipRes) other;
    return equals(itemId, o.itemId)
        && equals(MChip, o.MChip)
        && equals(MGold, o.MGold);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = itemId != null ? itemId.hashCode() : 0;
      result = result * 37 + (MChip != null ? MChip.hashCode() : 0);
      result = result * 37 + (MGold != null ? MGold.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<BuyMChipRes> {

    public Integer itemId;
    public Long MChip;
    public Long MGold;

    public Builder() {
    }

    public Builder(BuyMChipRes message) {
      super(message);
      if (message == null) return;
      this.itemId = message.itemId;
      this.MChip = message.MChip;
      this.MGold = message.MGold;
    }

    public Builder itemId(Integer itemId) {
      this.itemId = itemId;
      return this;
    }

    public Builder MChip(Long MChip) {
      this.MChip = MChip;
      return this;
    }

    /**
     * 购买成功后筹码数
     */
    public Builder MGold(Long MGold) {
      this.MGold = MGold;
      return this;
    }

    @Override
    public BuyMChipRes build() {
      checkRequiredFields();
      return new BuyMChipRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}