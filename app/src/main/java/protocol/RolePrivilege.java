// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum RolePrivilege
    implements ProtoEnum {
  Privilege_UserSort(64),
  /**
   * 分类、排序
   */
  Privilege_Ad(1024);

  private final int value;

  private RolePrivilege(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int Privilege_UserSort_VALUE = 64;
  /**
   * 分类、排序
   */
  public static final int Privilege_Ad_VALUE = 1024;

  public static RolePrivilege valueOf(int value) {
    switch (value)  {
      case 64: return Privilege_UserSort;
      case 1024: return Privilege_Ad;
      default: return null;
    }
  }
}
