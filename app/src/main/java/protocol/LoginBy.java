// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum LoginBy
    implements ProtoEnum {
  LoginBy_Mac(0),
  LoginBy_YY(1),
  LoginBy_MobilePhone(2),
  LoginBy_QQ(3),
  LoginBy_WeiXin(4),
  LoginBy_Sina(5),
  LoginBy_Robot(9),
  LoginBy_Cookie(11),
  LoginBy_Uid(12);

  private final int value;

  private LoginBy(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int LoginBy_Mac_VALUE = 0;
  public static final int LoginBy_YY_VALUE = 1;
  public static final int LoginBy_MobilePhone_VALUE = 2;
  public static final int LoginBy_QQ_VALUE = 3;
  public static final int LoginBy_WeiXin_VALUE = 4;
  public static final int LoginBy_Sina_VALUE = 5;
  public static final int LoginBy_Robot_VALUE = 9;
  public static final int LoginBy_Cookie_VALUE = 11;
  public static final int LoginBy_Uid_VALUE = 12;

  public static LoginBy valueOf(int value) {
    switch (value)  {
      case 0: return LoginBy_Mac;
      case 1: return LoginBy_YY;
      case 2: return LoginBy_MobilePhone;
      case 3: return LoginBy_QQ;
      case 4: return LoginBy_WeiXin;
      case 5: return LoginBy_Sina;
      case 9: return LoginBy_Robot;
      case 11: return LoginBy_Cookie;
      case 12: return LoginBy_Uid;
      default: return null;
    }
  }
}
