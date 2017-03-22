// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class GroupJoinRes extends Message {

  public static final String DEFAULT_LIVETOKEN = "";
  public static final Integer DEFAULT_SEAT = 0;
  public static final List<TexasSeatInfo> DEFAULT_SEATINFO = Collections.emptyList();
  public static final Integer DEFAULT_MEMBERS = 0;
  public static final Long DEFAULT_WINBETS = 0L;
  public static final Integer DEFAULT_CANSPEAK = 0;
  public static final Integer DEFAULT_ENCODE = 2;

  @ProtoField(tag = 1)
  public final GroupInfo groupInfo;

  @ProtoField(tag = 2)
  public final GroupMember member;

  @ProtoField(tag = 3, type = STRING)
  public final String liveToken;

  @ProtoField(tag = 4, type = UINT32)
  public final Integer seat;

  /**
   * 座位号
   */
  @ProtoField(tag = 5, label = REPEATED)
  public final List<TexasSeatInfo> seatInfo;

  /**
   * 德州座位状态信息
   */
  @ProtoField(tag = 6)
  public final TexasGameInfo gameInfo;

  /**
   * 德州游戏状态信息
   */
  @ProtoField(tag = 7, type = UINT32)
  public final Integer members;

  /**
   * 观看过的人数
   */
  @ProtoField(tag = 8, type = INT64)
  public final Long winBets;

  /**
   * 在这场直播中赢的筹码
   */
  @ProtoField(tag = 9, type = UINT32)
  public final Integer canSpeak;

  /**
   * 房主是否开启说话
   */
  @ProtoField(tag = 10, type = UINT32)
  public final Integer encode;

  private GroupJoinRes(Builder builder) {
    this.groupInfo = builder.groupInfo;
    this.member = builder.member;
    this.liveToken = builder.liveToken;
    this.seat = builder.seat;
    this.seatInfo = immutableCopyOf(builder.seatInfo);
    this.gameInfo = builder.gameInfo;
    this.members = builder.members;
    this.winBets = builder.winBets;
    this.canSpeak = builder.canSpeak;
    this.encode = builder.encode;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupJoinRes)) return false;
    GroupJoinRes o = (GroupJoinRes) other;
    return equals(groupInfo, o.groupInfo)
        && equals(member, o.member)
        && equals(liveToken, o.liveToken)
        && equals(seat, o.seat)
        && equals(seatInfo, o.seatInfo)
        && equals(gameInfo, o.gameInfo)
        && equals(members, o.members)
        && equals(winBets, o.winBets)
        && equals(canSpeak, o.canSpeak)
        && equals(encode, o.encode);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = groupInfo != null ? groupInfo.hashCode() : 0;
      result = result * 37 + (member != null ? member.hashCode() : 0);
      result = result * 37 + (liveToken != null ? liveToken.hashCode() : 0);
      result = result * 37 + (seat != null ? seat.hashCode() : 0);
      result = result * 37 + (seatInfo != null ? seatInfo.hashCode() : 1);
      result = result * 37 + (gameInfo != null ? gameInfo.hashCode() : 0);
      result = result * 37 + (members != null ? members.hashCode() : 0);
      result = result * 37 + (winBets != null ? winBets.hashCode() : 0);
      result = result * 37 + (canSpeak != null ? canSpeak.hashCode() : 0);
      result = result * 37 + (encode != null ? encode.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GroupJoinRes> {

    public GroupInfo groupInfo;
    public GroupMember member;
    public String liveToken;
    public Integer seat;
    public List<TexasSeatInfo> seatInfo;
    public TexasGameInfo gameInfo;
    public Integer members;
    public Long winBets;
    public Integer canSpeak;
    public Integer encode;

    public Builder() {
    }

    public Builder(GroupJoinRes message) {
      super(message);
      if (message == null) return;
      this.groupInfo = message.groupInfo;
      this.member = message.member;
      this.liveToken = message.liveToken;
      this.seat = message.seat;
      this.seatInfo = copyOf(message.seatInfo);
      this.gameInfo = message.gameInfo;
      this.members = message.members;
      this.winBets = message.winBets;
      this.canSpeak = message.canSpeak;
      this.encode = message.encode;
    }

    public Builder groupInfo(GroupInfo groupInfo) {
      this.groupInfo = groupInfo;
      return this;
    }

    public Builder member(GroupMember member) {
      this.member = member;
      return this;
    }

    public Builder liveToken(String liveToken) {
      this.liveToken = liveToken;
      return this;
    }

    public Builder seat(Integer seat) {
      this.seat = seat;
      return this;
    }

    /**
     * 座位号
     */
    public Builder seatInfo(List<TexasSeatInfo> seatInfo) {
      this.seatInfo = checkForNulls(seatInfo);
      return this;
    }

    /**
     * 德州座位状态信息
     */
    public Builder gameInfo(TexasGameInfo gameInfo) {
      this.gameInfo = gameInfo;
      return this;
    }

    /**
     * 德州游戏状态信息
     */
    public Builder members(Integer members) {
      this.members = members;
      return this;
    }

    /**
     * 观看过的人数
     */
    public Builder winBets(Long winBets) {
      this.winBets = winBets;
      return this;
    }

    /**
     * 在这场直播中赢的筹码
     */
    public Builder canSpeak(Integer canSpeak) {
      this.canSpeak = canSpeak;
      return this;
    }

    /**
     * 房主是否开启说话
     */
    public Builder encode(Integer encode) {
      this.encode = encode;
      return this;
    }

    @Override
    public GroupJoinRes build() {
      return new GroupJoinRes(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}