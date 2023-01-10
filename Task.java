import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

/***
 * Extension of Future task with extra requirements
 * @param <V> generic
 */
public class Task<V> extends FutureTask<V> implements Comparable<Task<V>> {
    private final TaskType taskType;

    private static final AtomicLong counter= new AtomicLong();

    private final long instanceNumber;


    /**
     * default constructor
     *
     * @param callable
     * @param taskType
     */
    protected Task(Callable<V> callable, TaskType taskType) {
        super(callable);
        this.taskType = taskType;
        this.instanceNumber= counter.incrementAndGet();


    }

    /**
     * Constructs new task instance from Callable with priority taskType
     *
     * @param callable to compute
     * @param taskType scheduling priority
     * @param <V>
     * @return
     */
    public static <V> Task<V> create(Callable<V> callable, TaskType taskType) {
        if (callable == null) {
            throw new NullPointerException();
        }

        return new Task<V>(callable, taskType);
    }

    /**
     * Creates a new task with the given callable.
     *
     * @param callable the callable to be executed as a task
     * @return a new task
     * @throws NullPointerException if the callable is null
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

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException if the specified object's type prevents it from being compared to this object
     */
    public int compareTo(Task<V> o) {

        return Integer.compare(this.getPriority(), o.getPriority());
    }
    /**
     * Returns a string representation of this task.
     *
     * @return a string representation of this task
     */
    public String toString() {
        return String.format("%s-#%s-=priority=%d,", Task.class.getSimpleName(), instanceNumber,getPriority());
    }
    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the object to be tested for equality
     * @return true if the other object is equal to this one; false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass())
            return false;
        Task<?> task = (Task<?>) o;
        return instanceNumber== task.instanceNumber;

    }


}



