// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GiftStatListRes extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final Boolean DEFAULT_BYSEND = false;
  public static final Long DEFAULT_FROMDATE = 0L;
  public static final List<GiftStatItem> DEFAULT_ITEMS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT64)
  public final Long uid;

  @ProtoField(tag = 2, type = BOOL)
  public final Boolean bySend;

  /**
   * true送出的,false收到的
   */
  @ProtoField(tag = 3, type = INT64)
  public final Long fromDate;

  /**
   * 从哪一天开始统计,可以不填
   */
  @ProtoField(tag = 9, label = REPEATED)
  public final List<GiftStatItem> items;

  private GiftStatListRes(Builder builder) {
    this.uid = builder.uid;
    this.bySend = builder.bySend;
    this.fromDate = builder.fromDate;
    this.items = immutableCopyOf(builder.items);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GiftStatListRes)) return false;
    GiftStatListRes o = (GiftStatListRes) other;
    return equals(uid, o.uid)
        && equals(bySend, o.bySend)
        && equals(fromDate, o.fromDate)
        && equals(items, o.items);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (bySend != null ? bySend.hashCode() : 0);
      result = result * 37 + (fromDate != null ? fromDate.hashCode() : 0);
      result = result * 37 + (items != null ? items.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GiftStatListRes> {

    public Long uid;
    public Boolean bySend;
    public Long fromDate;
    public List<GiftStatItem> items;

    public Builder() {
    }

    public Builder(GiftStatListRes message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.bySend = message.bySend;
      this.fromDate = message.fromDate;
      this.items = copyOf(message.items);
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder bySend(Boolean bySend) {
      this.bySend = bySend;
      return this;
    }

    /**
     * true送出的,false收到的
     */
    public Builder fromDate(Long fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    /**
     * 从哪一天开始统计,可以不填
     */
    public Builder items(List<GiftStatItem> items) {
      this.items = checkForNulls(items);
      return this;
    }

    @Override
    public GiftStatListRes build() {
      return new GiftStatListRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
