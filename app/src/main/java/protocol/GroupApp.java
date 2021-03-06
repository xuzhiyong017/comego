// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class GroupApp extends Message {

  public static final Long DEFAULT_REFMSG = 0L;
  public static final GroupAppType DEFAULT_APPTYPE = GroupAppType.GroupAppTypeNone;
  public static final GroupAppOp DEFAULT_APPOP = GroupAppOp.GroupAppOpAdd;
  public static final String DEFAULT_MSG = "";
  public static final Long DEFAULT_UID = 0L;
  public static final Long DEFAULT_REVISION = 0L;
  public static final Long DEFAULT_TIMESTAMP = 0L;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long refmsg;

  /**
   * 引用的消息版本号，发送时必填项
   */
  @ProtoField(tag = 2, type = ENUM, label = REQUIRED)
  public final GroupAppType apptype;

  /**
   * 应用类型，发送时必填项
   */
  @ProtoField(tag = 3, type = ENUM)
  public final GroupAppOp appop;

  /**
   * 操作方式
   */
  @ProtoField(tag = 4, type = STRING)
  public final String msg;

  /**
   * 发送评论时必填项
   */
  @ProtoField(tag = 5, type = UINT64)
  public final Long uid;

  /**
   * 用户ID
   */
  @ProtoField(tag = 6, type = UINT64)
  public final Long revision;

  /**
   * 评论版本号
   */
  @ProtoField(tag = 7, type = INT64)
  public final Long timestamp;

  private GroupApp(Builder builder) {
    this.refmsg = builder.refmsg;
    this.apptype = builder.apptype;
    this.appop = builder.appop;
    this.msg = builder.msg;
    this.uid = builder.uid;
    this.revision = builder.revision;
    this.timestamp = builder.timestamp;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupApp)) return false;
    GroupApp o = (GroupApp) other;
    return equals(refmsg, o.refmsg)
        && equals(apptype, o.apptype)
        && equals(appop, o.appop)
        && equals(msg, o.msg)
        && equals(uid, o.uid)
        && equals(revision, o.revision)
        && equals(timestamp, o.timestamp);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = refmsg != null ? refmsg.hashCode() : 0;
      result = result * 37 + (apptype != null ? apptype.hashCode() : 0);
      result = result * 37 + (appop != null ? appop.hashCode() : 0);
      result = result * 37 + (msg != null ? msg.hashCode() : 0);
      result = result * 37 + (uid != null ? uid.hashCode() : 0);
      result = result * 37 + (revision != null ? revision.hashCode() : 0);
      result = result * 37 + (timestamp != null ? timestamp.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupApp> {

    public Long refmsg;
    public GroupAppType apptype;
    public GroupAppOp appop;
    public String msg;
    public Long uid;
    public Long revision;
    public Long timestamp;

    public Builder() {
    }

    public Builder(GroupApp message) {
      super(message);
      if (message == null) return;
      this.refmsg = message.refmsg;
      this.apptype = message.apptype;
      this.appop = message.appop;
      this.msg = message.msg;
      this.uid = message.uid;
      this.revision = message.revision;
      this.timestamp = message.timestamp;
    }

    public Builder refmsg(Long refmsg) {
      this.refmsg = refmsg;
      return this;
    }

    /**
     * 引用的消息版本号，发送时必填项
     */
    public Builder apptype(GroupAppType apptype) {
      this.apptype = apptype;
      return this;
    }

    /**
     * 应用类型，发送时必填项
     */
    public Builder appop(GroupAppOp appop) {
      this.appop = appop;
      return this;
    }

    /**
     * 操作方式
     */
    public Builder msg(String msg) {
      this.msg = msg;
      return this;
    }

    /**
     * 发送评论时必填项
     */
    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    /**
     * 用户ID
     */
    public Builder revision(Long revision) {
      this.revision = revision;
      return this;
    }

    /**
     * 评论版本号
     */
    public Builder timestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    @Override
    public GroupApp build() {
      checkRequiredFields();
      return new GroupApp(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
