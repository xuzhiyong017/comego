// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Label.REPEATED;

public final class WealthDrawCorpListRes extends Message {

  public static final Boolean DEFAULT_CORP = false;
  public static final List<WealthDrawCorpItems> DEFAULT_ITEMS = Collections.emptyList();

  @ProtoField(tag = 1, type = BOOL)
  public final Boolean corp;

  @ProtoField(tag = 2, label = REPEATED)
  public final List<WealthDrawCorpItems> items;

  private WealthDrawCorpListRes(Builder builder) {
    this.corp = builder.corp;
    this.items = immutableCopyOf(builder.items);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof WealthDrawCorpListRes)) return false;
    WealthDrawCorpListRes o = (WealthDrawCorpListRes) other;
    return equals(corp, o.corp)
        && equals(items, o.items);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = corp != null ? corp.hashCode() : 0;
      result = result * 37 + (items != null ? items.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<WealthDrawCorpListRes> {

    public Boolean corp;
    public List<WealthDrawCorpItems> items;

    public Builder() {
    }

    public Builder(WealthDrawCorpListRes message) {
      super(message);
      if (message == null) return;
      this.corp = message.corp;
      this.items = copyOf(message.items);
    }

    public Builder corp(Boolean corp) {
      this.corp = corp;
      return this;
    }

    public Builder items(List<WealthDrawCorpItems> items) {
      this.items = checkForNulls(items);
      return this;
    }

    @Override
    public WealthDrawCorpListRes build() {
      return new WealthDrawCorpListRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
