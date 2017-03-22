// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum RoleType
    implements ProtoEnum {
  RoleType_Normal(0),
  /**
   * 普通用户（客户端显示）
   */
  RoleType_Broadcast(8),
  /**
   * 系统广播（公众号，客户端显示）
   */
  RoleType_GroupAdmin(512),
  RoleType_SysAdmin(2048);

  private final int value;

  private RoleType(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int RoleType_Normal_VALUE = 0;
  /**
   * 普通用户（客户端显示）
   */
  public static final int RoleType_Broadcast_VALUE = 8;
  /**
   * 系统广播（公众号，客户端显示）
   */
  public static final int RoleType_GroupAdmin_VALUE = 512;
  public static final int RoleType_SysAdmin_VALUE = 2048;

  public static RoleType valueOf(int value) {
    switch (value)  {
      case 0: return RoleType_Normal;
      case 8: return RoleType_Broadcast;
      case 512: return RoleType_GroupAdmin;
      case 2048: return RoleType_SysAdmin;
      default: return null;
    }
  }
}
