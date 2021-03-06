// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class Result extends Message {

  public static final Boolean DEFAULT_SUCCESS = false;
  public static final ErrCode DEFAULT_CODE = ErrCode.Success;
  public static final String DEFAULT_REMARKS = "";
  public static final Long DEFAULT_UID = 0L;
  public static final Long DEFAULT_GID = 0L;
  public static final Long DEFAULT_REVISION = 0L;
  public static final String DEFAULT_REASON = "";

  @ProtoField(tag = 1, type = BOOL, label = REQUIRED)
  public final Boolean success;

  @ProtoField(tag = 2, type = ENUM)
  public final ErrCode code;

  @ProtoField(tag = 3, type = STRING)
  public final String remarks;

  @ProtoField(tag = 4, type = UINT64)
  public final Long uid;

  @ProtoField(tag = 5, type = UINT64)
  public final Long gid;

  @ProtoField(tag = 6, type = UINT64)
  public final Long revision;

  @ProtoField(tag = 7, type = STRING)
  public final String reason;

  private Result(Builder builder) {
    this.success = builder.success;
    this.code = builder.code;
    this.remarks = builder.remarks;
    this.uid = builder.uid;
    this.gid = builder.gid;
    this.revision = builder.revision;
    this.reason = builder.reason;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Result)) return false;
    Result o = (Result) other;
    return equals(success, o.success)
        && equals(code, o.code)
        && equals(remarks, o.remarks)
        && equals(uid, o.uid)
        && equals(gid, o.gid)
        && equals(revision, o.revision)
        && equals(reason, o.reason);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = success != null ? success.hashCode() : 0;
      result = result * 37 + (code != null ? code.hashCode() : 0);
      result = result * 37 + (remarks != null ? remarks.hashCode() : 0);
      result = result * 37 + (uid != null ? uid.hashCode() : 0);
      result = result * 37 + (gid != null ? gid.hashCode() : 0);
      result = result * 37 + (revision != null ? revision.hashCode() : 0);
      result = result * 37 + (reason != null ? reason.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<Result> {

    public Boolean success;
    public ErrCode code;
    public String remarks;
    public Long uid;
    public Long gid;
    public Long revision;
    public String reason;

    public Builder() {
    }

    public Builder(Result message) {
      super(message);
      if (message == null) return;
      this.success = message.success;
      this.code = message.code;
      this.remarks = message.remarks;
      this.uid = message.uid;
      this.gid = message.gid;
      this.revision = message.revision;
      this.reason = message.reason;
    }

    public Builder success(Boolean success) {
      this.success = success;
      return this;
    }

    public Builder code(ErrCode code) {
      this.code = code;
      return this;
    }

    public Builder remarks(String remarks) {
      this.remarks = remarks;
      return this;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    public Builder revision(Long revision) {
      this.revision = revision;
      return this;
    }

    public Builder reason(String reason) {
      this.reason = reason;
      return this;
    }

    @Override
    public Result build() {
      checkRequiredFields();
      return new Result(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
