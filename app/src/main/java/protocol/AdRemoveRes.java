// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class AdRemoveRes extends Message {

  private AdRemoveRes(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof AdRemoveRes;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<AdRemoveRes> {

    public Builder() {
    }

    public Builder(AdRemoveRes message) {
      super(message);
    }

    @Override
    public AdRemoveRes build() {
      return new AdRemoveRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}