// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class Fan extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final Long DEFAULT_LOVEVAL = 0L;
  public static final Boolean DEFAULT_FOLLOW = false;
  public static final Boolean DEFAULT_FAN = false;
  public static final Boolean DEFAULT_SUBSCRIBE = false;

  @ProtoField(tag = 1, type = UINT64)
  public final Long uid;

  @ProtoField(tag = 2, type = UINT64)
  public final Long loveVal;

  @ProtoField(tag = 3, type = BOOL)
  public final Boolean follow;

  /**
   * 我关注的人
   */
  @ProtoField(tag = 4, type = BOOL)
  public final Boolean fan;

  /**
   * 我的粉丝
   */
  @ProtoField(tag = 9)
  public final UserInfo userInfo;

  @ProtoField(tag = 10, type = BOOL)
  public final Boolean Subscribe;

  private Fan(Builder builder) {
    this.uid = builder.uid;
    this.loveVal = builder.loveVal;
    this.follow = builder.follow;
    this.fan = builder.fan;
    this.userInfo = builder.userInfo;
    this.Subscribe = builder.Subscribe;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Fan)) return false;
    Fan o = (Fan) other;
    return equals(uid, o.uid)
        && equals(loveVal, o.loveVal)
        && equals(follow, o.follow)
        && equals(fan, o.fan)
        && equals(userInfo, o.userInfo)
        && equals(Subscribe, o.Subscribe);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (loveVal != null ? loveVal.hashCode() : 0);
      result = result * 37 + (follow != null ? follow.hashCode() : 0);
      result = result * 37 + (fan != null ? fan.hashCode() : 0);
      result = result * 37 + (userInfo != null ? userInfo.hashCode() : 0);
      result = result * 37 + (Subscribe != null ? Subscribe.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<Fan> {

    public Long uid;
    public Long loveVal;
    public Boolean follow;
    public Boolean fan;
    public UserInfo userInfo;
    public Boolean Subscribe;

    public Builder() {
    }

    public Builder(Fan message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.loveVal = message.loveVal;
      this.follow = message.follow;
      this.fan = message.fan;
      this.userInfo = message.userInfo;
      this.Subscribe = message.Subscribe;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder loveVal(Long loveVal) {
      this.loveVal = loveVal;
      return this;
    }

    public Builder follow(Boolean follow) {
      this.follow = follow;
      return this;
    }

    /**
     * 我关注的人
     */
    public Builder fan(Boolean fan) {
      this.fan = fan;
      return this;
    }

    /**
     * 我的粉丝
     */
    public Builder userInfo(UserInfo userInfo) {
      this.userInfo = userInfo;
      return this;
    }

    public Builder Subscribe(Boolean Subscribe) {
      this.Subscribe = Subscribe;
      return this;
    }

    @Override
    public Fan build() {
      return new Fan(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}