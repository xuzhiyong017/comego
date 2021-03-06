// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class GroupMemberCountReq extends Message {

  public static final Long DEFAULT_GID = 0L;
  public static final GroupMemberCountType DEFAULT_COUNTTYPE = GroupMemberCountType.GroupMemberCountAllOnline;
  public static final Integer DEFAULT_NEEDSEATPLAYER = 0;

  @ProtoField(tag = 1, type = UINT64)
  public final Long gid;

  @ProtoField(tag = 2, type = ENUM)
  public final GroupMemberCountType countType;

  @ProtoField(tag = 3, type = UINT32)
  public final Integer needSeatPlayer;

  private GroupMemberCountReq(Builder builder) {
    this.gid = builder.gid;
    this.countType = builder.countType;
    this.needSeatPlayer = builder.needSeatPlayer;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMemberCountReq)) return false;
    GroupMemberCountReq o = (GroupMemberCountReq) other;
    return equals(gid, o.gid)
        && equals(countType, o.countType)
        && equals(needSeatPlayer, o.needSeatPlayer);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = gid != null ? gid.hashCode() : 0;
      result = result * 37 + (countType != null ? countType.hashCode() : 0);
      result = result * 37 + (needSeatPlayer != null ? needSeatPlayer.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupMemberCountReq> {

    public Long gid;
    public GroupMemberCountType countType;
    public Integer needSeatPlayer;

    public Builder() {
    }

    public Builder(GroupMemberCountReq message) {
      super(message);
      if (message == null) return;
      this.gid = message.gid;
      this.countType = message.countType;
      this.needSeatPlayer = message.needSeatPlayer;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    public Builder countType(GroupMemberCountType countType) {
      this.countType = countType;
      return this;
    }

    public Builder needSeatPlayer(Integer needSeatPlayer) {
      this.needSeatPlayer = needSeatPlayer;
      return this;
    }

    @Override
    public GroupMemberCountReq build() {
      return new GroupMemberCountReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
