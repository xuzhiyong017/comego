// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;

/**
 * 分享成功后客户端上报
 */
public final class ReportShareReq extends Message {

  public static final Long DEFAULT_GID = 0L;

  @ProtoField(tag = 1, type = UINT64)
  public final Long gid;

  private ReportShareReq(Builder builder) {
    this.gid = builder.gid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ReportShareReq)) return false;
    return equals(gid, ((ReportShareReq) other).gid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = gid != null ? gid.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<ReportShareReq> {

    public Long gid;

    public Builder() {
    }

    public Builder(ReportShareReq message) {
      super(message);
      if (message == null) return;
      this.gid = message.gid;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    @Override
    public ReportShareReq build() {
      return new ReportShareReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
