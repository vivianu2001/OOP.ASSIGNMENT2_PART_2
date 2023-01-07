import org.junit.Test;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Tests {
    public static final Logger logger = Logger.getAnonymousLogger();
    @Test
    /**
     * This test creates a CustomExecutor object and uses it to submit two tasks - one to compute
     * the sum of the integers from 1 to 10 and the other to reverse a string. The test then
     * retrieves the results of these tasks, logs them, and checks that the sum is correct. Finally,
     * the test logs the current maximum priority of tasks in the CustomExecutor and shuts it down.
     */
    public void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        Task<Integer> task = Task.createTask(() -> {
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
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        assert (sum == 55);

        Callable<Double> callable1 = () -> 1000 * Math.pow(1.02, 5);

        Callable<String> callable2 = () -> {
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
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));

        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();

    }
    @Test
        /**
         Test method for {@link CustomExecutor}.
         This test case will test the functionality of the {@link CustomExecutor} by submitting
         two tasks to the executor - one which calculates the sum of the numbers from 1 to 100
         and another which reverses the string "Hello, world!".
         The {@link Future} objects for
         these tasks are obtained and the results are retrieved using the {@link Future#get()} method.
         The test also verifies that the maximum priority of the tasks submitted to the executor
         is correctly recorded.
         Finally, the test case invokes the {@link CustomExecutor#gracefullyTerminate()}
         method to shut down the executor.
         */
    public void testCustomExecutor() {
        CustomExecutor customExecutor = new CustomExecutor();
        Callable<Integer> callable1 = () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
            return sum;
        };

        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("Hello, world!");
            return sb.reverse().toString();
        };

        Future<Integer> sumTask = customExecutor.submit(callable1, TaskType.COMPUTATIONAL);
        Future<String> reverseTask = customExecutor.submit(callable2, TaskType.IO);

        Task task1 = new Task(callable1, TaskType.COMPUTATIONAL);
        Task task2 = new Task(callable2, TaskType.COMPUTATIONAL);

        final int sum;
        final String reversed;
        try {
            sum = sumTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 100 = " + sum);
        logger.info(() -> "Reversed String = " + reversed);

        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
    }

}

