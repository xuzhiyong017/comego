// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum SPReport
    implements ProtoEnum {
  PReportViolatorReq(1),
  PReportViolatorRes(2),
  PReportMeBySdkReq(3),
  PReportMeBySdkRes(4),
  PMmsReportCmdReq(5),
  PMmsReportCmdRes(6),
  PUserForbidReq(16),
  PUserForbidRes(17),
  PReportLogReq(32),
  PReportLogRes(33),
  PReportLogSetReq(34),
  PReportLogSetRes(35),
  PReportLogListReq(36),
  PReportLogListRes(37),
  PReportShareReq(48),
  PReportShareRes(49);

  private final int value;

  private SPReport(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int PReportViolatorReq_VALUE = 1;
  public static final int PReportViolatorRes_VALUE = 2;
  public static final int PReportMeBySdkReq_VALUE = 3;
  public static final int PReportMeBySdkRes_VALUE = 4;
  public static final int PMmsReportCmdReq_VALUE = 5;
  public static final int PMmsReportCmdRes_VALUE = 6;
  public static final int PUserForbidReq_VALUE = 16;
  public static final int PUserForbidRes_VALUE = 17;
  public static final int PReportLogReq_VALUE = 32;
  public static final int PReportLogRes_VALUE = 33;
  public static final int PReportLogSetReq_VALUE = 34;
  public static final int PReportLogSetRes_VALUE = 35;
  public static final int PReportLogListReq_VALUE = 36;
  public static final int PReportLogListRes_VALUE = 37;
  public static final int PReportShareReq_VALUE = 48;
  public static final int PReportShareRes_VALUE = 49;

  public static SPReport valueOf(int value) {
    switch (value)  {
      case 1: return PReportViolatorReq;
      case 2: return PReportViolatorRes;
      case 3: return PReportMeBySdkReq;
      case 4: return PReportMeBySdkRes;
      case 5: return PMmsReportCmdReq;
      case 6: return PMmsReportCmdRes;
      case 16: return PUserForbidReq;
      case 17: return PUserForbidRes;
      case 32: return PReportLogReq;
      case 33: return PReportLogRes;
      case 34: return PReportLogSetReq;
      case 35: return PReportLogSetRes;
      case 36: return PReportLogListReq;
      case 37: return PReportLogListRes;
      case 48: return PReportShareReq;
      case 49: return PReportShareRes;
      default: return null;
    }
  }
}
