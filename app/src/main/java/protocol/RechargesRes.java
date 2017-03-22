// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

public final class RechargesRes extends Message {

  public static final List<UserRecharge> DEFAULT_RECHARGES = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED)
  public final List<UserRecharge> recharges;

  private RechargesRes(Builder builder) {
    this.recharges = immutableCopyOf(builder.recharges);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RechargesRes)) return false;
    return equals(recharges, ((RechargesRes) other).recharges);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = recharges != null ? recharges.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<RechargesRes> {

    public List<UserRecharge> recharges;

    public Builder() {
    }

    public Builder(RechargesRes message) {
      super(message);
      if (message == null) return;
      this.recharges = copyOf(message.recharges);
    }

    public Builder recharges(List<UserRecharge> recharges) {
      this.recharges = checkForNulls(recharges);
      return this;
    }

    @Override
    public RechargesRes build() {
      return new RechargesRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}