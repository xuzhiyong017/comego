// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REPEATED;

/**
 * 踢用户广播
 */
public final class ForceLogoutPush extends Message {

  public static final List<String> DEFAULT_WHITEMAC = Collections.emptyList();
  public static final List<String> DEFAULT_BLACKMAC = Collections.emptyList();
  public static final String DEFAULT_REASON = "";

  @ProtoField(tag = 1, type = STRING, label = REPEATED)
  public final List<String> whitemac;

  /**
   * 在这个表里面的MAC不用退出
   */
  @ProtoField(tag = 2, type = STRING, label = REPEATED)
  public final List<String> blackmac;

  /**
   * 在这个表里面的MAC退出
   */
  @ProtoField(tag = 3, type = STRING)
  public final String reason;

  private ForceLogoutPush(Builder builder) {
    this.whitemac = immutableCopyOf(builder.whitemac);
    this.blackmac = immutableCopyOf(builder.blackmac);
    this.reason = builder.reason;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ForceLogoutPush)) return false;
    ForceLogoutPush o = (ForceLogoutPush) other;
    return equals(whitemac, o.whitemac)
        && equals(blackmac, o.blackmac)
        && equals(reason, o.reason);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = whitemac != null ? whitemac.hashCode() : 1;
      result = result * 37 + (blackmac != null ? blackmac.hashCode() : 1);
      result = result * 37 + (reason != null ? reason.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<ForceLogoutPush> {

    public List<String> whitemac;
    public List<String> blackmac;
    public String reason;

    public Builder() {
    }

    public Builder(ForceLogoutPush message) {
      super(message);
      if (message == null) return;
      this.whitemac = copyOf(message.whitemac);
      this.blackmac = copyOf(message.blackmac);
      this.reason = message.reason;
    }

    public Builder whitemac(List<String> whitemac) {
      this.whitemac = checkForNulls(whitemac);
      return this;
    }

    /**
     * 在这个表里面的MAC不用退出
     */
    public Builder blackmac(List<String> blackmac) {
      this.blackmac = checkForNulls(blackmac);
      return this;
    }

    /**
     * 在这个表里面的MAC退出
     */
    public Builder reason(String reason) {
      this.reason = reason;
      return this;
    }

    @Override
    public ForceLogoutPush build() {
      return new ForceLogoutPush(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
