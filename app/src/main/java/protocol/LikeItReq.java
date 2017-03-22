// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 点赞
 */
public final class LikeItReq extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final Integer DEFAULT_HITS = 1;
  public static final Long DEFAULT_GID = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long uid;

  @ProtoField(tag = 2, type = UINT32)
  public final Integer hits;

  /**
   * 点了多少次
   */
  @ProtoField(tag = 3, type = UINT64)
  public final Long gid;

  private LikeItReq(Builder builder) {
    this.uid = builder.uid;
    this.hits = builder.hits;
    this.gid = builder.gid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof LikeItReq)) return false;
    LikeItReq o = (LikeItReq) other;
    return equals(uid, o.uid)
        && equals(hits, o.hits)
        && equals(gid, o.gid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (hits != null ? hits.hashCode() : 0);
      result = result * 37 + (gid != null ? gid.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<LikeItReq> {

    public Long uid;
    public Integer hits;
    public Long gid;

    public Builder() {
    }

    public Builder(LikeItReq message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.hits = message.hits;
      this.gid = message.gid;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder hits(Integer hits) {
      this.hits = hits;
      return this;
    }

    /**
     * 点了多少次
     */
    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    @Override
    public LikeItReq build() {
      checkRequiredFields();
      return new LikeItReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
