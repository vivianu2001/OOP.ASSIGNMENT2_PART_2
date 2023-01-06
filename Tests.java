
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class Tests {
    public static final Logger logger = Logger.getAnonymousLogger();


    @Test
    public void partialTest(){
        CustomExecutor customExecutor = new CustomExecutor();
        Task<Integer> task = Task.createTask(()->{
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);
        Future<Integer> sumTask = customExecutor.submit(task);
        final int sum;
        try {
            sum = sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(()-> "Sum of 1 through 10 = " + sum);
        assert (sum == 55);

        Callable<Double> callable1 = ()-> 1000 * Math.pow(1.02, 5);

        Callable<String> callable2 = ()-> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };

        Future<Double> priceTask = customExecutor.submit(callable1, TaskType.COMPUTATIONAL);
        Future<String> reverseTask = customExecutor.submit(callable2, TaskType.IO);

        Task task1 = new Task(callable1, TaskType.COMPUTATIONAL);
        Task task2 = new Task(callable2, TaskType.COMPUTATIONAL);

        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(()-> "Reversed String = " + reversed);
        logger.info(()->String.valueOf("Total Price = " + totalPrice));

        logger.info(()-> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();

    }

}
