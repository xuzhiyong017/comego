// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum SPGift
    implements ProtoEnum {
  PGiftListReq(0),
  PGiftListRes(1),
  PGiftDonateReq(2),
  PGiftDonateRes(3),
  PGiftRecordListReq(16),
  PGiftRecordListRes(17),
  PGiftStatListReq(18),
  PGiftStatListRes(19),
  PGrabRedBagReq(20),
  PGrabRedBagRes(21);

  private final int value;

  private SPGift(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int PGiftListReq_VALUE = 0;
  public static final int PGiftListRes_VALUE = 1;
  public static final int PGiftDonateReq_VALUE = 2;
  public static final int PGiftDonateRes_VALUE = 3;
  public static final int PGiftRecordListReq_VALUE = 16;
  public static final int PGiftRecordListRes_VALUE = 17;
  public static final int PGiftStatListReq_VALUE = 18;
  public static final int PGiftStatListRes_VALUE = 19;
  public static final int PGrabRedBagReq_VALUE = 20;
  public static final int PGrabRedBagRes_VALUE = 21;

  public static SPGift valueOf(int value) {
    switch (value)  {
      case 0: return PGiftListReq;
      case 1: return PGiftListRes;
      case 2: return PGiftDonateReq;
      case 3: return PGiftDonateRes;
      case 16: return PGiftRecordListReq;
      case 17: return PGiftRecordListRes;
      case 18: return PGiftStatListReq;
      case 19: return PGiftStatListRes;
      case 20: return PGrabRedBagReq;
      case 21: return PGrabRedBagRes;
      default: return null;
    }
  }
}
