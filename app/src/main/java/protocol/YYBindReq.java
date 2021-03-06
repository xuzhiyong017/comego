// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class YYBindReq extends Message {

  public static final Long DEFAULT_YYNO = 0L;
  public static final Integer DEFAULT_LEVEL = 0;
  public static final Long DEFAULT_UID = 0L;
  public static final Boolean DEFAULT_FORCE = false;

  @ProtoField(tag = 1, type = UINT64)
  public final Long yyno;

  /**
   * YY号,这里如果传入0，返回值会带是否已绑定
   */
  @ProtoField(tag = 2, type = UINT32)
  public final Integer level;

  @ProtoField(tag = 3, type = UINT64)
  public final Long uid;

  @ProtoField(tag = 4, type = BOOL)
  public final Boolean force;

  private YYBindReq(Builder builder) {
    this.yyno = builder.yyno;
    this.level = builder.level;
    this.uid = builder.uid;
    this.force = builder.force;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof YYBindReq)) return false;
    YYBindReq o = (YYBindReq) other;
    return equals(yyno, o.yyno)
        && equals(level, o.level)
        && equals(uid, o.uid)
        && equals(force, o.force);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = yyno != null ? yyno.hashCode() : 0;
      result = result * 37 + (level != null ? level.hashCode() : 0);
      result = result * 37 + (uid != null ? uid.hashCode() : 0);
      result = result * 37 + (force != null ? force.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<YYBindReq> {

    public Long yyno;
    public Integer level;
    public Long uid;
    public Boolean force;

    public Builder() {
    }

    public Builder(YYBindReq message) {
      super(message);
      if (message == null) return;
      this.yyno = message.yyno;
      this.level = message.level;
      this.uid = message.uid;
      this.force = message.force;
    }

    public Builder yyno(Long yyno) {
      this.yyno = yyno;
      return this;
    }

    /**
     * YY号,这里如果传入0，返回值会带是否已绑定
     */
    public Builder level(Integer level) {
      this.level = level;
      return this;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder force(Boolean force) {
      this.force = force;
      return this;
    }

    @Override
    public YYBindReq build() {
      return new YYBindReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
