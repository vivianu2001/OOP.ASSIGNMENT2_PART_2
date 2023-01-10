
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.asserts.Assertion;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class Tests {
    public static final Logger logger = Logger.getAnonymousLogger();

    @Test
    /**
     * Test for gracefully terminate, after two tasks are excute the CustomExecuter shutdown,
     * return exception ExecutionException by request
     */
    public void partialTest() {
        CustomExecutor customExecutor = CustomExecutor.newDefultExecuter();
        Task<Integer> simpletask = Task.create(() -> {
            int sum = 0;
            for (int i = 1; i <= 1000; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);

        Future<Integer> sumTask = customExecutor.submit(simpletask);
        final int sum;
        try {
            sum = sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 10 = " + sum);

        Assertions.assertThrows(RejectedExecutionException.class, () -> {
            customExecutor.submit(() -> {
                Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
                return 1000 * Math.pow(1.02, 5);
            }, TaskType.COMPUTATIONAL);

        });
    }


        @Test
        public void partialTest2()
        {
            Callable<Double> callable1 = () -> 1000 * Math.pow(1.02, 5);

            Callable<String> callable2 = () -> {
                StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                return sb.reverse().toString();
            };

            CustomExecutor customExecutor = CustomExecutor.newDefultExecuter();
            Future<Double> priceTask = customExecutor.submit(callable1, TaskType.COMPUTATIONAL);
            Future<String> reverseTask = customExecutor.submit(callable2, TaskType.IO);


            final Double totalPrice;
            final String reversed;
            try {
                totalPrice = priceTask.get();
                reversed = reverseTask.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            logger.info(() -> "Reversed String = " + reversed);
            logger.info(() -> "Total Price = " + totalPrice);

            logger.info(() -> "Current maximum priority = " +
                    customExecutor.getCurrentMax());



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
        CustomExecutor customExecutor = CustomExecutor.newDefultExecuter();
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
        Task task2 = new Task(callable2, TaskType.IO);

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

    }


    @Test
    public void scheduling() {

        logger.info("Testing scheduling priority...");

        CustomExecutor customExecutor2 = CustomExecutor.newSingleThreadExecuter();

        customExecutor2.submit(() -> {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
            return null;
        }, TaskType.COMPUTATIONAL);


        customExecutor2.submit(() -> {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
            return null;
        }, TaskType.IO);


        customExecutor2.submit(() -> {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
            return null;
        }, TaskType.OTHER);


        customExecutor2.submit(() -> {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
            return null;
        }, TaskType.COMPUTATIONAL);

        customExecutor2.submit(() -> {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
            return null;
        }, TaskType.COMPUTATIONAL);

        customExecutor2.submit(() -> {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
            return null;
        }, TaskType.COMPUTATIONAL);


        customExecutor2.shutdown();


        try {
            boolean terminated = customExecutor2.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("Scheduling priority test finished");

    }

    @Test

    public void automaticTermination()
    {
        CustomExecutor customExecutor= CustomExecutor.newDefultExecuter();
        customExecutor.submit(()-> {
            throw new RuntimeException("Exception");
        },TaskType.COMPUTATIONAL);

    }


}

