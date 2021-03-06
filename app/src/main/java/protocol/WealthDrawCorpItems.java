// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class WealthDrawCorpItems extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final Long DEFAULT_YYUID = 0L;
  public static final Long DEFAULT_YYNO = 0L;
  public static final Long DEFAULT_CORPUID = 0L;
  public static final String DEFAULT_NICK = "";
  public static final String DEFAULT_CORPTYPE = "";
  public static final Integer DEFAULT_OFFLINESIGN = 0;

  @ProtoField(tag = 1, type = UINT64)
  public final Long uid;

  @ProtoField(tag = 2, type = UINT64)
  public final Long yyuid;

  @ProtoField(tag = 3, type = UINT64)
  public final Long yyno;

  @ProtoField(tag = 4, type = UINT64)
  public final Long corpUid;

  @ProtoField(tag = 5, type = STRING)
  public final String nick;

  @ProtoField(tag = 6, type = STRING)
  public final String corpType;

  @ProtoField(tag = 7, type = UINT32)
  public final Integer offlineSign;

  private WealthDrawCorpItems(Builder builder) {
    this.uid = builder.uid;
    this.yyuid = builder.yyuid;
    this.yyno = builder.yyno;
    this.corpUid = builder.corpUid;
    this.nick = builder.nick;
    this.corpType = builder.corpType;
    this.offlineSign = builder.offlineSign;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof WealthDrawCorpItems)) return false;
    WealthDrawCorpItems o = (WealthDrawCorpItems) other;
    return equals(uid, o.uid)
        && equals(yyuid, o.yyuid)
        && equals(yyno, o.yyno)
        && equals(corpUid, o.corpUid)
        && equals(nick, o.nick)
        && equals(corpType, o.corpType)
        && equals(offlineSign, o.offlineSign);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (yyuid != null ? yyuid.hashCode() : 0);
      result = result * 37 + (yyno != null ? yyno.hashCode() : 0);
      result = result * 37 + (corpUid != null ? corpUid.hashCode() : 0);
      result = result * 37 + (nick != null ? nick.hashCode() : 0);
      result = result * 37 + (corpType != null ? corpType.hashCode() : 0);
      result = result * 37 + (offlineSign != null ? offlineSign.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<WealthDrawCorpItems> {

    public Long uid;
    public Long yyuid;
    public Long yyno;
    public Long corpUid;
    public String nick;
    public String corpType;
    public Integer offlineSign;

    public Builder() {
    }

    public Builder(WealthDrawCorpItems message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.yyuid = message.yyuid;
      this.yyno = message.yyno;
      this.corpUid = message.corpUid;
      this.nick = message.nick;
      this.corpType = message.corpType;
      this.offlineSign = message.offlineSign;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder yyuid(Long yyuid) {
      this.yyuid = yyuid;
      return this;
    }

    public Builder yyno(Long yyno) {
      this.yyno = yyno;
      return this;
    }

    public Builder corpUid(Long corpUid) {
      this.corpUid = corpUid;
      return this;
    }

    public Builder nick(String nick) {
      this.nick = nick;
      return this;
    }

    public Builder corpType(String corpType) {
      this.corpType = corpType;
      return this;
    }

    public Builder offlineSign(Integer offlineSign) {
      this.offlineSign = offlineSign;
      return this;
    }

    @Override
    public WealthDrawCorpItems build() {
      return new WealthDrawCorpItems(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
