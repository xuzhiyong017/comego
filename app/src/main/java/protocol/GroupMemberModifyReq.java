// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;

/**
 * 群组成员属性修改：
 */
public final class GroupMemberModifyReq extends Message {

  public static final Long DEFAULT_GID = 0L;

  @ProtoField(tag = 1, type = UINT64)
  public final Long gid;

  @ProtoField(tag = 2)
  public final GroupMember member;

  private GroupMemberModifyReq(Builder builder) {
    this.gid = builder.gid;
    this.member = builder.member;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMemberModifyReq)) return false;
    GroupMemberModifyReq o = (GroupMemberModifyReq) other;
    return equals(gid, o.gid)
        && equals(member, o.member);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = gid != null ? gid.hashCode() : 0;
      result = result * 37 + (member != null ? member.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupMemberModifyReq> {

    public Long gid;
    public GroupMember member;

    public Builder() {
    }

    public Builder(GroupMemberModifyReq message) {
      super(message);
      if (message == null) return;
      this.gid = message.gid;
      this.member = message.member;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    public Builder member(GroupMember member) {
      this.member = member;
      return this;
    }

    @Override
    public GroupMemberModifyReq build() {
      return new GroupMemberModifyReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}