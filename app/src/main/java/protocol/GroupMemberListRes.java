// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GroupMemberListRes extends Message {

  public static final Long DEFAULT_REVISION = 0L;
  public static final Long DEFAULT_GID = 0L;
  public static final List<GroupMemberIncrement> DEFAULT_INCREMENT = Collections.emptyList();
  public static final Long DEFAULT_REQREVISION = 0L;

  @ProtoField(tag = 1, type = UINT64)
  public final Long revision;

  @ProtoField(tag = 2, type = UINT64)
  public final Long gid;

  @ProtoField(tag = 3)
  public final GroupMemberList memberlist;

  /**
   * 不为空就重新同步
   */
  @ProtoField(tag = 4, label = REPEATED)
  public final List<GroupMemberIncrement> increment;

  @ProtoField(tag = 5, type = UINT64)
  public final Long reqrevision;

  private GroupMemberListRes(Builder builder) {
    this.revision = builder.revision;
    this.gid = builder.gid;
    this.memberlist = builder.memberlist;
    this.increment = immutableCopyOf(builder.increment);
    this.reqrevision = builder.reqrevision;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMemberListRes)) return false;
    GroupMemberListRes o = (GroupMemberListRes) other;
    return equals(revision, o.revision)
        && equals(gid, o.gid)
        && equals(memberlist, o.memberlist)
        && equals(increment, o.increment)
        && equals(reqrevision, o.reqrevision);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = revision != null ? revision.hashCode() : 0;
      result = result * 37 + (gid != null ? gid.hashCode() : 0);
      result = result * 37 + (memberlist != null ? memberlist.hashCode() : 0);
      result = result * 37 + (increment != null ? increment.hashCode() : 1);
      result = result * 37 + (reqrevision != null ? reqrevision.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupMemberListRes> {

    public Long revision;
    public Long gid;
    public GroupMemberList memberlist;
    public List<GroupMemberIncrement> increment;
    public Long reqrevision;

    public Builder() {
    }

    public Builder(GroupMemberListRes message) {
      super(message);
      if (message == null) return;
      this.revision = message.revision;
      this.gid = message.gid;
      this.memberlist = message.memberlist;
      this.increment = copyOf(message.increment);
      this.reqrevision = message.reqrevision;
    }

    public Builder revision(Long revision) {
      this.revision = revision;
      return this;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    public Builder memberlist(GroupMemberList memberlist) {
      this.memberlist = memberlist;
      return this;
    }

    /**
     * 不为空就重新同步
     */
    public Builder increment(List<GroupMemberIncrement> increment) {
      this.increment = checkForNulls(increment);
      return this;
    }

    public Builder reqrevision(Long reqrevision) {
      this.reqrevision = reqrevision;
      return this;
    }

    @Override
    public GroupMemberListRes build() {
      return new GroupMemberListRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}