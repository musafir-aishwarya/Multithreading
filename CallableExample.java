import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallableExample {

    // Simulate a bank transaction
    static class BankTransaction implements Callable<String> {
        private final String transactionId;
        private final double amount;

        public BankTransaction(String transactionId, double amount) {
            this.transactionId = transactionId;
            this.amount = amount;
        }

        @Override
        public String call() throws Exception {
            System.out.println("Processing transaction: " + transactionId + " for amount: $" + amount);
            // Simulate processing time
            Thread.sleep(2000);

            // Simulate success or failure randomly
            boolean success = Math.random() > 0.2;
            if (success) {
                return "Transaction " + transactionId + " completed successfully for amount $" + amount;
            } else {
                return "Transaction " + transactionId + " failed for amount $" + amount;
            }
        }
    }

    public static void main(String[] args) {
        // Create an ExecutorService with a fixed thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        try {
            // Submit multiple bank transactions
            Future<String> future1 = executorService.submit(new BankTransaction("TXN001", 100.50));
            Future<String> future2 = executorService.submit(new BankTransaction("TXN002", 250.75));
            Future<String> future3 = executorService.submit(new BankTransaction("TXN003", 400.00));

            // Wait for results and print them
            System.out.println("Result: " + future1.get());
            System.out.println("Result: " + future2.get());
            System.out.println("Result: " + future3.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Gracefully shutdown the executor
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
            System.out.println("Executor service shut down.");
        }
    }
}
