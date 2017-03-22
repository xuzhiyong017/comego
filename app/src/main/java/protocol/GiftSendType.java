// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum GiftSendType
    implements ProtoEnum {
  NoType(0),
  Continue(1),
  Effect(2);

  private final int value;

  private GiftSendType(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int NoType_VALUE = 0;
  public static final int Continue_VALUE = 1;
  public static final int Effect_VALUE = 2;

  public static GiftSendType valueOf(int value) {
    switch (value)  {
      case 0: return NoType;
      case 1: return Continue;
      case 2: return Effect;
      default: return null;
    }
  }
}