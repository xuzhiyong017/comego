// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Label.REQUIRED;

public final class UserInfoRes extends Message {

  @ProtoField(tag = 1, label = REQUIRED)
  public final UserInfo userinfo;

  private UserInfoRes(Builder builder) {
    this.userinfo = builder.userinfo;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserInfoRes)) return false;
    return equals(userinfo, ((UserInfoRes) other).userinfo);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = userinfo != null ? userinfo.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<UserInfoRes> {

    public UserInfo userinfo;

    public Builder() {
    }

    public Builder(UserInfoRes message) {
      super(message);
      if (message == null) return;
      this.userinfo = message.userinfo;
    }

    public Builder userinfo(UserInfo userinfo) {
      this.userinfo = userinfo;
      return this;
    }

    @Override
    public UserInfoRes build() {
      checkRequiredFields();
      return new UserInfoRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
