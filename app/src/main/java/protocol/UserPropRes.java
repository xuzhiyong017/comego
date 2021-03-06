// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class UserPropRes extends Message {

  public static final Long DEFAULT_UID = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long uid;

  @ProtoField(tag = 2)
  public final UserProp prop;

  private UserPropRes(Builder builder) {
    this.uid = builder.uid;
    this.prop = builder.prop;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserPropRes)) return false;
    UserPropRes o = (UserPropRes) other;
    return equals(uid, o.uid)
        && equals(prop, o.prop);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (prop != null ? prop.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UserPropRes> {

    public Long uid;
    public UserProp prop;

    public Builder() {
    }

    public Builder(UserPropRes message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.prop = message.prop;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder prop(UserProp prop) {
      this.prop = prop;
      return this;
    }

    @Override
    public UserPropRes build() {
      checkRequiredFields();
      return new UserPropRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
