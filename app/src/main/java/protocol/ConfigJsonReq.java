// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class ConfigJsonReq extends Message {

  private ConfigJsonReq(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof ConfigJsonReq;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<ConfigJsonReq> {

    public Builder() {
    }

    public Builder(ConfigJsonReq message) {
      super(message);
    }

    @Override
    public ConfigJsonReq build() {
      return new ConfigJsonReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}