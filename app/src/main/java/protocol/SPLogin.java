// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum SPLogin
    implements ProtoEnum {
  PUserLoginReq(0),
  PUserLoginRes(1),
  PUserActivateReq(2),
  PUserActivateRes(3),
  PUserHeartBeatReq(4),
  PUserHeartBeatRes(5),
  PUserTokenReq(6),
  /**
   * 登录前取Token
   */
  PUserTokenRes(7),
  PUserLogoutReq(8),
  PUserLogoutRes(9),
  PSessionResumeReq(16),
  PSessionResumeRes(17),
  PSessionSuspendReq(18),
  PSessionSuspendRes(19),
  PUserLoginVerifyReq(20),
  PUserLoginVerifyRes(21),
  PUserLoginedTokenReq(22),
  /**
   * 登录后发送验证码
   */
  PUserLoginedTokenRes(23),
  PUptokenReq(48),
  PUptokenRes(49),
  PUserPushTokenRegisterReq(50),
  PUserPushTokenRegisterRes(51),
  PUserWebLoginReq(52),
  PUserWebLoginRes(53);

  private final int value;

  private SPLogin(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int PUserLoginReq_VALUE = 0;
  public static final int PUserLoginRes_VALUE = 1;
  public static final int PUserActivateReq_VALUE = 2;
  public static final int PUserActivateRes_VALUE = 3;
  public static final int PUserHeartBeatReq_VALUE = 4;
  public static final int PUserHeartBeatRes_VALUE = 5;
  public static final int PUserTokenReq_VALUE = 6;
  /**
   * 登录前取Token
   */
  public static final int PUserTokenRes_VALUE = 7;
  public static final int PUserLogoutReq_VALUE = 8;
  public static final int PUserLogoutRes_VALUE = 9;
  public static final int PSessionResumeReq_VALUE = 16;
  public static final int PSessionResumeRes_VALUE = 17;
  public static final int PSessionSuspendReq_VALUE = 18;
  public static final int PSessionSuspendRes_VALUE = 19;
  public static final int PUserLoginVerifyReq_VALUE = 20;
  public static final int PUserLoginVerifyRes_VALUE = 21;
  public static final int PUserLoginedTokenReq_VALUE = 22;
  /**
   * 登录后发送验证码
   */
  public static final int PUserLoginedTokenRes_VALUE = 23;
  public static final int PUptokenReq_VALUE = 48;
  public static final int PUptokenRes_VALUE = 49;
  public static final int PUserPushTokenRegisterReq_VALUE = 50;
  public static final int PUserPushTokenRegisterRes_VALUE = 51;
  public static final int PUserWebLoginReq_VALUE = 52;
  public static final int PUserWebLoginRes_VALUE = 53;

  public static SPLogin valueOf(int value) {
    switch (value)  {
      case 0: return PUserLoginReq;
      case 1: return PUserLoginRes;
      case 2: return PUserActivateReq;
      case 3: return PUserActivateRes;
      case 4: return PUserHeartBeatReq;
      case 5: return PUserHeartBeatRes;
      case 6: return PUserTokenReq;
      case 7: return PUserTokenRes;
      case 8: return PUserLogoutReq;
      case 9: return PUserLogoutRes;
      case 16: return PSessionResumeReq;
      case 17: return PSessionResumeRes;
      case 18: return PSessionSuspendReq;
      case 19: return PSessionSuspendRes;
      case 20: return PUserLoginVerifyReq;
      case 21: return PUserLoginVerifyRes;
      case 22: return PUserLoginedTokenReq;
      case 23: return PUserLoginedTokenRes;
      case 48: return PUptokenReq;
      case 49: return PUptokenRes;
      case 50: return PUserPushTokenRegisterReq;
      case 51: return PUserPushTokenRegisterRes;
      case 52: return PUserWebLoginReq;
      case 53: return PUserWebLoginRes;
      default: return null;
    }
  }
}
