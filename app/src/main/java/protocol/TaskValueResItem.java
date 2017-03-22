// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Label.REPEATED;

public final class TaskValueResItem extends Message {

  public static final TaskClass DEFAULT_TASKCLASS = TaskClass.Task;
  public static final List<EventValue> DEFAULT_VALUES = Collections.emptyList();
  public static final List<TypeConfig> DEFAULT_CONFIGS = Collections.emptyList();
  public static final Long DEFAULT_TS = 0L;

  @ProtoField(tag = 1, type = ENUM)
  public final TaskClass taskClass;

  /**
   * 分类
   */
  @ProtoField(tag = 2, label = REPEATED)
  public final List<EventValue> values;

  /**
   * 用户对应的值  动态
   */
  @ProtoField(tag = 3, label = REPEATED)
  public final List<TypeConfig> configs;

  /**
   * 配置数据  要作缓存
   */
  @ProtoField(tag = 4, type = INT64)
  public final Long ts;

  private TaskValueResItem(Builder builder) {
    this.taskClass = builder.taskClass;
    this.values = immutableCopyOf(builder.values);
    this.configs = immutableCopyOf(builder.configs);
    this.ts = builder.ts;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TaskValueResItem)) return false;
    TaskValueResItem o = (TaskValueResItem) other;
    return equals(taskClass, o.taskClass)
        && equals(values, o.values)
        && equals(configs, o.configs)
        && equals(ts, o.ts);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = taskClass != null ? taskClass.hashCode() : 0;
      result = result * 37 + (values != null ? values.hashCode() : 1);
      result = result * 37 + (configs != null ? configs.hashCode() : 1);
      result = result * 37 + (ts != null ? ts.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TaskValueResItem> {

    public TaskClass taskClass;
    public List<EventValue> values;
    public List<TypeConfig> configs;
    public Long ts;

    public Builder() {
    }

    public Builder(TaskValueResItem message) {
      super(message);
      if (message == null) return;
      this.taskClass = message.taskClass;
      this.values = copyOf(message.values);
      this.configs = copyOf(message.configs);
      this.ts = message.ts;
    }

    public Builder taskClass(TaskClass taskClass) {
      this.taskClass = taskClass;
      return this;
    }

    /**
     * 分类
     */
    public Builder values(List<EventValue> values) {
      this.values = checkForNulls(values);
      return this;
    }

    /**
     * 用户对应的值  动态
     */
    public Builder configs(List<TypeConfig> configs) {
      this.configs = checkForNulls(configs);
      return this;
    }

    /**
     * 配置数据  要作缓存
     */
    public Builder ts(Long ts) {
      this.ts = ts;
      return this;
    }

    @Override
    public TaskValueResItem build() {
      return new TaskValueResItem(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}