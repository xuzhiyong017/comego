// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT64;

/**
 * Encode编码模式
 * 0 720X1280,20fps,1300kbps,1100-1500kbps,1300kbps,1800kbps,MEDIA_QUALITY_MUSIC
 * 1 540X960,20fps,750kbps,600-900kbps,750kbps,1100kbps,MEDIA_QUALITY_MUSIC
 * 2 480X720,20fps,600kbps,500-700kbps,600kbps,900kbps,MEDIA_QUALITY_MUSIC
 * 3 360X480,15fps,400kbps,300-500kbps,400kbps,700kbps,MEDIA_QUALITY_MEDIUM
 * 4 240X320,15fps,300kbps,200-400kbps,300kbps,600kbps,MEDIA_QUALITY_MEDIUM
 * 5 160X240,15fps,200kbps,100-300kbps,200kbps,500kbps,MEDIA_QUALITY_MEDIUM
 * 退出群组
 */
public final class GroupQuitReq extends Message {

  public static final Long DEFAULT_GID = 0L;

  @ProtoField(tag = 1, type = UINT64)
  public final Long gid;

  private GroupQuitReq(Builder builder) {
    this.gid = builder.gid;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupQuitReq)) return false;
    return equals(gid, ((GroupQuitReq) other).gid);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = gid != null ? gid.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<GroupQuitReq> {

    public Long gid;

    public Builder() {
    }

    public Builder(GroupQuitReq message) {
      super(message);
      if (message == null) return;
      this.gid = message.gid;
    }

    public Builder gid(Long gid) {
      this.gid = gid;
      return this;
    }

    @Override
    public GroupQuitReq build() {
      return new GroupQuitReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}