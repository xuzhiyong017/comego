// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

public final class TexasChipQueueRes extends Message {

  private TexasChipQueueRes(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof TexasChipQueueRes;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<TexasChipQueueRes> {

    public Builder() {
    }

    public Builder(TexasChipQueueRes message) {
      super(message);
    }

    @Override
    public TexasChipQueueRes build() {
      return new TexasChipQueueRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
