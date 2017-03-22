// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class GroupMemberCountRes extends Message {

  public static final Long DEFAULT_GID = 0L;
  public static final GroupMemberCountType DEFAULT_COUNTTYPE = GroupMemberCountType.GroupMemberCountAllOnline;
  public static final Integer DEFAULT_MEMBERS = 0;
  public static final Integer DEFAULT_SEATPLAYERS = 0;

  @ProtoField(tag = 1, type = UINT64)
  public final Long gid;

  @ProtoField(tag = 2, type = ENUM)
  public final GroupMemberCountType countType;

  @ProtoField(tag = 3, type = UINT32)
  public final Integer members;

  @ProtoField(tag = 4, type = UINT32)
  public final Integer seatPlayers;

  private GroupMemberCountRes(Builder builder) {
    this.gid = builder.gid;
    this.countType = builder.countType;
    this.members = builder.members;
    this.seatPlayers = builder.seatPlayers;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMemberCountRes)) return false;
    GroupMemberCountRes o = (GroupMemberCountRes) other;
    return equals(gid, o.gid)
        && equals(countType, o.countType)
        && equals(members, o.members)
        && equals(seatPlayers, o.seatPlayers);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = gid != null ? gid.hashCode() : 0;
      result = result * 37 + (countType != null ? countType.hashCode() : 0);
      result = result * 37 + (members != null ? members.hashCode() : 0);
      result = result * 37 + (seatPlayers != null ? seatPlayers.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupMemberCountRes> {

    public Long gid;
    public GroupMemberCountType countType;
    public Integer members;
    public Integer seatPlayers;

    public Builder() {
    }

    public Builder(GroupMemberCountRes message) {
      super(message);
      if (message == null) return;
      this.gid = message.gid;
      this.countType = message.countType;
      this.members = message.members;
      this.seatPlayers = message.seatPlayers;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    public Builder countType(GroupMemberCountType countType) {
      this.countType = countType;
      return this;
    }

    public Builder members(Integer members) {
      this.members = members;
      return this;
    }

    public Builder seatPlayers(Integer seatPlayers) {
      this.seatPlayers = seatPlayers;
      return this;
    }

    @Override
    public GroupMemberCountRes build() {
      return new GroupMemberCountRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}