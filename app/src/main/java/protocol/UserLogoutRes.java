// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class UserLogoutRes extends Message {

  private UserLogoutRes(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof UserLogoutRes;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<UserLogoutRes> {

    public Builder() {
    }

    public Builder(UserLogoutRes message) {
      super(message);
    }

    @Override
    public UserLogoutRes build() {
      return new UserLogoutRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
