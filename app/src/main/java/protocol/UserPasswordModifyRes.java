// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class UserPasswordModifyRes extends Message {

  public static final String DEFAULT_COOKIE = "";

  @ProtoField(tag = 1, type = STRING)
  public final String cookie;

  private UserPasswordModifyRes(Builder builder) {
    this.cookie = builder.cookie;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserPasswordModifyRes)) return false;
    return equals(cookie, ((UserPasswordModifyRes) other).cookie);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = cookie != null ? cookie.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<UserPasswordModifyRes> {

    public String cookie;

    public Builder() {
    }

    public Builder(UserPasswordModifyRes message) {
      super(message);
      if (message == null) return;
      this.cookie = message.cookie;
    }

    public Builder cookie(String cookie) {
      this.cookie = cookie;
      return this;
    }

    @Override
    public UserPasswordModifyRes build() {
      return new UserPasswordModifyRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
