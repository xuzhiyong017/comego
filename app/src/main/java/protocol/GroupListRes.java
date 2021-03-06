// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GroupListRes extends Message {

  public static final Long DEFAULT_REVISION = 0L;
  public static final List<GroupIncrement> DEFAULT_INCREMENTS = Collections.emptyList();
  public static final Long DEFAULT_REQREVISION = 0L;

  @ProtoField(tag = 1, type = UINT64)
  public final Long revision;

  @ProtoField(tag = 2)
  public final GroupInfoList grouplist;

  /**
   * 如果此项不为空，删除客户端Cache并从这里开始版本同步
   */
  @ProtoField(tag = 3, label = REPEATED)
  public final List<GroupIncrement> increments;

  @ProtoField(tag = 4, type = UINT64)
  public final Long reqrevision;

  private GroupListRes(Builder builder) {
    this.revision = builder.revision;
    this.grouplist = builder.grouplist;
    this.increments = immutableCopyOf(builder.increments);
    this.reqrevision = builder.reqrevision;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupListRes)) return false;
    GroupListRes o = (GroupListRes) other;
    return equals(revision, o.revision)
        && equals(grouplist, o.grouplist)
        && equals(increments, o.increments)
        && equals(reqrevision, o.reqrevision);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = revision != null ? revision.hashCode() : 0;
      result = result * 37 + (grouplist != null ? grouplist.hashCode() : 0);
      result = result * 37 + (increments != null ? increments.hashCode() : 1);
      result = result * 37 + (reqrevision != null ? reqrevision.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupListRes> {

    public Long revision;
    public GroupInfoList grouplist;
    public List<GroupIncrement> increments;
    public Long reqrevision;

    public Builder() {
    }

    public Builder(GroupListRes message) {
      super(message);
      if (message == null) return;
      this.revision = message.revision;
      this.grouplist = message.grouplist;
      this.increments = copyOf(message.increments);
      this.reqrevision = message.reqrevision;
    }

    public Builder revision(Long revision) {
      this.revision = revision;
      return this;
    }

    public Builder grouplist(GroupInfoList grouplist) {
      this.grouplist = grouplist;
      return this;
    }

    /**
     * 如果此项不为空，删除客户端Cache并从这里开始版本同步
     */
    public Builder increments(List<GroupIncrement> increments) {
      this.increments = checkForNulls(increments);
      return this;
    }

    public Builder reqrevision(Long reqrevision) {
      this.reqrevision = reqrevision;
      return this;
    }

    @Override
    public GroupListRes build() {
      return new GroupListRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
