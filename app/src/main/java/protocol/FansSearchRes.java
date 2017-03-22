// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;

public final class FansSearchRes extends Message {

  public static final Integer DEFAULT_INDEX = 0;
  public static final Integer DEFAULT_FETCHS = 0;
  public static final FansSearchBy DEFAULT_SEARCHBY = FansSearchBy.FansSearchByNormal;
  public static final Long DEFAULT_UID = 0L;
  public static final Boolean DEFAULT_FAN = false;
  public static final Boolean DEFAULT_FOLLOW = false;
  public static final Long DEFAULT_SORTKEY = 0L;
  public static final Integer DEFAULT_TOTALRESULTS = 0;
  public static final List<Fan> DEFAULT_FANS = Collections.emptyList();
  public static final Boolean DEFAULT_HASMORE = false;

  @ProtoField(tag = 1, type = UINT32)
  public final Integer index;

  @ProtoField(tag = 2, type = UINT32)
  public final Integer fetchs;

  @ProtoField(tag = 3, type = ENUM)
  public final FansSearchBy searchby;

  @ProtoField(tag = 5, type = UINT64)
  public final Long uid;

  @ProtoField(tag = 6, type = BOOL)
  public final Boolean fan;

  /**
   * (UID)的粉丝
   */
  @ProtoField(tag = 7, type = BOOL)
  public final Boolean follow;

  /**
   * (UID)关注的人
   */
  @ProtoField(tag = 9, type = INT64)
  public final Long sortkey;

  @ProtoField(tag = 11, type = UINT32)
  public final Integer totalresults;

  /**
   * 暂时不用
   */
  @ProtoField(tag = 10, label = REPEATED)
  public final List<Fan> fans;

  @ProtoField(tag = 13, type = BOOL)
  public final Boolean hasmore;

  private FansSearchRes(Builder builder) {
    this.index = builder.index;
    this.fetchs = builder.fetchs;
    this.searchby = builder.searchby;
    this.uid = builder.uid;
    this.fan = builder.fan;
    this.follow = builder.follow;
    this.sortkey = builder.sortkey;
    this.totalresults = builder.totalresults;
    this.fans = immutableCopyOf(builder.fans);
    this.hasmore = builder.hasmore;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof FansSearchRes)) return false;
    FansSearchRes o = (FansSearchRes) other;
    return equals(index, o.index)
        && equals(fetchs, o.fetchs)
        && equals(searchby, o.searchby)
        && equals(uid, o.uid)
        && equals(fan, o.fan)
        && equals(follow, o.follow)
        && equals(sortkey, o.sortkey)
        && equals(totalresults, o.totalresults)
        && equals(fans, o.fans)
        && equals(hasmore, o.hasmore);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = index != null ? index.hashCode() : 0;
      result = result * 37 + (fetchs != null ? fetchs.hashCode() : 0);
      result = result * 37 + (searchby != null ? searchby.hashCode() : 0);
      result = result * 37 + (uid != null ? uid.hashCode() : 0);
      result = result * 37 + (fan != null ? fan.hashCode() : 0);
      result = result * 37 + (follow != null ? follow.hashCode() : 0);
      result = result * 37 + (sortkey != null ? sortkey.hashCode() : 0);
      result = result * 37 + (totalresults != null ? totalresults.hashCode() : 0);
      result = result * 37 + (fans != null ? fans.hashCode() : 1);
      result = result * 37 + (hasmore != null ? hasmore.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<FansSearchRes> {

    public Integer index;
    public Integer fetchs;
    public FansSearchBy searchby;
    public Long uid;
    public Boolean fan;
    public Boolean follow;
    public Long sortkey;
    public Integer totalresults;
    public List<Fan> fans;
    public Boolean hasmore;

    public Builder() {
    }

    public Builder(FansSearchRes message) {
      super(message);
      if (message == null) return;
      this.index = message.index;
      this.fetchs = message.fetchs;
      this.searchby = message.searchby;
      this.uid = message.uid;
      this.fan = message.fan;
      this.follow = message.follow;
      this.sortkey = message.sortkey;
      this.totalresults = message.totalresults;
      this.fans = copyOf(message.fans);
      this.hasmore = message.hasmore;
    }

    public Builder index(Integer index) {
      this.index = index;
      return this;
    }

    public Builder fetchs(Integer fetchs) {
      this.fetchs = fetchs;
      return this;
    }

    public Builder searchby(FansSearchBy searchby) {
      this.searchby = searchby;
      return this;
    }

    public Builder uid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder fan(Boolean fan) {
      this.fan = fan;
      return this;
    }

    /**
     * (UID)的粉丝
     */
    public Builder follow(Boolean follow) {
      this.follow = follow;
      return this;
    }

    /**
     * (UID)关注的人
     */
    public Builder sortkey(Long sortkey) {
      this.sortkey = sortkey;
      return this;
    }

    public Builder totalresults(Integer totalresults) {
      this.totalresults = totalresults;
      return this;
    }

    /**
     * 暂时不用
     */
    public Builder fans(List<Fan> fans) {
      this.fans = checkForNulls(fans);
      return this;
    }

    public Builder hasmore(Boolean hasmore) {
      this.hasmore = hasmore;
      return this;
    }

    @Override
    public FansSearchRes build() {
      return new FansSearchRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
