// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class MmsReportCmdReq extends Message {

  public static final String DEFAULT_APPKEY = "";
  public static final String DEFAULT_SERIAL = "";
  public static final String DEFAULT_CMD = "";
  public static final String DEFAULT_REASON = "";
  public static final String DEFAULT_MSG = "";
  public static final String DEFAULT_EXTPARURLENCODER = "";
  public static final String DEFAULT_SIGN = "";
  public static final String DEFAULT_STATUS = "";
  public static final String DEFAULT_REC = "";

  @ProtoField(tag = 1, type = STRING)
  public final String appKey;

  @ProtoField(tag = 2, type = STRING)
  public final String serial;

  @ProtoField(tag = 3, type = STRING)
  public final String cmd;

  @ProtoField(tag = 4, type = STRING)
  public final String reason;

  @ProtoField(tag = 5, type = STRING)
  public final String msg;

  @ProtoField(tag = 6, type = STRING)
  public final String extParUrlEncoder;

  @ProtoField(tag = 7, type = STRING)
  public final String sign;

  @ProtoField(tag = 8, type = STRING)
  public final String status;

  @ProtoField(tag = 10, type = STRING)
  public final String rec;

  private MmsReportCmdReq(Builder builder) {
    this.appKey = builder.appKey;
    this.serial = builder.serial;
    this.cmd = builder.cmd;
    this.reason = builder.reason;
    this.msg = builder.msg;
    this.extParUrlEncoder = builder.extParUrlEncoder;
    this.sign = builder.sign;
    this.status = builder.status;
    this.rec = builder.rec;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MmsReportCmdReq)) return false;
    MmsReportCmdReq o = (MmsReportCmdReq) other;
    return equals(appKey, o.appKey)
        && equals(serial, o.serial)
        && equals(cmd, o.cmd)
        && equals(reason, o.reason)
        && equals(msg, o.msg)
        && equals(extParUrlEncoder, o.extParUrlEncoder)
        && equals(sign, o.sign)
        && equals(status, o.status)
        && equals(rec, o.rec);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = appKey != null ? appKey.hashCode() : 0;
      result = result * 37 + (serial != null ? serial.hashCode() : 0);
      result = result * 37 + (cmd != null ? cmd.hashCode() : 0);
      result = result * 37 + (reason != null ? reason.hashCode() : 0);
      result = result * 37 + (msg != null ? msg.hashCode() : 0);
      result = result * 37 + (extParUrlEncoder != null ? extParUrlEncoder.hashCode() : 0);
      result = result * 37 + (sign != null ? sign.hashCode() : 0);
      result = result * 37 + (status != null ? status.hashCode() : 0);
      result = result * 37 + (rec != null ? rec.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<MmsReportCmdReq> {

    public String appKey;
    public String serial;
    public String cmd;
    public String reason;
    public String msg;
    public String extParUrlEncoder;
    public String sign;
    public String status;
    public String rec;

    public Builder() {
    }

    public Builder(MmsReportCmdReq message) {
      super(message);
      if (message == null) return;
      this.appKey = message.appKey;
      this.serial = message.serial;
      this.cmd = message.cmd;
      this.reason = message.reason;
      this.msg = message.msg;
      this.extParUrlEncoder = message.extParUrlEncoder;
      this.sign = message.sign;
      this.status = message.status;
      this.rec = message.rec;
    }

    public Builder appKey(String appKey) {
      this.appKey = appKey;
      return this;
    }

    public Builder serial(String serial) {
      this.serial = serial;
      return this;
    }

    public Builder cmd(String cmd) {
      this.cmd = cmd;
      return this;
    }

    public Builder reason(String reason) {
      this.reason = reason;
      return this;
    }

    public Builder msg(String msg) {
      this.msg = msg;
      return this;
    }

    public Builder extParUrlEncoder(String extParUrlEncoder) {
      this.extParUrlEncoder = extParUrlEncoder;
      return this;
    }

    public Builder sign(String sign) {
      this.sign = sign;
      return this;
    }

    public Builder status(String status) {
      this.status = status;
      return this;
    }

    public Builder rec(String rec) {
      this.rec = rec;
      return this;
    }

    @Override
    public MmsReportCmdReq build() {
      return new MmsReportCmdReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
