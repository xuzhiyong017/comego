// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;

/**
 * 补充筹码
 */
public final class TexasAddChipReq extends Message {

  private TexasAddChipReq(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof TexasAddChipReq;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<TexasAddChipReq> {

    public Builder() {
    }

    public Builder(TexasAddChipReq message) {
      super(message);
    }

    @Override
    public TexasAddChipReq build() {
      return new TexasAddChipReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
