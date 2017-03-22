// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class GroupMemberKickReq extends Message {

  public static final Long DEFAULT_GID = 0L;
  public static final Long DEFAULT_UID = 0L;
  public static final Integer DEFAULT_SECONDS = 0;

  @ProtoField(tag = 1, type = UINT64)
  public final Long gid;

  @ProtoField(tag = 2, type = UINT64)
  public final Long uid;

  @ProtoField(tag = 3, type = UINT32)
  public final Integer seconds;

  private GroupMemberKickReq(Builder builder) {
    this.gid = builder.gid;
    this.uid = builder.uid;
    this.seconds = builder.seconds;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMemberKickReq)) return false;
    GroupMemberKickReq o = (GroupMemberKickReq) other;
    return equals(gid, o.gid)
        && equals(uid, o.uid)
        && equals(seconds, o.seconds);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = gid != null ? gid.hashCode() : 0;
      result = result * 37 + (uid != null ? uid.hashCode() : 0);
      result = result * 37 + (seconds != null ? seconds.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupMemberKickReq> {

    public Long gid;
    public Long uid;
    public Integer seconds;

    public Builder() {
    }

    public Builder(GroupMemberKickReq message) {
      super(message);
      if (message == null) return;
      this.gid = message.gid;
      this.uid = message.uid;
      this.seconds = message.seconds;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder seconds(Integer seconds) {
      this.seconds = seconds;
      return this;
    }

    @Override
    public GroupMemberKickReq build() {
      return new GroupMemberKickReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}