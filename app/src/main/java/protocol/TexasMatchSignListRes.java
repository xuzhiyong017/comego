// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class TexasMatchSignListRes extends Message {

  public static final Integer DEFAULT_ACTID = 0;
  public static final List<UserInfo> DEFAULT_USERS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32)
  public final Integer actId;

  @ProtoField(tag = 2, label = REPEATED)
  public final List<UserInfo> users;

  private TexasMatchSignListRes(Builder builder) {
    this.actId = builder.actId;
    this.users = immutableCopyOf(builder.users);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasMatchSignListRes)) return false;
    TexasMatchSignListRes o = (TexasMatchSignListRes) other;
    return equals(actId, o.actId)
        && equals(users, o.users);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = actId != null ? actId.hashCode() : 0;
      result = result * 37 + (users != null ? users.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasMatchSignListRes> {

    public Integer actId;
    public List<UserInfo> users;

    public Builder() {
    }

    public Builder(TexasMatchSignListRes message) {
      super(message);
      if (message == null) return;
      this.actId = message.actId;
      this.users = copyOf(message.users);
    }

    public Builder actId(Integer actId) {
      this.actId = actId;
      return this;
    }

    public Builder users(List<UserInfo> users) {
      this.users = checkForNulls(users);
      return this;
    }

    @Override
    public TexasMatchSignListRes build() {
      return new TexasMatchSignListRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
