// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class LuckyDrawNumReq extends Message {

  public static final String DEFAULT_ACTNAME = "";

  @ProtoField(tag = 1, type = STRING, label = REQUIRED)
  public final String actName;

  private LuckyDrawNumReq(Builder builder) {
    this.actName = builder.actName;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof LuckyDrawNumReq)) return false;
    return equals(actName, ((LuckyDrawNumReq) other).actName);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = actName != null ? actName.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<LuckyDrawNumReq> {

    public String actName;

    public Builder() {
    }

    public Builder(LuckyDrawNumReq message) {
      super(message);
      if (message == null) return;
      this.actName = message.actName;
    }

    public Builder actName(String actName) {
      this.actName = actName;
      return this;
    }

    @Override
    public LuckyDrawNumReq build() {
      checkRequiredFields();
      return new LuckyDrawNumReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}