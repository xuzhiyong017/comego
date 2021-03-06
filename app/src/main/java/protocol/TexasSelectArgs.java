// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class TexasSelectArgs extends Message {

  public static final TexasSelect DEFAULT_SEL = TexasSelect.SELECT_FLOP;
  public static final Long DEFAULT_ARGS = 0L;

  @ProtoField(tag = 1, type = ENUM, label = REQUIRED)
  public final TexasSelect sel;

  /**
   * 选项
   */
  @ProtoField(tag = 2, type = UINT64, label = REQUIRED)
  public final Long args;

  private TexasSelectArgs(Builder builder) {
    this.sel = builder.sel;
    this.args = builder.args;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasSelectArgs)) return false;
    TexasSelectArgs o = (TexasSelectArgs) other;
    return equals(sel, o.sel)
        && equals(args, o.args);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = sel != null ? sel.hashCode() : 0;
      result = result * 37 + (args != null ? args.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasSelectArgs> {

    public TexasSelect sel;
    public Long args;

    public Builder() {
    }

    public Builder(TexasSelectArgs message) {
      super(message);
      if (message == null) return;
      this.sel = message.sel;
      this.args = message.args;
    }

    public Builder sel(TexasSelect sel) {
      this.sel = sel;
      return this;
    }

    /**
     * 选项
     */
    public Builder args(Long args) {
      this.args = args;
      return this;
    }

    @Override
    public TexasSelectArgs build() {
      checkRequiredFields();
      return new TexasSelectArgs(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
