// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class UserInviteUserRes extends Message {

  public static final Integer DEFAULT_INDEX = 0;
  public static final Integer DEFAULT_FETCHS = 0;
  public static final List<UserInviteUser> DEFAULT_USERS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer index;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer fetchs;

  @ProtoField(tag = 3, label = REPEATED)
  public final List<UserInviteUser> users;

  private UserInviteUserRes(Builder builder) {
    this.index = builder.index;
    this.fetchs = builder.fetchs;
    this.users = immutableCopyOf(builder.users);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserInviteUserRes)) return false;
    UserInviteUserRes o = (UserInviteUserRes) other;
    return equals(index, o.index)
        && equals(fetchs, o.fetchs)
        && equals(users, o.users);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = index != null ? index.hashCode() : 0;
      result = result * 37 + (fetchs != null ? fetchs.hashCode() : 0);
      result = result * 37 + (users != null ? users.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UserInviteUserRes> {

    public Integer index;
    public Integer fetchs;
    public List<UserInviteUser> users;

    public Builder() {
    }

    public Builder(UserInviteUserRes message) {
      super(message);
      if (message == null) return;
      this.index = message.index;
      this.fetchs = message.fetchs;
      this.users = copyOf(message.users);
    }

    public Builder index(Integer index) {
      this.index = index;
      return this;
    }

    public Builder fetchs(Integer fetchs) {
      this.fetchs = fetchs;
      return this;
    }

    public Builder users(List<UserInviteUser> users) {
      this.users = checkForNulls(users);
      return this;
    }

    @Override
    public UserInviteUserRes build() {
      checkRequiredFields();
      return new UserInviteUserRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
