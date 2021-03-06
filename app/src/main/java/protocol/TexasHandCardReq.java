// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;

/**
 * 请求查看手牌
 */
public final class TexasHandCardReq extends Message {

  public static final Integer DEFAULT_REQ = 0;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer req;

  private TexasHandCardReq(Builder builder) {
    this.req = builder.req;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasHandCardReq)) return false;
    return equals(req, ((TexasHandCardReq) other).req);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = req != null ? req.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<TexasHandCardReq> {

    public Integer req;

    public Builder() {
    }

    public Builder(TexasHandCardReq message) {
      super(message);
      if (message == null) return;
      this.req = message.req;
    }

    public Builder req(Integer req) {
      this.req = req;
      return this;
    }

    @Override
    public TexasHandCardReq build() {
      return new TexasHandCardReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
