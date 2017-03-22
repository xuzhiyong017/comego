// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class UserInviteUserReq extends Message {

  public static final Integer DEFAULT_INDEX = 0;
  public static final Integer DEFAULT_FETCHS = 0;

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer index;

  @ProtoField(tag = 2, type = UINT32, label = REQUIRED)
  public final Integer fetchs;

  private UserInviteUserReq(Builder builder) {
    this.index = builder.index;
    this.fetchs = builder.fetchs;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserInviteUserReq)) return false;
    UserInviteUserReq o = (UserInviteUserReq) other;
    return equals(index, o.index)
        && equals(fetchs, o.fetchs);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = index != null ? index.hashCode() : 0;
      result = result * 37 + (fetchs != null ? fetchs.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UserInviteUserReq> {

    public Integer index;
    public Integer fetchs;

    public Builder() {
    }

    public Builder(UserInviteUserReq message) {
      super(message);
      if (message == null) return;
      this.index = message.index;
      this.fetchs = message.fetchs;
    }

    public Builder index(Integer index) {
      this.index = index;
      return this;
    }

    public Builder fetchs(Integer fetchs) {
      this.fetchs = fetchs;
      return this;
    }

    @Override
    public UserInviteUserReq build() {
      checkRequiredFields();
      return new UserInviteUserReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
