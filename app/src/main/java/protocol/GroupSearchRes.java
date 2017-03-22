// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GroupSearchRes extends Message {

  public static final String DEFAULT_KEYWORD = "";
  public static final Integer DEFAULT_TOTAL = 0;
  public static final Integer DEFAULT_INDEX = 0;
  public static final Integer DEFAULT_FETCHS = 0;
  public static final GroupType DEFAULT_GTYPE = GroupType.Group_Unknown;
  public static final GroupSearchBy DEFAULT_SEARCHBY = GroupSearchBy.GroupSearchByUnknown;
  public static final GroupSearchOrderBy DEFAULT_ORDERBY = GroupSearchOrderBy.GroupSearchOrderByDesc;
  public static final List<GroupInfo> DEFAULT_GROUPS = Collections.emptyList();
  public static final Integer DEFAULT_GAMEID = 0;
  public static final Long DEFAULT_GIFTID = 0L;
  public static final Long DEFAULT_CACHEKEY = 0L;

  @ProtoField(tag = 1, type = STRING)
  public final String keyword;

  @ProtoField(tag = 2, type = UINT32)
  public final Integer total;

  @ProtoField(tag = 3, type = UINT32)
  public final Integer index;

  @ProtoField(tag = 4, type = UINT32)
  public final Integer fetchs;

  @ProtoField(tag = 5, type = ENUM)
  public final GroupType gtype;

  @ProtoField(tag = 10, type = ENUM)
  public final GroupSearchBy searchBy;

  @ProtoField(tag = 11, type = ENUM)
  public final GroupSearchOrderBy orderBy;

  /**
   * 升序或降序
   */
  @ProtoField(tag = 20, label = REPEATED)
  public final List<GroupInfo> groups;

  @ProtoField(tag = 21, type = UINT32)
  public final Integer gameid;

  /**
   * 游戏id
   */
  @ProtoField(tag = 22, type = UINT64)
  public final Long giftid;

  /**
   * 礼包id  (查找有某款游戏礼包的公会列表)
   */
  @ProtoField(tag = 100, type = UINT64)
  public final Long cacheKey;

  private GroupSearchRes(Builder builder) {
    this.keyword = builder.keyword;
    this.total = builder.total;
    this.index = builder.index;
    this.fetchs = builder.fetchs;
    this.gtype = builder.gtype;
    this.searchBy = builder.searchBy;
    this.orderBy = builder.orderBy;
    this.groups = immutableCopyOf(builder.groups);
    this.gameid = builder.gameid;
    this.giftid = builder.giftid;
    this.cacheKey = builder.cacheKey;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupSearchRes)) return false;
    GroupSearchRes o = (GroupSearchRes) other;
    return equals(keyword, o.keyword)
        && equals(total, o.total)
        && equals(index, o.index)
        && equals(fetchs, o.fetchs)
        && equals(gtype, o.gtype)
        && equals(searchBy, o.searchBy)
        && equals(orderBy, o.orderBy)
        && equals(groups, o.groups)
        && equals(gameid, o.gameid)
        && equals(giftid, o.giftid)
        && equals(cacheKey, o.cacheKey);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = keyword != null ? keyword.hashCode() : 0;
      result = result * 37 + (total != null ? total.hashCode() : 0);
      result = result * 37 + (index != null ? index.hashCode() : 0);
      result = result * 37 + (fetchs != null ? fetchs.hashCode() : 0);
      result = result * 37 + (gtype != null ? gtype.hashCode() : 0);
      result = result * 37 + (searchBy != null ? searchBy.hashCode() : 0);
      result = result * 37 + (orderBy != null ? orderBy.hashCode() : 0);
      result = result * 37 + (groups != null ? groups.hashCode() : 1);
      result = result * 37 + (gameid != null ? gameid.hashCode() : 0);
      result = result * 37 + (giftid != null ? giftid.hashCode() : 0);
      result = result * 37 + (cacheKey != null ? cacheKey.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupSearchRes> {

    public String keyword;
    public Integer total;
    public Integer index;
    public Integer fetchs;
    public GroupType gtype;
    public GroupSearchBy searchBy;
    public GroupSearchOrderBy orderBy;
    public List<GroupInfo> groups;
    public Integer gameid;
    public Long giftid;
    public Long cacheKey;

    public Builder() {
    }

    public Builder(GroupSearchRes message) {
      super(message);
      if (message == null) return;
      this.keyword = message.keyword;
      this.total = message.total;
      this.index = message.index;
      this.fetchs = message.fetchs;
      this.gtype = message.gtype;
      this.searchBy = message.searchBy;
      this.orderBy = message.orderBy;
      this.groups = copyOf(message.groups);
      this.gameid = message.gameid;
      this.giftid = message.giftid;
      this.cacheKey = message.cacheKey;
    }

    public Builder keyword(String keyword) {
      this.keyword = keyword;
      return this;
    }

    public Builder total(Integer total) {
      this.total = total;
      return this;
    }

    public Builder index(Integer index) {
      this.index = index;
      return this;
    }

    public Builder fetchs(Integer fetchs) {
      this.fetchs = fetchs;
      return this;
    }

    public Builder gtype(GroupType gtype) {
      this.gtype = gtype;
      return this;
    }

    public Builder searchBy(GroupSearchBy searchBy) {
      this.searchBy = searchBy;
      return this;
    }

    public Builder orderBy(GroupSearchOrderBy orderBy) {
      this.orderBy = orderBy;
      return this;
    }

    /**
     * 升序或降序
     */
    public Builder groups(List<GroupInfo> groups) {
      this.groups = checkForNulls(groups);
      return this;
    }

    public Builder gameid(Integer gameid) {
      this.gameid = gameid;
      return this;
    }

    /**
     * 游戏id
     */
    public Builder giftid(Long giftid) {
      this.giftid = giftid;
      return this;
    }

    /**
     * 礼包id  (查找有某款游戏礼包的公会列表)
     */
    public Builder cacheKey(Long cacheKey) {
      this.cacheKey = cacheKey;
      return this;
    }

    @Override
    public GroupSearchRes build() {
      return new GroupSearchRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
