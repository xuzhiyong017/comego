// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class TexasStartRes extends Message {

  private TexasStartRes(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof TexasStartRes;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<TexasStartRes> {

    public Builder() {
    }

    public Builder(TexasStartRes message) {
      super(message);
    }

    @Override
    public TexasStartRes build() {
      return new TexasStartRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}