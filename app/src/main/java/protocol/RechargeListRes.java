// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class RechargeListRes extends Message {

  public static final Integer DEFAULT_INDEX = 0;
  public static final Integer DEFAULT_TOTAL = 0;
  public static final List<RechargeSelection> DEFAULT_SELECTIONS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer index;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer total;

  @ProtoField(tag = 3, label = REPEATED)
  public final List<RechargeSelection> selections;

  private RechargeListRes(Builder builder) {
    this.index = builder.index;
    this.total = builder.total;
    this.selections = immutableCopyOf(builder.selections);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RechargeListRes)) return false;
    RechargeListRes o = (RechargeListRes) other;
    return equals(index, o.index)
        && equals(total, o.total)
        && equals(selections, o.selections);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = index != null ? index.hashCode() : 0;
      result = result * 37 + (total != null ? total.hashCode() : 0);
      result = result * 37 + (selections != null ? selections.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<RechargeListRes> {

    public Integer index;
    public Integer total;
    public List<RechargeSelection> selections;

    public Builder() {
    }

    public Builder(RechargeListRes message) {
      super(message);
      if (message == null) return;
      this.index = message.index;
      this.total = message.total;
      this.selections = copyOf(message.selections);
    }

    public Builder index(Integer index) {
      this.index = index;
      return this;
    }

    public Builder total(Integer total) {
      this.total = total;
      return this;
    }

    public Builder selections(List<RechargeSelection> selections) {
      this.selections = checkForNulls(selections);
      return this;
    }

    @Override
    public RechargeListRes build() {
      checkRequiredFields();
      return new RechargeListRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}