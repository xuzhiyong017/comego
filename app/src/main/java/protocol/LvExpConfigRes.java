// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

public final class LvExpConfigRes extends Message {

  public static final List<LvExpConfig> DEFAULT_CONFIGS = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED)
  public final List<LvExpConfig> configs;

  private LvExpConfigRes(Builder builder) {
    this.configs = immutableCopyOf(builder.configs);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof LvExpConfigRes)) return false;
    return equals(configs, ((LvExpConfigRes) other).configs);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = configs != null ? configs.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<LvExpConfigRes> {

    public List<LvExpConfig> configs;

    public Builder() {
    }

    public Builder(LvExpConfigRes message) {
      super(message);
      if (message == null) return;
      this.configs = copyOf(message.configs);
    }

    public Builder configs(List<LvExpConfig> configs) {
      this.configs = checkForNulls(configs);
      return this;
    }

    @Override
    public LvExpConfigRes build() {
      return new LvExpConfigRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
