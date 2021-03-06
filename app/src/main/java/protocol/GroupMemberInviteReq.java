// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;

/**
 * 粉丝邀请通知
 */
public final class GroupMemberInviteReq extends Message {

  public static final Long DEFAULT_GID = 0L;
  public static final List<Long> DEFAULT_UIDS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT64)
  public final Long gid;

  @ProtoField(tag = 2, type = UINT64, label = REPEATED)
  public final List<Long> uids;

  private GroupMemberInviteReq(Builder builder) {
    this.gid = builder.gid;
    this.uids = immutableCopyOf(builder.uids);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMemberInviteReq)) return false;
    GroupMemberInviteReq o = (GroupMemberInviteReq) other;
    return equals(gid, o.gid)
        && equals(uids, o.uids);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = gid != null ? gid.hashCode() : 0;
      result = result * 37 + (uids != null ? uids.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupMemberInviteReq> {

    public Long gid;
    public List<Long> uids;

    public Builder() {
    }

    public Builder(GroupMemberInviteReq message) {
      super(message);
      if (message == null) return;
      this.gid = message.gid;
      this.uids = copyOf(message.uids);
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    public Builder uids(List<Long> uids) {
      this.uids = checkForNulls(uids);
      return this;
    }

    @Override
    public GroupMemberInviteReq build() {
      return new GroupMemberInviteReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
