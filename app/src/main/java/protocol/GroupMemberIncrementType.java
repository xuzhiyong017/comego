// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum GroupMemberIncrementType
    implements ProtoEnum {
  GroupMemberNone(0),
  GroupMemberAdd(1),
  GroupMemberUpdate(2),
  GroupMemberDestroy(3),
  GroupMemberKick(4),
  GroupMemberQuit(5),
  GroupMemberRolerListUpdate(6);

  private final int value;

  private GroupMemberIncrementType(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int GroupMemberNone_VALUE = 0;
  public static final int GroupMemberAdd_VALUE = 1;
  public static final int GroupMemberUpdate_VALUE = 2;
  public static final int GroupMemberDestroy_VALUE = 3;
  public static final int GroupMemberKick_VALUE = 4;
  public static final int GroupMemberQuit_VALUE = 5;
  public static final int GroupMemberRolerListUpdate_VALUE = 6;

  public static GroupMemberIncrementType valueOf(int value) {
    switch (value)  {
      case 0: return GroupMemberNone;
      case 1: return GroupMemberAdd;
      case 2: return GroupMemberUpdate;
      case 3: return GroupMemberDestroy;
      case 4: return GroupMemberKick;
      case 5: return GroupMemberQuit;
      case 6: return GroupMemberRolerListUpdate;
      default: return null;
    }
  }
}
