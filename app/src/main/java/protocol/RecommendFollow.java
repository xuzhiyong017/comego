// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class RecommendFollow extends Message {

  public static final Long DEFAULT_UID = 0L;
  public static final String DEFAULT_NAME = "";
  public static final String DEFAULT_LOGOURL = "";
  public static final Long DEFAULT_LOVEVAL = 0L;
  public static final SexType DEFAULT_SEX = SexType.Unknow;

  @ProtoField(tag = 1, type = UINT64, label = REQUIRED)
  public final Long uid;

  @ProtoField(tag = 2, type = STRING, label = REQUIRED)
  public final String name;

  @ProtoField(tag = 3, type = STRING)
  public final String logoUrl;

  @ProtoField(tag = 4, type = UINT64)
  public final Long loveVal;

  @ProtoField(tag = 5, type = ENUM)
  public final SexType sex;

  private RecommendFollow(Builder builder) {
    this.uid = builder.uid;
    this.name = builder.name;
    this.logoUrl = builder.logoUrl;
    this.loveVal = builder.loveVal;
    this.sex = builder.sex;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RecommendFollow)) return false;
    RecommendFollow o = (RecommendFollow) other;
    return equals(uid, o.uid)
        && equals(name, o.name)
        && equals(logoUrl, o.logoUrl)
        && equals(loveVal, o.loveVal)
        && equals(sex, o.sex);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = uid != null ? uid.hashCode() : 0;
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (logoUrl != null ? logoUrl.hashCode() : 0);
      result = result * 37 + (loveVal != null ? loveVal.hashCode() : 0);
      result = result * 37 + (sex != null ? sex.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<RecommendFollow> {

    public Long uid;
    public String name;
    public String logoUrl;
    public Long loveVal;
    public SexType sex;

    public Builder() {
    }

    public Builder(RecommendFollow message) {
      super(message);
      if (message == null) return;
      this.uid = message.uid;
      this.name = message.name;
      this.logoUrl = message.logoUrl;
      this.loveVal = message.loveVal;
      this.sex = message.sex;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder logoUrl(String logoUrl) {
      this.logoUrl = logoUrl;
      return this;
    }

    public Builder loveVal(Long loveVal) {
      this.loveVal = loveVal;
      return this;
    }

    public Builder sex(SexType sex) {
      this.sex = sex;
      return this;
    }

    @Override
    public RecommendFollow build() {
      checkRequiredFields();
      return new RecommendFollow(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
