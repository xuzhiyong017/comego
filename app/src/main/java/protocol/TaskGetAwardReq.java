// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REPEATED;

public final class TaskGetAwardReq extends Message {

  public static final List<Integer> DEFAULT_TASKIDS = Collections.emptyList();

  @ProtoField(tag = 1, type = UINT32, label = REPEATED)
  public final List<Integer> taskIds;

  private TaskGetAwardReq(Builder builder) {
    this.taskIds = immutableCopyOf(builder.taskIds);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TaskGetAwardReq)) return false;
    return equals(taskIds, ((TaskGetAwardReq) other).taskIds);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = taskIds != null ? taskIds.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<TaskGetAwardReq> {

    public List<Integer> taskIds;

    public Builder() {
    }

    public Builder(TaskGetAwardReq message) {
      super(message);
      if (message == null) return;
      this.taskIds = copyOf(message.taskIds);
    }

    public Builder taskIds(List<Integer> taskIds) {
      this.taskIds = checkForNulls(taskIds);
      return this;
    }

    @Override
    public TaskGetAwardReq build() {
      return new TaskGetAwardReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
