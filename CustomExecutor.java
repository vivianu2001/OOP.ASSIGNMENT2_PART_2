import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * A custom thread pool executor that executes tasks with priorities.
 */
public class CustomExecutor extends ThreadPoolExecutor {


    private ConcurrentHashMap<Integer, Integer> priorityMap = new ConcurrentHashMap<>();


    public static final Logger logger = Logger.getAnonymousLogger();
    private boolean autoTerminate;

    private final AtomicInteger maxPriority = new AtomicInteger(11);

    /**
     * Creates a new custom executor with the default number of threads.
     * The default number of threads is equal to the number of available processors divided by 2. The threads are stored in a priority queue that orders tasks by their priority.
     * The executor has a keep-alive time of 300 milliseconds. If a thread is idle for more than the keep-alive time, it will be terminated.
     *
     * @see Runtime#availableProcessors()
     */
    public CustomExecutor() {
        this(Runtime.getRuntime().availableProcessors() / 2, Math.max(Runtime.getRuntime().availableProcessors() - 1, 1),
                300L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());


    }

    /**
     * Creates a new custom executor with the specified number of threads and work queue.
     * The threads are stored in a priority queue that orders tasks by their priority.
     *
     * @param corePoolSize   the number of threads to keep in the pool, even if they are idle
     * @param maxiumPoolSize the maximum number of threads to allow in the pool
     * @param keepAliveTime  the maximum time that excess idle threads will wait for new tasks before terminating
     * @param unit           the time unit for the keepAliveTime argument
     * @param workQueue      the queue to use for holding tasks before they are executed. This queue will hold only the {@link Runnable} tasks submitted by the {@link #execute(Runnable)} method
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if workQueue is null
     */
    public CustomExecutor(int corePoolSize, int maxiumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maxiumPoolSize, keepAliveTime, unit, new PriorityBlockingQueue<>());


    }

    /**
     * Submits a task with a given task type to be executed by this executor and returns a Future representing the pending results of the task.
     * The task type determines the priority of the task. Tasks with higher priority are executed before tasks with lower priority.
     *
     * @param task     the task to submit
     * @param taskType the task type of the task
     * @return a Future representing the pending results of the task
     * @throws NullPointerException if the task is null
     */
    public <T> Future<T> submit(Callable<T> task, TaskType taskType) {
        if (task == null) {
            throw new NullPointerException();
        }

        Task<T> futureTask = Task.createTask(task, taskType);
        logger.info("Task Type is:" + taskType.toString());
        execute(trackMaxPriority(futureTask));
        return futureTask;
    }

    /**
     * Submits a task to be executed by this executor and returns a Future representing the pending results of the task.
     *
     * <p>
     * The priority of the task is determined by the task's task type. Tasks with higher priority are executed before tasks with lower priority.
     *
     * @param task the task to submit
     * @return a Future representing the pending results of the task
     * @throws NullPointerException if the task is null
     */
    public <T> Future<T> submit(Task<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        execute(trackMaxPriority(task));
        return task;
    }
    /**
     * Updates the maximum priority of the tasks submitted to this executor.
     * If the priority of the given task is greater than the current maximum priority, the maximum priority is updated to the task's priority.
     * @param customTask the task whose priority is used to update the maximum priority
     * @return the given task
     */


    /**
     * Returns the current maximum priority of the tasks submitted to this executor.
     *
     * @return the current maximum priority of the tasks submitted to this executor
     */
    public int getCurrentMax() {
        if (maxPriority != null) {

            return maxPriority.get();
        } else {
            return 11;
        }
    }


    protected <T> Task<T> trackMaxPriority(Task<T> customTask) {

        if (priorityMap.get(customTask.getPriority()) == null) {
            priorityMap.put(customTask.getPriority(), 1);

        } else {
            priorityMap.compute(customTask.getPriority(), (k, v) -> v + 1);

        }



        for (Map.Entry<Integer, Integer> entry : priorityMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                int key = entry.getKey();
                maxPriority.getAndUpdate(current -> Math.min(current, key));

            }
        }

        logger.info("Submitted task: '" + customTask + "', max priority: " + getCurrentMax());

        return customTask;
    }


    protected void afterExecute(Runnable r, Throwable t) {


        if (t != null && autoTerminate) {
            shutdown();
        }

        Task customTask = (Task) r;



        priorityMap.computeIfPresent(customTask.getPriority(), (k, v) -> v - 1);


        for (Map.Entry<Integer, Integer> entry : priorityMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                int key = entry.getKey();
                if (key < maxPriority.get()) {
                    maxPriority.getAndUpdate(current -> key);

                }
            }
        }

        logger.info("Completed task: '" + customTask + "', max priority: " + getCurrentMax());


    }


    /**
     * Gracefully terminates this executor by initiating an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
     */
    public void gracefullyTerminate() {
        shutdown();
    }


}


