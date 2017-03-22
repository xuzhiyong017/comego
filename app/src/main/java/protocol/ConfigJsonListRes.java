// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

public final class ConfigJsonListRes extends Message {

  public static final List<ConfigJsonData> DEFAULT_ITEMS = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED)
  public final List<ConfigJsonData> items;

  private ConfigJsonListRes(Builder builder) {
    this.items = immutableCopyOf(builder.items);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ConfigJsonListRes)) return false;
    return equals(items, ((ConfigJsonListRes) other).items);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = items != null ? items.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<ConfigJsonListRes> {

    public List<ConfigJsonData> items;

    public Builder() {
    }

    public Builder(ConfigJsonListRes message) {
      super(message);
      if (message == null) return;
      this.items = copyOf(message.items);
    }

    public Builder items(List<ConfigJsonData> items) {
      this.items = checkForNulls(items);
      return this;
    }

    @Override
    public ConfigJsonListRes build() {
      return new ConfigJsonListRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
