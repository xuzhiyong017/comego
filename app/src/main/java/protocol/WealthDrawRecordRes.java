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

public final class WealthDrawRecordRes extends Message {

  public static final Integer DEFAULT_INDEX = 0;
  public static final Integer DEFAULT_FETCHS = 0;
  public static final List<WealthDrawRecord> DEFAULT_RECORDS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer index;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer fetchs;

  @ProtoField(tag = 3, label = REPEATED)
  public final List<WealthDrawRecord> records;

  private WealthDrawRecordRes(Builder builder) {
    this.index = builder.index;
    this.fetchs = builder.fetchs;
    this.records = immutableCopyOf(builder.records);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof WealthDrawRecordRes)) return false;
    WealthDrawRecordRes o = (WealthDrawRecordRes) other;
    return equals(index, o.index)
        && equals(fetchs, o.fetchs)
        && equals(records, o.records);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = index != null ? index.hashCode() : 0;
      result = result * 37 + (fetchs != null ? fetchs.hashCode() : 0);
      result = result * 37 + (records != null ? records.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<WealthDrawRecordRes> {

    public Integer index;
    public Integer fetchs;
    public List<WealthDrawRecord> records;

    public Builder() {
    }

    public Builder(WealthDrawRecordRes message) {
      super(message);
      if (message == null) return;
      this.index = message.index;
      this.fetchs = message.fetchs;
      this.records = copyOf(message.records);
    }

    public Builder index(Integer index) {
      this.index = index;
      return this;
    }

    public Builder fetchs(Integer fetchs) {
      this.fetchs = fetchs;
      return this;
    }

    public Builder records(List<WealthDrawRecord> records) {
      this.records = checkForNulls(records);
      return this;
    }

    @Override
    public WealthDrawRecordRes build() {
      checkRequiredFields();
      return new WealthDrawRecordRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
