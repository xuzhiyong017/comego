// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 激活帐号
 */
public final class UserActivateReq extends Message {

  public static final String DEFAULT_MACID = "";
  public static final String DEFAULT_TOKEN = "";
  public static final String DEFAULT_IDENTIFYINGCODE = "";
  public static final String DEFAULT_PASSWORD = "";
  public static final String DEFAULT_FINGER = "";
  public static final ProtoVersion DEFAULT_PROTOVERSION = ProtoVersion.ProtoVersion_None;
  public static final String DEFAULT_IPADDRESS = "";
  public static final Boolean DEFAULT_RESETPASSWORD = false;
  public static final String DEFAULT_FROMSTORE = "";
  public static final String DEFAULT_INVITECODE = "";

  @ProtoField(tag = 1, type = STRING, label = REQUIRED)
  public final String macid;

  @ProtoField(tag = 2, type = STRING)
  public final String token;

  /**
   * 手机证码对应的Token
   */
  @ProtoField(tag = 3, type = STRING)
  public final String identifyingCode;

  /**
   * 手机验证码
   */
  @ProtoField(tag = 4, type = STRING)
  public final String password;

  @ProtoField(tag = 5)
  public final UserInfo userInfo;

  @ProtoField(tag = 20, type = STRING)
  public final String finger;

  /**
   * 指纹
   */
  @ProtoField(tag = 30, type = ENUM)
  public final ProtoVersion protoVersion;

  @ProtoField(tag = 40, type = STRING)
  public final String ipaddress;

  /**
   * 内部使用，客户端不用赋值
   */
  @ProtoField(tag = 50, type = BOOL)
  public final Boolean resetpassword;

  /**
   * 重置密码
   */
  @ProtoField(tag = 60, type = STRING)
  public final String fromStore;

  @ProtoField(tag = 100, type = STRING)
  public final String inviteCode;

  private UserActivateReq(Builder builder) {
    this.macid = builder.macid;
    this.token = builder.token;
    this.identifyingCode = builder.identifyingCode;
    this.password = builder.password;
    this.userInfo = builder.userInfo;
    this.finger = builder.finger;
    this.protoVersion = builder.protoVersion;
    this.ipaddress = builder.ipaddress;
    this.resetpassword = builder.resetpassword;
    this.fromStore = builder.fromStore;
    this.inviteCode = builder.inviteCode;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserActivateReq)) return false;
    UserActivateReq o = (UserActivateReq) other;
    return equals(macid, o.macid)
        && equals(token, o.token)
        && equals(identifyingCode, o.identifyingCode)
        && equals(password, o.password)
        && equals(userInfo, o.userInfo)
        && equals(finger, o.finger)
        && equals(protoVersion, o.protoVersion)
        && equals(ipaddress, o.ipaddress)
        && equals(resetpassword, o.resetpassword)
        && equals(fromStore, o.fromStore)
        && equals(inviteCode, o.inviteCode);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = macid != null ? macid.hashCode() : 0;
      result = result * 37 + (token != null ? token.hashCode() : 0);
      result = result * 37 + (identifyingCode != null ? identifyingCode.hashCode() : 0);
      result = result * 37 + (password != null ? password.hashCode() : 0);
      result = result * 37 + (userInfo != null ? userInfo.hashCode() : 0);
      result = result * 37 + (finger != null ? finger.hashCode() : 0);
      result = result * 37 + (protoVersion != null ? protoVersion.hashCode() : 0);
      result = result * 37 + (ipaddress != null ? ipaddress.hashCode() : 0);
      result = result * 37 + (resetpassword != null ? resetpassword.hashCode() : 0);
      result = result * 37 + (fromStore != null ? fromStore.hashCode() : 0);
      result = result * 37 + (inviteCode != null ? inviteCode.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UserActivateReq> {

    public String macid;
    public String token;
    public String identifyingCode;
    public String password;
    public UserInfo userInfo;
    public String finger;
    public ProtoVersion protoVersion;
    public String ipaddress;
    public Boolean resetpassword;
    public String fromStore;
    public String inviteCode;

    public Builder() {
    }

    public Builder(UserActivateReq message) {
      super(message);
      if (message == null) return;
      this.macid = message.macid;
      this.token = message.token;
      this.identifyingCode = message.identifyingCode;
      this.password = message.password;
      this.userInfo = message.userInfo;
      this.finger = message.finger;
      this.protoVersion = message.protoVersion;
      this.ipaddress = message.ipaddress;
      this.resetpassword = message.resetpassword;
      this.fromStore = message.fromStore;
      this.inviteCode = message.inviteCode;
    }

    public Builder macid(String macid) {
      this.macid = macid;
      return this;
    }

    public Builder token(String token) {
      this.token = token;
      return this;
    }

    /**
     * 手机证码对应的Token
     */
    public Builder identifyingCode(String identifyingCode) {
      this.identifyingCode = identifyingCode;
      return this;
    }

    /**
     * 手机验证码
     */
    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder userInfo(UserInfo userInfo) {
      this.userInfo = userInfo;
      return this;
    }

    public Builder finger(String finger) {
      this.finger = finger;
      return this;
    }

    /**
     * 指纹
     */
    public Builder protoVersion(ProtoVersion protoVersion) {
      this.protoVersion = protoVersion;
      return this;
    }

    public Builder ipaddress(String ipaddress) {
      this.ipaddress = ipaddress;
      return this;
    }

    /**
     * 内部使用，客户端不用赋值
     */
    public Builder resetpassword(Boolean resetpassword) {
      this.resetpassword = resetpassword;
      return this;
    }

    /**
     * 重置密码
     */
    public Builder fromStore(String fromStore) {
      this.fromStore = fromStore;
      return this;
    }

    public Builder inviteCode(String inviteCode) {
      this.inviteCode = inviteCode;
      return this;
    }

    @Override
    public UserActivateReq build() {
      checkRequiredFields();
      return new UserActivateReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
