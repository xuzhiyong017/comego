// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class TexasQueueQuitRes extends Message {

  private TexasQueueQuitRes(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof TexasQueueQuitRes;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<TexasQueueQuitRes> {

    public Builder() {
    }

    public Builder(TexasQueueQuitRes message) {
      super(message);
    }

    @Override
    public TexasQueueQuitRes build() {
      return new TexasQueueQuitRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
