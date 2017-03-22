// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;

public final class GroupListReq extends Message {

  public static final Long DEFAULT_REVISION = 0L;

  @ProtoField(tag = 1, type = UINT64)
  public final Long revision;

  private GroupListReq(Builder builder) {
    this.revision = builder.revision;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupListReq)) return false;
    return equals(revision, ((GroupListReq) other).revision);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = revision != null ? revision.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<GroupListReq> {

    public Long revision;

    public Builder() {
    }

    public Builder(GroupListReq message) {
      super(message);
      if (message == null) return;
      this.revision = message.revision;
    }

    public Builder revision(Long revision) {
      this.revision = revision;
      return this;
    }

    @Override
    public GroupListReq build() {
      return new GroupListReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}