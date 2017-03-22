// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum SPUser
    implements ProtoEnum {
  PUserInfoReq(0),
  PUserInfoRes(1),
  PUserInfoListReq(2),
  PUserInfoListRes(3),
  PUserInfoModifyReq(4),
  PUserInfoModifyRes(5),
  PUserPropReq(6),
  PUserPropRes(7),
  PUserPasswordModifyReq(10),
  PUserPasswordModifyRes(11),
  PUserSearchReq(16),
  PUserSearchRes(17),
  PUserInviteInfoReq(18),
  /**
   * 邀请信息
   */
  PUserInviteInfoRes(19),
  PUserInviteUserReq(20),
  /**
   * 我邀请的人
   */
  PUserInviteUserRes(21);

  private final int value;

  private SPUser(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int PUserInfoReq_VALUE = 0;
  public static final int PUserInfoRes_VALUE = 1;
  public static final int PUserInfoListReq_VALUE = 2;
  public static final int PUserInfoListRes_VALUE = 3;
  public static final int PUserInfoModifyReq_VALUE = 4;
  public static final int PUserInfoModifyRes_VALUE = 5;
  public static final int PUserPropReq_VALUE = 6;
  public static final int PUserPropRes_VALUE = 7;
  public static final int PUserPasswordModifyReq_VALUE = 10;
  public static final int PUserPasswordModifyRes_VALUE = 11;
  public static final int PUserSearchReq_VALUE = 16;
  public static final int PUserSearchRes_VALUE = 17;
  public static final int PUserInviteInfoReq_VALUE = 18;
  /**
   * 邀请信息
   */
  public static final int PUserInviteInfoRes_VALUE = 19;
  public static final int PUserInviteUserReq_VALUE = 20;
  /**
   * 我邀请的人
   */
  public static final int PUserInviteUserRes_VALUE = 21;

  public static SPUser valueOf(int value) {
    switch (value)  {
      case 0: return PUserInfoReq;
      case 1: return PUserInfoRes;
      case 2: return PUserInfoListReq;
      case 3: return PUserInfoListRes;
      case 4: return PUserInfoModifyReq;
      case 5: return PUserInfoModifyRes;
      case 6: return PUserPropReq;
      case 7: return PUserPropRes;
      case 10: return PUserPasswordModifyReq;
      case 11: return PUserPasswordModifyRes;
      case 16: return PUserSearchReq;
      case 17: return PUserSearchRes;
      case 18: return PUserInviteInfoReq;
      case 19: return PUserInviteInfoRes;
      case 20: return PUserInviteUserReq;
      case 21: return PUserInviteUserRes;
      default: return null;
    }
  }
}