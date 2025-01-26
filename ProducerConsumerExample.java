import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumerExample {

    // Shared warehouse class
    static class Warehouse {
        private final LinkedList<String> inventory = new LinkedList<>();
        private final int capacity = 5; // Max capacity of the warehouse

        // Method for producers to add items
        public synchronized void addItem(String item) throws InterruptedException {
            while (inventory.size() == capacity) {
                System.out.println("Warehouse is full. Producer is waiting...");
                wait(); // Wait until space is available
            }
            inventory.add(item);
            System.out.println("Produced: " + item);
            notifyAll(); // Notify consumers
        }

        // Method for consumers to retrieve items
        public synchronized String retrieveItem() throws InterruptedException {
            while (inventory.isEmpty()) {
                System.out.println("Warehouse is empty. Consumer is waiting...");
                wait(); // Wait until items are available
            }
            String item = inventory.removeFirst();
            System.out.println("Consumed: " + item);
            notifyAll(); // Notify producers
            return item;
        }
    }

    // Producer class
    static class Producer implements Runnable {
        private final Warehouse warehouse;
        private volatile boolean running = true;

        public Producer(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        public void stop() {
            running = false;
        }

        @Override
        public void run() {
            int count = 0;
            while (running) {
                try {
                    warehouse.addItem("Item-" + count++);
                    Thread.sleep(500); // Simulate production time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Consumer class
    static class Consumer implements Runnable {
        private final Warehouse warehouse;

        public Consumer(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    warehouse.retrieveItem();
                    Thread.sleep(700); // Simulate consumption time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        // Create a shared warehouse
        Warehouse warehouse = new Warehouse();

        // Create producer and consumer
        Producer producer = new Producer(warehouse);
        Consumer consumer = new Consumer(warehouse);

        // Executor framework to manage threads
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(producer);
        executorService.submit(consumer);

        // Run for 10 seconds and then stop the producer
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            producer.stop();
            executorService.shutdownNow();
            System.out.println("System shut down gracefully.");
        }
    }
}
