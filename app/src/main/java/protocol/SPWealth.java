// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum SPWealth
    implements ProtoEnum {
  PUserWealthReq(0),
  PUserWealthRes(1),
  PRechargeListReq(2),
  PRechargeListRes(3),
  PRechargeReq(4),
  PRechargeRes(5),
  PRechargeFeedbackReq(6),
  PRechargeFeedbackRes(7),
  PRechargePayGateCallbackReq(8),
  PRechargePayGateCallbackRes(9),
  PBuyMChipReq(10),
  PBuyMChipRes(11),
  PMChipItemReq(12),
  PMChipItemRes(13),
  PWealthSetTestReq(14),
  PWealthSetTestRes(15),
  PWealthDrawInfoReq(16),
  PWealthDrawInfoRes(17),
  PWealthDrawBindReq(18),
  PWealthDrawBindRes(19),
  PWealthDrawMoneyReq(20),
  PWealthDrawMoneyRes(21),
  PWealthDrawRecordReq(22),
  PWealthDrawRecordRes(23),
  PLvExpConfigReq(24),
  PLvExpConfigRes(25),
  PZhimaCallbackReq(32),
  PZhimaCallbackRes(33),
  PIssueCallBackReq(34),
  PIssueCallBackRes(35),
  PGetWxOpenidReq(36),
  PGetWxOpenidRes(37),
  PMGoldChangeRecordReq(38),
  PMGoldChangeRecordRes(39),
  PWealthDrawYYBindReq(48),
  PWealthDrawYYBindRes(49),
  PWealthDrawYYBindSetReq(50),
  PWealthDrawYYBindSetRes(51),
  PWealthDrawCorpSetReq(52),
  PWealthDrawCorpSetRes(53),
  PWealthDrawCorpListReq(56),
  PWealthDrawCorpListRes(57),
  PWealthDrawManualSetReq(64),
  PWealthDrawManualSetRes(65),
  PWealthDrawManualListReq(66),
  PWealthDrawManualListRes(67),
  PWealthDrawTaxReq(68),
  PWealthDrawTaxRes(69);

  private final int value;

  private SPWealth(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int PUserWealthReq_VALUE = 0;
  public static final int PUserWealthRes_VALUE = 1;
  public static final int PRechargeListReq_VALUE = 2;
  public static final int PRechargeListRes_VALUE = 3;
  public static final int PRechargeReq_VALUE = 4;
  public static final int PRechargeRes_VALUE = 5;
  public static final int PRechargeFeedbackReq_VALUE = 6;
  public static final int PRechargeFeedbackRes_VALUE = 7;
  public static final int PRechargePayGateCallbackReq_VALUE = 8;
  public static final int PRechargePayGateCallbackRes_VALUE = 9;
  public static final int PBuyMChipReq_VALUE = 10;
  public static final int PBuyMChipRes_VALUE = 11;
  public static final int PMChipItemReq_VALUE = 12;
  public static final int PMChipItemRes_VALUE = 13;
  public static final int PWealthSetTestReq_VALUE = 14;
  public static final int PWealthSetTestRes_VALUE = 15;
  public static final int PWealthDrawInfoReq_VALUE = 16;
  public static final int PWealthDrawInfoRes_VALUE = 17;
  public static final int PWealthDrawBindReq_VALUE = 18;
  public static final int PWealthDrawBindRes_VALUE = 19;
  public static final int PWealthDrawMoneyReq_VALUE = 20;
  public static final int PWealthDrawMoneyRes_VALUE = 21;
  public static final int PWealthDrawRecordReq_VALUE = 22;
  public static final int PWealthDrawRecordRes_VALUE = 23;
  public static final int PLvExpConfigReq_VALUE = 24;
  public static final int PLvExpConfigRes_VALUE = 25;
  public static final int PZhimaCallbackReq_VALUE = 32;
  public static final int PZhimaCallbackRes_VALUE = 33;
  public static final int PIssueCallBackReq_VALUE = 34;
  public static final int PIssueCallBackRes_VALUE = 35;
  public static final int PGetWxOpenidReq_VALUE = 36;
  public static final int PGetWxOpenidRes_VALUE = 37;
  public static final int PMGoldChangeRecordReq_VALUE = 38;
  public static final int PMGoldChangeRecordRes_VALUE = 39;
  public static final int PWealthDrawYYBindReq_VALUE = 48;
  public static final int PWealthDrawYYBindRes_VALUE = 49;
  public static final int PWealthDrawYYBindSetReq_VALUE = 50;
  public static final int PWealthDrawYYBindSetRes_VALUE = 51;
  public static final int PWealthDrawCorpSetReq_VALUE = 52;
  public static final int PWealthDrawCorpSetRes_VALUE = 53;
  public static final int PWealthDrawCorpListReq_VALUE = 56;
  public static final int PWealthDrawCorpListRes_VALUE = 57;
  public static final int PWealthDrawManualSetReq_VALUE = 64;
  public static final int PWealthDrawManualSetRes_VALUE = 65;
  public static final int PWealthDrawManualListReq_VALUE = 66;
  public static final int PWealthDrawManualListRes_VALUE = 67;
  public static final int PWealthDrawTaxReq_VALUE = 68;
  public static final int PWealthDrawTaxRes_VALUE = 69;

  public static SPWealth valueOf(int value) {
    switch (value)  {
      case 0: return PUserWealthReq;
      case 1: return PUserWealthRes;
      case 2: return PRechargeListReq;
      case 3: return PRechargeListRes;
      case 4: return PRechargeReq;
      case 5: return PRechargeRes;
      case 6: return PRechargeFeedbackReq;
      case 7: return PRechargeFeedbackRes;
      case 8: return PRechargePayGateCallbackReq;
      case 9: return PRechargePayGateCallbackRes;
      case 10: return PBuyMChipReq;
      case 11: return PBuyMChipRes;
      case 12: return PMChipItemReq;
      case 13: return PMChipItemRes;
      case 14: return PWealthSetTestReq;
      case 15: return PWealthSetTestRes;
      case 16: return PWealthDrawInfoReq;
      case 17: return PWealthDrawInfoRes;
      case 18: return PWealthDrawBindReq;
      case 19: return PWealthDrawBindRes;
      case 20: return PWealthDrawMoneyReq;
      case 21: return PWealthDrawMoneyRes;
      case 22: return PWealthDrawRecordReq;
      case 23: return PWealthDrawRecordRes;
      case 24: return PLvExpConfigReq;
      case 25: return PLvExpConfigRes;
      case 32: return PZhimaCallbackReq;
      case 33: return PZhimaCallbackRes;
      case 34: return PIssueCallBackReq;
      case 35: return PIssueCallBackRes;
      case 36: return PGetWxOpenidReq;
      case 37: return PGetWxOpenidRes;
      case 38: return PMGoldChangeRecordReq;
      case 39: return PMGoldChangeRecordRes;
      case 48: return PWealthDrawYYBindReq;
      case 49: return PWealthDrawYYBindRes;
      case 50: return PWealthDrawYYBindSetReq;
      case 51: return PWealthDrawYYBindSetRes;
      case 52: return PWealthDrawCorpSetReq;
      case 53: return PWealthDrawCorpSetRes;
      case 56: return PWealthDrawCorpListReq;
      case 57: return PWealthDrawCorpListRes;
      case 64: return PWealthDrawManualSetReq;
      case 65: return PWealthDrawManualSetRes;
      case 66: return PWealthDrawManualListReq;
      case 67: return PWealthDrawManualListRes;
      case 68: return PWealthDrawTaxReq;
      case 69: return PWealthDrawTaxRes;
      default: return null;
    }
  }
}
