// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class TypeConfig extends Message {

  public static final Integer DEFAULT_EVENTID = 0;
  public static final List<TaskConfig> DEFAULT_TASKIDS = Collections.emptyList();
  public static final TaskClass DEFAULT_TASKCLASS = TaskClass.Task;
  public static final TaskReset DEFAULT_RESETTYPE = TaskReset.Forever;
  public static final Integer DEFAULT_AWARDTYPE = 0;
  public static final ValId DEFAULT_VALID = ValId.Sign;

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer eventId;

  @ProtoField(tag = 2, label = REPEATED)
  public final List<TaskConfig> taskIds;

  @ProtoField(tag = 3, type = ENUM, label = REQUIRED)
  public final TaskClass taskClass;

  @ProtoField(tag = 4, type = ENUM)
  public final TaskReset resetType;

  /**
   * 0不重置1每日重置2一周重置
   */
  @ProtoField(tag = 5, type = UINT32)
  public final Integer awardType;

  /**
   * 0val=TaskArgs,1val>=TaskArgs,2val>=TaskArgs领取后val重置
   */
  @ProtoField(tag = 6, type = ENUM)
  public final ValId valId;

  private TypeConfig(Builder builder) {
    this.eventId = builder.eventId;
    this.taskIds = immutableCopyOf(builder.taskIds);
    this.taskClass = builder.taskClass;
    this.resetType = builder.resetType;
    this.awardType = builder.awardType;
    this.valId = builder.valId;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TypeConfig)) return false;
    TypeConfig o = (TypeConfig) other;
    return equals(eventId, o.eventId)
        && equals(taskIds, o.taskIds)
        && equals(taskClass, o.taskClass)
        && equals(resetType, o.resetType)
        && equals(awardType, o.awardType)
        && equals(valId, o.valId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = eventId != null ? eventId.hashCode() : 0;
      result = result * 37 + (taskIds != null ? taskIds.hashCode() : 1);
      result = result * 37 + (taskClass != null ? taskClass.hashCode() : 0);
      result = result * 37 + (resetType != null ? resetType.hashCode() : 0);
      result = result * 37 + (awardType != null ? awardType.hashCode() : 0);
      result = result * 37 + (valId != null ? valId.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TypeConfig> {

    public Integer eventId;
    public List<TaskConfig> taskIds;
    public TaskClass taskClass;
    public TaskReset resetType;
    public Integer awardType;
    public ValId valId;

    public Builder() {
    }

    public Builder(TypeConfig message) {
      super(message);
      if (message == null) return;
      this.eventId = message.eventId;
      this.taskIds = copyOf(message.taskIds);
      this.taskClass = message.taskClass;
      this.resetType = message.resetType;
      this.awardType = message.awardType;
      this.valId = message.valId;
    }

    public Builder eventId(Integer eventId) {
      this.eventId = eventId;
      return this;
    }

    public Builder taskIds(List<TaskConfig> taskIds) {
      this.taskIds = checkForNulls(taskIds);
      return this;
    }

    public Builder taskClass(TaskClass taskClass) {
      this.taskClass = taskClass;
      return this;
    }

    public Builder resetType(TaskReset resetType) {
      this.resetType = resetType;
      return this;
    }

    /**
     * 0不重置1每日重置2一周重置
     */
    public Builder awardType(Integer awardType) {
      this.awardType = awardType;
      return this;
    }

    /**
     * 0val=TaskArgs,1val>=TaskArgs,2val>=TaskArgs领取后val重置
     */
    public Builder valId(ValId valId) {
      this.valId = valId;
      return this;
    }

    @Override
    public TypeConfig build() {
      checkRequiredFields();
      return new TypeConfig(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
