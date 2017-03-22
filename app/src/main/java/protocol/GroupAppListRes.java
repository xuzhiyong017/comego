// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

public final class GroupAppListRes extends Message {

  public static final List<GroupAppList> DEFAULT_APPLIST = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED)
  public final List<GroupAppList> applist;

  private GroupAppListRes(Builder builder) {
    this.applist = immutableCopyOf(builder.applist);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupAppListRes)) return false;
    return equals(applist, ((GroupAppListRes) other).applist);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = applist != null ? applist.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<GroupAppListRes> {

    public List<GroupAppList> applist;

    public Builder() {
    }

    public Builder(GroupAppListRes message) {
      super(message);
      if (message == null) return;
      this.applist = copyOf(message.applist);
    }

    public Builder applist(List<GroupAppList> applist) {
      this.applist = checkForNulls(applist);
      return this;
    }

    @Override
    public GroupAppListRes build() {
      return new GroupAppListRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
