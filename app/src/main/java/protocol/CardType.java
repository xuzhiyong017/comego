// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum CardType
    implements ProtoEnum {
  Level_High(0),
  /**
   * 高牌
   */
  Level_Pair1(1),
  /**
   * 一对
   */
  Level_Pair2(2),
  /**
   * 两对
   */
  Level_Three(3),
  /**
   * 三条
   */
  Level_Straight(4),
  /**
   * 顺子
   */
  Level_flush(5),
  /**
   * 同花
   */
  Level_Full_House(6),
  /**
   * 葫芦
   */
  Level_Four(7),
  /**
   * 四条
   */
  Level_Flush_Straight(8),
  /**
   * 同花顺
   */
  Level_Flush_Straight_Max(9);

  private final int value;

  private CardType(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int Level_High_VALUE = 0;
  /**
   * 高牌
   */
  public static final int Level_Pair1_VALUE = 1;
  /**
   * 一对
   */
  public static final int Level_Pair2_VALUE = 2;
  /**
   * 两对
   */
  public static final int Level_Three_VALUE = 3;
  /**
   * 三条
   */
  public static final int Level_Straight_VALUE = 4;
  /**
   * 顺子
   */
  public static final int Level_flush_VALUE = 5;
  /**
   * 同花
   */
  public static final int Level_Full_House_VALUE = 6;
  /**
   * 葫芦
   */
  public static final int Level_Four_VALUE = 7;
  /**
   * 四条
   */
  public static final int Level_Flush_Straight_VALUE = 8;
  /**
   * 同花顺
   */
  public static final int Level_Flush_Straight_Max_VALUE = 9;

  public static CardType valueOf(int value) {
    switch (value)  {
      case 0: return Level_High;
      case 1: return Level_Pair1;
      case 2: return Level_Pair2;
      case 3: return Level_Three;
      case 4: return Level_Straight;
      case 5: return Level_flush;
      case 6: return Level_Full_House;
      case 7: return Level_Four;
      case 8: return Level_Flush_Straight;
      case 9: return Level_Flush_Straight_Max;
      default: return null;
    }
  }
}