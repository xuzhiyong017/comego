// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class YYBindRes extends Message {

  public static final Long DEFAULT_YYNO = 0L;
  public static final Integer DEFAULT_LEVEL = 0;

  @ProtoField(tag = 1, type = UINT64)
  public final Long yyno;

  /**
   * YY号
   */
  @ProtoField(tag = 2, type = UINT32)
  public final Integer level;

  private YYBindRes(Builder builder) {
    this.yyno = builder.yyno;
    this.level = builder.level;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof YYBindRes)) return false;
    YYBindRes o = (YYBindRes) other;
    return equals(yyno, o.yyno)
        && equals(level, o.level);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = yyno != null ? yyno.hashCode() : 0;
      result = result * 37 + (level != null ? level.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<YYBindRes> {

    public Long yyno;
    public Integer level;

    public Builder() {
    }

    public Builder(YYBindRes message) {
      super(message);
      if (message == null) return;
      this.yyno = message.yyno;
      this.level = message.level;
    }

    public Builder yyno(Long yyno) {
      this.yyno = yyno;
      return this;
    }

    /**
     * YY号
     */
    public Builder level(Integer level) {
      this.level = level;
      return this;
    }

    @Override
    public YYBindRes build() {
      return new YYBindRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}