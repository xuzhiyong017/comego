// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Label.REQUIRED;

public final class GroupInfoModifyRes extends Message {

  @ProtoField(tag = 1, label = REQUIRED)
  public final GroupInfo groupInfo;

  private GroupInfoModifyRes(Builder builder) {
    this.groupInfo = builder.groupInfo;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupInfoModifyRes)) return false;
    return equals(groupInfo, ((GroupInfoModifyRes) other).groupInfo);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = groupInfo != null ? groupInfo.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<GroupInfoModifyRes> {

    public GroupInfo groupInfo;

    public Builder() {
    }

    public Builder(GroupInfoModifyRes message) {
      super(message);
      if (message == null) return;
      this.groupInfo = message.groupInfo;
    }

    public Builder groupInfo(GroupInfo groupInfo) {
      this.groupInfo = groupInfo;
      return this;
    }

    @Override
    public GroupInfoModifyRes build() {
      checkRequiredFields();
      return new GroupInfoModifyRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
