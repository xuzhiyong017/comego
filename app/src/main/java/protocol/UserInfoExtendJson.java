// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

/**
 * UserInfo.extendJson格式
 */
public final class UserInfoExtendJson extends Message {

  public static final String DEFAULT_GROUPURL = "";

  @ProtoField(tag = 1, type = STRING)
  public final String groupUrl;

  private UserInfoExtendJson(Builder builder) {
    this.groupUrl = builder.groupUrl;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserInfoExtendJson)) return false;
    return equals(groupUrl, ((UserInfoExtendJson) other).groupUrl);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = groupUrl != null ? groupUrl.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<UserInfoExtendJson> {

    public String groupUrl;

    public Builder() {
    }

    public Builder(UserInfoExtendJson message) {
      super(message);
      if (message == null) return;
      this.groupUrl = message.groupUrl;
    }

    public Builder groupUrl(String groupUrl) {
      this.groupUrl = groupUrl;
      return this;
    }

    @Override
    public UserInfoExtendJson build() {
      return new UserInfoExtendJson(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}