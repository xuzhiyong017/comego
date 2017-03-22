// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class EventValue extends Message {

  public static final Integer DEFAULT_EVENTID = 0;
  public static final Long DEFAULT_DAYVAL = 0L;
  public static final Long DEFAULT_TOTALVAL = 0L;
  public static final Integer DEFAULT_TASKID = 0;

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer eventId;

  @ProtoField(tag = 2, type = UINT64, label = REQUIRED)
  public final Long dayVal;

  /**
   * 每日任务用这个值  签到则代表今天签到了没
   */
  @ProtoField(tag = 3, type = UINT64, label = REQUIRED)
  public final Long totalVal;

  /**
   * 永久任务或成就用这个值  签到则代表共签了几天
   */
  @ProtoField(tag = 4, type = UINT32, label = REQUIRED)
  public final Integer taskId;

  private EventValue(Builder builder) {
    this.eventId = builder.eventId;
    this.dayVal = builder.dayVal;
    this.totalVal = builder.totalVal;
    this.taskId = builder.taskId;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof EventValue)) return false;
    EventValue o = (EventValue) other;
    return equals(eventId, o.eventId)
        && equals(dayVal, o.dayVal)
        && equals(totalVal, o.totalVal)
        && equals(taskId, o.taskId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = eventId != null ? eventId.hashCode() : 0;
      result = result * 37 + (dayVal != null ? dayVal.hashCode() : 0);
      result = result * 37 + (totalVal != null ? totalVal.hashCode() : 0);
      result = result * 37 + (taskId != null ? taskId.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<EventValue> {

    public Integer eventId;
    public Long dayVal;
    public Long totalVal;
    public Integer taskId;

    public Builder() {
    }

    public Builder(EventValue message) {
      super(message);
      if (message == null) return;
      this.eventId = message.eventId;
      this.dayVal = message.dayVal;
      this.totalVal = message.totalVal;
      this.taskId = message.taskId;
    }

    public Builder eventId(Integer eventId) {
      this.eventId = eventId;
      return this;
    }

    public Builder dayVal(Long dayVal) {
      this.dayVal = dayVal;
      return this;
    }

    /**
     * 每日任务用这个值  签到则代表今天签到了没
     */
    public Builder totalVal(Long totalVal) {
      this.totalVal = totalVal;
      return this;
    }

    /**
     * 永久任务或成就用这个值  签到则代表共签了几天
     */
    public Builder taskId(Integer taskId) {
      this.taskId = taskId;
      return this;
    }

    @Override
    public EventValue build() {
      checkRequiredFields();
      return new EventValue(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}