// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum SPPush
    implements ProtoEnum {
  PPushPack(0),
  PForceLogout(1);

  private final int value;

  private SPPush(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int PPushPack_VALUE = 0;
  public static final int PForceLogout_VALUE = 1;

  public static SPPush valueOf(int value) {
    switch (value)  {
      case 0: return PPushPack;
      case 1: return PForceLogout;
      default: return null;
    }
  }
}
