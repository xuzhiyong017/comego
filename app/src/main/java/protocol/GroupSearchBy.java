// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum GroupSearchBy
    implements ProtoEnum {
  GroupSearchByUnknown(0),
  GroupSearchByKeyword(1),
  /**
   * 同时支持DGid和Keyword
   */
  GroupSearchByLivingRecommed(2);

  private final int value;

  private GroupSearchBy(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int GroupSearchByUnknown_VALUE = 0;
  public static final int GroupSearchByKeyword_VALUE = 1;
  /**
   * 同时支持DGid和Keyword
   */
  public static final int GroupSearchByLivingRecommed_VALUE = 2;

  public static GroupSearchBy valueOf(int value) {
    switch (value)  {
      case 0: return GroupSearchByUnknown;
      case 1: return GroupSearchByKeyword;
      case 2: return GroupSearchByLivingRecommed;
      default: return null;
    }
  }
}
