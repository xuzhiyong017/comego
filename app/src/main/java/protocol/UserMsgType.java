// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum UserMsgType
    implements ProtoEnum {
  UserMsgReceived(1),
  /**
   * 收到的用户消息
   */
  UserMsgSend(2),
  /**
   * 发出的用户消息
   */
  UserMsgStartLive(5);

  private final int value;

  private UserMsgType(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int UserMsgReceived_VALUE = 1;
  /**
   * 收到的用户消息
   */
  public static final int UserMsgSend_VALUE = 2;
  /**
   * 发出的用户消息
   */
  public static final int UserMsgStartLive_VALUE = 5;

  public static UserMsgType valueOf(int value) {
    switch (value)  {
      case 1: return UserMsgReceived;
      case 2: return UserMsgSend;
      case 5: return UserMsgStartLive;
      default: return null;
    }
  }
}