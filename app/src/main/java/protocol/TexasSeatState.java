// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.ProtoEnum;

public enum TexasSeatState
    implements ProtoEnum {
  TexasPlayer_Ready(0),
  /**
   * 准备玩
   */
  TexasPlayer_InGame(1),
  /**
   * 开始游戏
   */
  TexasPlayer_Thinking(2),
  /**
   * 等待操作中
   */
  TexasPlayer_Check(3),
  /**
   * 过
   */
  TexasPlayer_Call(4),
  /**
   * 跟
   */
  TexasPlayer_Raise(5),
  /**
   * 加
   */
  TexasPlayer_Fold(6),
  /**
   * 弃牌
   */
  TexasPlayer_AllIn(7);

  private final int value;

  private TexasSeatState(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final int TexasPlayer_Ready_VALUE = 0;
  /**
   * 准备玩
   */
  public static final int TexasPlayer_InGame_VALUE = 1;
  /**
   * 开始游戏
   */
  public static final int TexasPlayer_Thinking_VALUE = 2;
  /**
   * 等待操作中
   */
  public static final int TexasPlayer_Check_VALUE = 3;
  /**
   * 过
   */
  public static final int TexasPlayer_Call_VALUE = 4;
  /**
   * 跟
   */
  public static final int TexasPlayer_Raise_VALUE = 5;
  /**
   * 加
   */
  public static final int TexasPlayer_Fold_VALUE = 6;
  /**
   * 弃牌
   */
  public static final int TexasPlayer_AllIn_VALUE = 7;

  public static TexasSeatState valueOf(int value) {
    switch (value)  {
      case 0: return TexasPlayer_Ready;
      case 1: return TexasPlayer_InGame;
      case 2: return TexasPlayer_Thinking;
      case 3: return TexasPlayer_Check;
      case 4: return TexasPlayer_Call;
      case 5: return TexasPlayer_Raise;
      case 6: return TexasPlayer_Fold;
      case 7: return TexasPlayer_AllIn;
      default: return null;
    }
  }
}