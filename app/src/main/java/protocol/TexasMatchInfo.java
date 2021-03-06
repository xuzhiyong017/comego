// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;

public final class TexasMatchInfo extends Message {

  public static final Integer DEFAULT_ACTID = 0;
  public static final String DEFAULT_TITLE = "";
  public static final String DEFAULT_REWARDURL = "";
  public static final Long DEFAULT_ENTRYBEGINTIME = 0L;
  public static final Long DEFAULT_ENTRYENDTIME = 0L;
  public static final Long DEFAULT_GAMEBEGINTIME = 0L;
  public static final Long DEFAULT_GAMEENDTIME = 0L;
  public static final Long DEFAULT_PREPARESECONDS = 0L;
  public static final Long DEFAULT_RESULTSHOWSECONDS = 0L;
  public static final Long DEFAULT_SBLIND = 0L;
  public static final Long DEFAULT_MINCARRY = 0L;
  public static final Long DEFAULT_MAXCARRY = 0L;
  public static final Long DEFAULT_ENTRYFEE = 0L;
  public static final String DEFAULT_CONTENT = "";
  public static final Integer DEFAULT_MEMBERS = 0;
  public static final String DEFAULT_ENTRYINTRO = "";
  public static final String DEFAULT_ICONURL = "";
  public static final String DEFAULT_TAGICONURL = "";
  public static final String DEFAULT_BACKURL = "";

  @ProtoField(tag = 1, type = UINT32)
  public final Integer actId;

  @ProtoField(tag = 2, type = STRING)
  public final String title;

  @ProtoField(tag = 3, type = STRING)
  public final String rewardUrl;

  /**
   * 奖励说明页链接
   */
  @ProtoField(tag = 4, type = INT64)
  public final Long entryBeginTime;

  /**
   * 报名开始时间
   */
  @ProtoField(tag = 5, type = INT64)
  public final Long entryEndTime;

  /**
   * 报名结束时间
   */
  @ProtoField(tag = 6, type = INT64)
  public final Long gameBeginTime;

  /**
   * 游戏开始时间
   */
  @ProtoField(tag = 7, type = INT64)
  public final Long gameEndTime;

  /**
   * 游戏结束时间
   */
  @ProtoField(tag = 8, type = INT64)
  public final Long prepareSeconds;

  /**
   * 准备时间，离gameBeginTime XXX秒开始
   */
  @ProtoField(tag = 9, type = INT64)
  public final Long resultShowSeconds;

  /**
   * 展示时间，离gameEndTime XXX秒后结束
   */
  @ProtoField(tag = 10, type = UINT64)
  public final Long sBlind;

  /**
   * 小盲(0表示不限)
   */
  @ProtoField(tag = 11, type = UINT64)
  public final Long minCarry;

  /**
   * 最小携带
   */
  @ProtoField(tag = 12, type = UINT64)
  public final Long maxCarry;

  /**
   * 最大携带
   */
  @ProtoField(tag = 13, type = UINT64)
  public final Long entryFee;

  /**
   * 报名费
   */
  @ProtoField(tag = 14, type = STRING)
  public final String content;

  /**
   * 文本说明
   */
  @ProtoField(tag = 15, type = UINT32)
  public final Integer members;

  /**
   * 参与人数
   */
  @ProtoField(tag = 17, type = STRING)
  public final String entryIntro;

  /**
   * 排名说明
   */
  @ProtoField(tag = 18, type = STRING)
  public final String iconUrl;

  /**
   * 房间里的浮层按钮
   */
  @ProtoField(tag = 19, type = STRING)
  public final String tagIconUrl;

  /**
   * 主播带的标识
   */
  @ProtoField(tag = 20, type = STRING)
  public final String backUrl;

  private TexasMatchInfo(Builder builder) {
    this.actId = builder.actId;
    this.title = builder.title;
    this.rewardUrl = builder.rewardUrl;
    this.entryBeginTime = builder.entryBeginTime;
    this.entryEndTime = builder.entryEndTime;
    this.gameBeginTime = builder.gameBeginTime;
    this.gameEndTime = builder.gameEndTime;
    this.prepareSeconds = builder.prepareSeconds;
    this.resultShowSeconds = builder.resultShowSeconds;
    this.sBlind = builder.sBlind;
    this.minCarry = builder.minCarry;
    this.maxCarry = builder.maxCarry;
    this.entryFee = builder.entryFee;
    this.content = builder.content;
    this.members = builder.members;
    this.entryIntro = builder.entryIntro;
    this.iconUrl = builder.iconUrl;
    this.tagIconUrl = builder.tagIconUrl;
    this.backUrl = builder.backUrl;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TexasMatchInfo)) return false;
    TexasMatchInfo o = (TexasMatchInfo) other;
    return equals(actId, o.actId)
        && equals(title, o.title)
        && equals(rewardUrl, o.rewardUrl)
        && equals(entryBeginTime, o.entryBeginTime)
        && equals(entryEndTime, o.entryEndTime)
        && equals(gameBeginTime, o.gameBeginTime)
        && equals(gameEndTime, o.gameEndTime)
        && equals(prepareSeconds, o.prepareSeconds)
        && equals(resultShowSeconds, o.resultShowSeconds)
        && equals(sBlind, o.sBlind)
        && equals(minCarry, o.minCarry)
        && equals(maxCarry, o.maxCarry)
        && equals(entryFee, o.entryFee)
        && equals(content, o.content)
        && equals(members, o.members)
        && equals(entryIntro, o.entryIntro)
        && equals(iconUrl, o.iconUrl)
        && equals(tagIconUrl, o.tagIconUrl)
        && equals(backUrl, o.backUrl);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = actId != null ? actId.hashCode() : 0;
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (rewardUrl != null ? rewardUrl.hashCode() : 0);
      result = result * 37 + (entryBeginTime != null ? entryBeginTime.hashCode() : 0);
      result = result * 37 + (entryEndTime != null ? entryEndTime.hashCode() : 0);
      result = result * 37 + (gameBeginTime != null ? gameBeginTime.hashCode() : 0);
      result = result * 37 + (gameEndTime != null ? gameEndTime.hashCode() : 0);
      result = result * 37 + (prepareSeconds != null ? prepareSeconds.hashCode() : 0);
      result = result * 37 + (resultShowSeconds != null ? resultShowSeconds.hashCode() : 0);
      result = result * 37 + (sBlind != null ? sBlind.hashCode() : 0);
      result = result * 37 + (minCarry != null ? minCarry.hashCode() : 0);
      result = result * 37 + (maxCarry != null ? maxCarry.hashCode() : 0);
      result = result * 37 + (entryFee != null ? entryFee.hashCode() : 0);
      result = result * 37 + (content != null ? content.hashCode() : 0);
      result = result * 37 + (members != null ? members.hashCode() : 0);
      result = result * 37 + (entryIntro != null ? entryIntro.hashCode() : 0);
      result = result * 37 + (iconUrl != null ? iconUrl.hashCode() : 0);
      result = result * 37 + (tagIconUrl != null ? tagIconUrl.hashCode() : 0);
      result = result * 37 + (backUrl != null ? backUrl.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TexasMatchInfo> {

    public Integer actId;
    public String title;
    public String rewardUrl;
    public Long entryBeginTime;
    public Long entryEndTime;
    public Long gameBeginTime;
    public Long gameEndTime;
    public Long prepareSeconds;
    public Long resultShowSeconds;
    public Long sBlind;
    public Long minCarry;
    public Long maxCarry;
    public Long entryFee;
    public String content;
    public Integer members;
    public String entryIntro;
    public String iconUrl;
    public String tagIconUrl;
    public String backUrl;

    public Builder() {
    }

    public Builder(TexasMatchInfo message) {
      super(message);
      if (message == null) return;
      this.actId = message.actId;
      this.title = message.title;
      this.rewardUrl = message.rewardUrl;
      this.entryBeginTime = message.entryBeginTime;
      this.entryEndTime = message.entryEndTime;
      this.gameBeginTime = message.gameBeginTime;
      this.gameEndTime = message.gameEndTime;
      this.prepareSeconds = message.prepareSeconds;
      this.resultShowSeconds = message.resultShowSeconds;
      this.sBlind = message.sBlind;
      this.minCarry = message.minCarry;
      this.maxCarry = message.maxCarry;
      this.entryFee = message.entryFee;
      this.content = message.content;
      this.members = message.members;
      this.entryIntro = message.entryIntro;
      this.iconUrl = message.iconUrl;
      this.tagIconUrl = message.tagIconUrl;
      this.backUrl = message.backUrl;
    }

    public Builder actId(Integer actId) {
      this.actId = actId;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder rewardUrl(String rewardUrl) {
      this.rewardUrl = rewardUrl;
      return this;
    }

    /**
     * 奖励说明页链接
     */
    public Builder entryBeginTime(Long entryBeginTime) {
      this.entryBeginTime = entryBeginTime;
      return this;
    }

    /**
     * 报名开始时间
     */
    public Builder entryEndTime(Long entryEndTime) {
      this.entryEndTime = entryEndTime;
      return this;
    }

    /**
     * 报名结束时间
     */
    public Builder gameBeginTime(Long gameBeginTime) {
      this.gameBeginTime = gameBeginTime;
      return this;
    }

    /**
     * 游戏开始时间
     */
    public Builder gameEndTime(Long gameEndTime) {
      this.gameEndTime = gameEndTime;
      return this;
    }

    /**
     * 游戏结束时间
     */
    public Builder prepareSeconds(Long prepareSeconds) {
      this.prepareSeconds = prepareSeconds;
      return this;
    }

    /**
     * 准备时间，离gameBeginTime XXX秒开始
     */
    public Builder resultShowSeconds(Long resultShowSeconds) {
      this.resultShowSeconds = resultShowSeconds;
      return this;
    }

    /**
     * 展示时间，离gameEndTime XXX秒后结束
     */
    public Builder sBlind(Long sBlind) {
      this.sBlind = sBlind;
      return this;
    }

    /**
     * 小盲(0表示不限)
     */
    public Builder minCarry(Long minCarry) {
      this.minCarry = minCarry;
      return this;
    }

    /**
     * 最小携带
     */
    public Builder maxCarry(Long maxCarry) {
      this.maxCarry = maxCarry;
      return this;
    }

    /**
     * 最大携带
     */
    public Builder entryFee(Long entryFee) {
      this.entryFee = entryFee;
      return this;
    }

    /**
     * 报名费
     */
    public Builder content(String content) {
      this.content = content;
      return this;
    }

    /**
     * 文本说明
     */
    public Builder members(Integer members) {
      this.members = members;
      return this;
    }

    /**
     * 参与人数
     */
    public Builder entryIntro(String entryIntro) {
      this.entryIntro = entryIntro;
      return this;
    }

    /**
     * 排名说明
     */
    public Builder iconUrl(String iconUrl) {
      this.iconUrl = iconUrl;
      return this;
    }

    /**
     * 房间里的浮层按钮
     */
    public Builder tagIconUrl(String tagIconUrl) {
      this.tagIconUrl = tagIconUrl;
      return this;
    }

    /**
     * 主播带的标识
     */
    public Builder backUrl(String backUrl) {
      this.backUrl = backUrl;
      return this;
    }

    @Override
    public TexasMatchInfo build() {
      return new TexasMatchInfo(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
