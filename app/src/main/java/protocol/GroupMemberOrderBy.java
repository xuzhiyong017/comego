// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum GroupMemberOrderBy
    implements ProtoEnum {
  GroupMemberOrderByDesc(0),
  /**
   * 降序
   */
  GroupMemberOrderByAsc(1);

  private final int value;

  private GroupMemberOrderBy(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int GroupMemberOrderByDesc_VALUE = 0;
  /**
   * 降序
   */
  public static final int GroupMemberOrderByAsc_VALUE = 1;

  public static GroupMemberOrderBy valueOf(int value) {
    switch (value)  {
      case 0: return GroupMemberOrderByDesc;
      case 1: return GroupMemberOrderByAsc;
      default: return null;
    }
  }
}
