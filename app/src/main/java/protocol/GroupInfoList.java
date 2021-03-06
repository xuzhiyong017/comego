// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

public final class GroupInfoList extends Message {

  public static final List<GroupInfo> DEFAULT_GROUPS = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED)
  public final List<GroupInfo> groups;

  private GroupInfoList(Builder builder) {
    this.groups = immutableCopyOf(builder.groups);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupInfoList)) return false;
    return equals(groups, ((GroupInfoList) other).groups);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = groups != null ? groups.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<GroupInfoList> {

    public List<GroupInfo> groups;

    public Builder() {
    }

    public Builder(GroupInfoList message) {
      super(message);
      if (message == null) return;
      this.groups = copyOf(message.groups);
    }

    public Builder groups(List<GroupInfo> groups) {
      this.groups = checkForNulls(groups);
      return this;
    }

    @Override
    public GroupInfoList build() {
      return new GroupInfoList(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
