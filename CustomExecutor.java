import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class CustomExecutor extends ThreadPoolExecutor {

    public static final Logger logger=Logger.getAnonymousLogger();


    private final AtomicInteger maxPriority=new AtomicInteger();


    public CustomExecutor()
    {
      this(Runtime.getRuntime().availableProcessors()/2,Math.max(Runtime.getRuntime().availableProcessors()-1,1),
              300L,TimeUnit.MILLISECONDS,new PriorityBlockingQueue<>());

    }


    public CustomExecutor(int corePoolSize, int maxiumPoolSize, long keepAliveTime, TimeUnit unit,BlockingQueue<Runnable> workQueue)
    {
        super(corePoolSize,maxiumPoolSize,keepAliveTime,unit,new PriorityBlockingQueue<>());

    }



    public <T> Future <T> submit(Callable <T> task,TaskType taskType)
    {
        if(task==null)
        {
            throw new NullPointerException();
        }

        Task<T> futureTask=Task.createTask(task,taskType);
        logger.info("Task Type is:"+taskType.toString());
        execute(trackMaxPriority(futureTask));
        return futureTask;
    }

    public <T> Future <T> submit(Task <T> task)
    {
        if(task==null)
        {
            throw new NullPointerException();
        }
       execute(trackMaxPriority(task));
        return task;
    }

    protected <T> Task trackMaxPriority(Task<T> customTask){

        maxPriority.getAndUpdate(maxPriority->{
            int currentTaskPriority=customTask.getPriority();
            if(currentTaskPriority>maxPriority){
                return currentTaskPriority;
            }
            return maxPriority;
        });
        return customTask;
    }



public int getCurrentMax()
{
    return maxPriority.get();

}

public void gracefullyTerminate()
{
    shutdown();
}




}


