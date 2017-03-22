// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class UserActivateRes extends Message {

  public static final String DEFAULT_COOKIE = "";

  @ProtoField(tag = 1, type = STRING)
  public final String cookie;

  @ProtoField(tag = 5)
  public final UserInfo userInfo;

  private UserActivateRes(Builder builder) {
    this.cookie = builder.cookie;
    this.userInfo = builder.userInfo;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserActivateRes)) return false;
    UserActivateRes o = (UserActivateRes) other;
    return equals(cookie, o.cookie)
        && equals(userInfo, o.userInfo);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = cookie != null ? cookie.hashCode() : 0;
      result = result * 37 + (userInfo != null ? userInfo.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UserActivateRes> {

    public String cookie;
    public UserInfo userInfo;

    public Builder() {
    }

    public Builder(UserActivateRes message) {
      super(message);
      if (message == null) return;
      this.cookie = message.cookie;
      this.userInfo = message.userInfo;
    }

    public Builder cookie(String cookie) {
      this.cookie = cookie;
      return this;
    }

    public Builder userInfo(UserInfo userInfo) {
      this.userInfo = userInfo;
      return this;
    }

    @Override
    public UserActivateRes build() {
      return new UserActivateRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}