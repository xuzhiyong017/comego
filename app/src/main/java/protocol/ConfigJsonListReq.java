// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class ConfigJsonListReq extends Message {

  private ConfigJsonListReq(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof ConfigJsonListReq;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<ConfigJsonListReq> {

    public Builder() {
    }

    public Builder(ConfigJsonListReq message) {
      super(message);
    }

    @Override
    public ConfigJsonListReq build() {
      return new ConfigJsonListReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
