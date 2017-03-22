// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class GroupAppReq extends Message {

  public static final Long DEFAULT_GID = 0L;
  public static final Long DEFAULT_REVISION = 0L;
  public static final Integer DEFAULT_FETCHS = 0;
  public static final Integer DEFAULT_TOPN = 0;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long gid;

  @ProtoField(tag = 2, type = UINT64)
  public final Long revision;

  @ProtoField(tag = 3, type = UINT32)
  public final Integer fetchs;

  /**
   * 这个参数如果不为0，从指定版本读取fetchs个结果返回
   */
  @ProtoField(tag = 4, type = UINT32)
  public final Integer topn;

  private GroupAppReq(Builder builder) {
    this.gid = builder.gid;
    this.revision = builder.revision;
    this.fetchs = builder.fetchs;
    this.topn = builder.topn;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupAppReq)) return false;
    GroupAppReq o = (GroupAppReq) other;
    return equals(gid, o.gid)
        && equals(revision, o.revision)
        && equals(fetchs, o.fetchs)
        && equals(topn, o.topn);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = gid != null ? gid.hashCode() : 0;
      result = result * 37 + (revision != null ? revision.hashCode() : 0);
      result = result * 37 + (fetchs != null ? fetchs.hashCode() : 0);
      result = result * 37 + (topn != null ? topn.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupAppReq> {

    public Long gid;
    public Long revision;
    public Integer fetchs;
    public Integer topn;

    public Builder() {
    }

    public Builder(GroupAppReq message) {
      super(message);
      if (message == null) return;
      this.gid = message.gid;
      this.revision = message.revision;
      this.fetchs = message.fetchs;
      this.topn = message.topn;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    public Builder revision(Long revision) {
      this.revision = revision;
      return this;
    }

    public Builder fetchs(Integer fetchs) {
      this.fetchs = fetchs;
      return this;
    }

    /**
     * 这个参数如果不为0，从指定版本读取fetchs个结果返回
     */
    public Builder topn(Integer topn) {
      this.topn = topn;
      return this;
    }

    @Override
    public GroupAppReq build() {
      checkRequiredFields();
      return new GroupAppReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
