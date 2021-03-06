// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class GroupMapRes extends Message {

  public static final String DEFAULT_PASSWORD = "";

  @ProtoField(tag = 1, type = STRING)
  public final String password;

  @ProtoField(tag = 2)
  public final GroupInfo groupInfo;

  private GroupMapRes(Builder builder) {
    this.password = builder.password;
    this.groupInfo = builder.groupInfo;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMapRes)) return false;
    GroupMapRes o = (GroupMapRes) other;
    return equals(password, o.password)
        && equals(groupInfo, o.groupInfo);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = password != null ? password.hashCode() : 0;
      result = result * 37 + (groupInfo != null ? groupInfo.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupMapRes> {

    public String password;
    public GroupInfo groupInfo;

    public Builder() {
    }

    public Builder(GroupMapRes message) {
      super(message);
      if (message == null) return;
      this.password = message.password;
      this.groupInfo = message.groupInfo;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder groupInfo(GroupInfo groupInfo) {
      this.groupInfo = groupInfo;
      return this;
    }

    @Override
    public GroupMapRes build() {
      return new GroupMapRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
