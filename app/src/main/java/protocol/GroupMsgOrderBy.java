// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum GroupMsgOrderBy
    implements ProtoEnum {
  GroupMsgOrderByDesc(0),
  /**
   * 降序
   */
  GroupMsgOrderByAsc(1);

  private final int value;

  private GroupMsgOrderBy(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int GroupMsgOrderByDesc_VALUE = 0;
  /**
   * 降序
   */
  public static final int GroupMsgOrderByAsc_VALUE = 1;

  public static GroupMsgOrderBy valueOf(int value) {
    switch (value)  {
      case 0: return GroupMsgOrderByDesc;
      case 1: return GroupMsgOrderByAsc;
      default: return null;
    }
  }
}
