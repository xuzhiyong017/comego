// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class AdModifyRes extends Message {

  private AdModifyRes(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof AdModifyRes;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<AdModifyRes> {

    public Builder() {
    }

    public Builder(AdModifyRes message) {
      super(message);
    }

    @Override
    public AdModifyRes build() {
      return new AdModifyRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}