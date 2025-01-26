public class Singleton {

    // Volatile variable to ensure visibility and prevent reordering
    private static volatile Singleton instance;

    // Private constructor to prevent instantiation
    private Singleton() {
        System.out.println("Singleton instance created.");
    }

    // Double-checked locking to ensure thread-safe lazy initialization
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    // Example method
    public void showMessage(String message) {
        System.out.println("Message from Singleton: " + message);
    }

    public static void main(String[] args) {
        // Runnable task for multiple threads
        Runnable task = () -> {
            Singleton singleton = Singleton.getInstance();
            singleton.showMessage("Thread " + Thread.currentThread().getName());
        };

        // Create and start multiple threads
        Thread thread1 = new Thread(task, "1");
        Thread thread2 = new Thread(task, "2");
        Thread thread3 = new Thread(task, "3");
        Thread thread4 = new Thread(task, "4");
        Thread thread5 = new Thread(task, "5");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        // Wait for threads to finish
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted.");
        }

        System.out.println("All threads have finished execution.");
    }
}
