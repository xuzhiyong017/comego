// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class UserProp extends Message {

  public static final Long DEFAULT_LOVEVAL = 0L;
  public static final Integer DEFAULT_FOLLOWS = 0;
  public static final Integer DEFAULT_FANS = 0;

  @ProtoField(tag = 1, type = UINT64)
  public final Long LoveVal;

  /**
   * 粉丝亲密度
   */
  @ProtoField(tag = 2, type = UINT32)
  public final Integer follows;

  /**
   * 关注数
   */
  @ProtoField(tag = 3, type = UINT32)
  public final Integer fans;

  private UserProp(Builder builder) {
    this.LoveVal = builder.LoveVal;
    this.follows = builder.follows;
    this.fans = builder.fans;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserProp)) return false;
    UserProp o = (UserProp) other;
    return equals(LoveVal, o.LoveVal)
        && equals(follows, o.follows)
        && equals(fans, o.fans);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = LoveVal != null ? LoveVal.hashCode() : 0;
      result = result * 37 + (follows != null ? follows.hashCode() : 0);
      result = result * 37 + (fans != null ? fans.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UserProp> {

    public Long LoveVal;
    public Integer follows;
    public Integer fans;

    public Builder() {
    }

    public Builder(UserProp message) {
      super(message);
      if (message == null) return;
      this.LoveVal = message.LoveVal;
      this.follows = message.follows;
      this.fans = message.fans;
    }

    public Builder LoveVal(Long LoveVal) {
      this.LoveVal = LoveVal;
      return this;
    }

    /**
     * 粉丝亲密度
     */
    public Builder follows(Integer follows) {
      this.follows = follows;
      return this;
    }

    /**
     * 关注数
     */
    public Builder fans(Integer fans) {
      this.fans = fans;
      return this;
    }

    @Override
    public UserProp build() {
      return new UserProp(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
