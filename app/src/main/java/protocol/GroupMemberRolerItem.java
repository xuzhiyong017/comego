// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

/**
 * 同步群组成员列表：
 */
public final class GroupMemberRolerItem extends Message {

  public static final Integer DEFAULT_ROLER = 0;
  public static final Integer DEFAULT_STATE = 0;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer roler;

  @ProtoField(tag = 2, type = UINT32)
  public final Integer state;

  private GroupMemberRolerItem(Builder builder) {
    this.roler = builder.roler;
    this.state = builder.state;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMemberRolerItem)) return false;
    GroupMemberRolerItem o = (GroupMemberRolerItem) other;
    return equals(roler, o.roler)
        && equals(state, o.state);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = roler != null ? roler.hashCode() : 0;
      result = result * 37 + (state != null ? state.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupMemberRolerItem> {

    public Integer roler;
    public Integer state;

    public Builder() {
    }

    public Builder(GroupMemberRolerItem message) {
      super(message);
      if (message == null) return;
      this.roler = message.roler;
      this.state = message.state;
    }

    public Builder roler(Integer roler) {
      this.roler = roler;
      return this;
    }

    public Builder state(Integer state) {
      this.state = state;
      return this;
    }

    @Override
    public GroupMemberRolerItem build() {
      return new GroupMemberRolerItem(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
