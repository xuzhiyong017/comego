// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class RecommendFollowSetReq extends Message {

  public static final Integer DEFAULT_OP = 0;
  public static final List<Long> DEFAULT_UIDS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer op;

  /**
   * 0查询1增加2删除
   */
  @ProtoField(tag = 2, type = UINT64, label = REPEATED)
  public final List<Long> uids;

  private RecommendFollowSetReq(Builder builder) {
    this.op = builder.op;
    this.uids = immutableCopyOf(builder.uids);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RecommendFollowSetReq)) return false;
    RecommendFollowSetReq o = (RecommendFollowSetReq) other;
    return equals(op, o.op)
        && equals(uids, o.uids);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = op != null ? op.hashCode() : 0;
      result = result * 37 + (uids != null ? uids.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<RecommendFollowSetReq> {

    public Integer op;
    public List<Long> uids;

    public Builder() {
    }

    public Builder(RecommendFollowSetReq message) {
      super(message);
      if (message == null) return;
      this.op = message.op;
      this.uids = copyOf(message.uids);
    }

    public Builder op(Integer op) {
      this.op = op;
      return this;
    }

    /**
     * 0查询1增加2删除
     */
    public Builder uids(List<Long> uids) {
      this.uids = checkForNulls(uids);
      return this;
    }

    @Override
    public RecommendFollowSetReq build() {
      checkRequiredFields();
      return new RecommendFollowSetReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
