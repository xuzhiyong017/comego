// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class PageDataExchangeReq extends Message {

  public static final String DEFAULT_KEY = "";

  @ProtoField(tag = 1, type = STRING)
  public final String key;

  private PageDataExchangeReq(Builder builder) {
    this.key = builder.key;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PageDataExchangeReq)) return false;
    return equals(key, ((PageDataExchangeReq) other).key);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = key != null ? key.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<PageDataExchangeReq> {

    public String key;

    public Builder() {
    }

    public Builder(PageDataExchangeReq message) {
      super(message);
      if (message == null) return;
      this.key = message.key;
    }

    public Builder key(String key) {
      this.key = key;
      return this;
    }

    @Override
    public PageDataExchangeReq build() {
      return new PageDataExchangeReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}