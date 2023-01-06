import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/***
 * Extension of Future task with extra requirements
 * @param <V> generic
 */
public class Task<V> extends FutureTask<V> implements Comparable<Task<V>> {
    private final TaskType taskType;


    /**
     * default constructor
     *
     * @param callable
     * @param taskType
     */
    protected Task(Callable<V> callable, TaskType taskType) {
        super(callable);
        this.taskType = taskType;


    }

    /**
     * Constructs new task instance from Callable with priority taskType
     *
     * @param callable to compute
     * @param taskType scheduling priority
     * @param <V>
     * @return
     */
    public static <V> Task<V> createTask(Callable<V> callable, TaskType taskType) {
        if (callable == null) {
            throw new NullPointerException();
        }

        return new Task<V>(callable, taskType);
    }

    /**
     * Constructs new task instance with lowes priority
     *
     * @param callable
     * @param <V>
     * @return
     */

    public static <V> Task<V> create(Callable<V> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }

        return new Task<V>(callable, null);
    }

    public int getPriority() {
        if (taskType != null) {
            return taskType.getPriorityValue();
        }
        return 9;
    }


    public int compareTo(Task<V> o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }

    public String toString() {
        return String.format("%s-=priority-%d,", Task.class.getSimpleName(), getPriority());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass())
            return false;
        Task<?> task = (Task<?>) o;
        return taskType == task.taskType;

    }


}



