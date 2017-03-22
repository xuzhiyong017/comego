// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 充值记录
 */
public final class RechargesReq extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final String DEFAULT_FROMTIME = "";
  public static final String DEFAULT_TOTIME = "";

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long uid;

  @ProtoField(tag = 2, type = STRING, label = REQUIRED)
  public final String fromTime;

  @ProtoField(tag = 3, type = STRING, label = REQUIRED)
  public final String toTime;

  private RechargesReq(Builder builder) {
    this.uid = builder.uid;
    this.fromTime = builder.fromTime;
    this.toTime = builder.toTime;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RechargesReq)) return false;
    RechargesReq o = (RechargesReq) other;
    return equals(uid, o.uid)
        && equals(fromTime, o.fromTime)
        && equals(toTime, o.toTime);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (fromTime != null ? fromTime.hashCode() : 0);
      result = result * 37 + (toTime != null ? toTime.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<RechargesReq> {

    public Long uid;
    public String fromTime;
    public String toTime;

    public Builder() {
    }

    public Builder(RechargesReq message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.fromTime = message.fromTime;
      this.toTime = message.toTime;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder fromTime(String fromTime) {
      this.fromTime = fromTime;
      return this;
    }

    public Builder toTime(String toTime) {
      this.toTime = toTime;
      return this;
    }

    @Override
    public RechargesReq build() {
      checkRequiredFields();
      return new RechargesReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}