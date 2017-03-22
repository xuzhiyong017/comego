// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class ChangeWealthRecRes extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final Integer DEFAULT_CHANGETYPE = 0;
  public static final List<ChangeWealthRec> DEFAULT_RECORDS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long uid;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer changeType;

  @ProtoField(tag = 4, label = REPEATED)
  public final List<ChangeWealthRec> records;

  private ChangeWealthRecRes(Builder builder) {
    this.uid = builder.uid;
    this.changeType = builder.changeType;
    this.records = immutableCopyOf(builder.records);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ChangeWealthRecRes)) return false;
    ChangeWealthRecRes o = (ChangeWealthRecRes) other;
    return equals(uid, o.uid)
        && equals(changeType, o.changeType)
        && equals(records, o.records);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (changeType != null ? changeType.hashCode() : 0);
      result = result * 37 + (records != null ? records.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<ChangeWealthRecRes> {

    public Long uid;
    public Integer changeType;
    public List<ChangeWealthRec> records;

    public Builder() {
    }

    public Builder(ChangeWealthRecRes message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.changeType = message.changeType;
      this.records = copyOf(message.records);
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder changeType(Integer changeType) {
      this.changeType = changeType;
      return this;
    }

    public Builder records(List<ChangeWealthRec> records) {
      this.records = checkForNulls(records);
      return this;
    }

    @Override
    public ChangeWealthRecRes build() {
      checkRequiredFields();
      return new ChangeWealthRecRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}