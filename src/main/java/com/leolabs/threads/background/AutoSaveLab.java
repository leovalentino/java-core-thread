package com.leolabs.threads.background;

/**
 * Demonstrates daemon threads and shared memory concepts.
 * 
 * Key concepts:
 * 1. Daemon Threads: Background threads that don't prevent JVM shutdown
 * 2. Shared Memory: Multiple threads accessing the same memory without explicit data passing
 * 3. Thread Safety: This example intentionally doesn't synchronize to show potential issues
 */
public class AutoSaveLab {
    
    // Shared memory: All threads can read/write this StringBuilder
    private static final StringBuilder document = new StringBuilder();
    
    /**
     * Starts a daemon thread that auto-saves the document every second.
     * Daemon threads are automatically terminated when all non-daemon threads finish.
     */
    private static void startAutoSaver() {
        Thread autoSaver = new Thread(() -> {
            System.out.println("[Auto-Saver] Daemon thread started. Will auto-save every second.");
            
            // Infinite loop - but daemon thread will be terminated when main thread ends
            while (true) {
                try {
                    // Wait 1 second between saves
                    Thread.sleep(1000);
                    
                    // Read from shared memory
                    String currentContent = document.toString();
                    System.out.println("[Auto-Saver] Saving content: " + 
                        (currentContent.isEmpty() ? "<empty>" : currentContent.trim()));
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("[Auto-Saver] Interrupted!");
                    break;
                }
            }
        });
        
        // CRITICAL: Mark as daemon thread
        autoSaver.setDaemon(true);
        autoSaver.start();
        
        System.out.println("[Main] Auto-saver daemon thread started.");
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Daemon Threads & Shared Memory Lab ===\n");
        System.out.println("Demonstrating:");
        System.out.println("1. Daemon threads (auto-terminate when main ends)");
        System.out.println("2. Shared memory access (no explicit data passing)\n");
        
        // Start the auto-saver daemon thread
        startAutoSaver();
        
        // Simulate user typing
        System.out.println("[User] Starting to type document...");
        
        for (int i = 1; i <= 5; i++) {
            // Write to shared memory
            String line = "Line " + i + "\n";
            document.append(line);
            
            System.out.println("[User] Typing: " + line.trim());
            
            // Simulate typing speed
            Thread.sleep(600);
        }
        
        System.out.println("\n[User] Done typing. Exiting application.");
        System.out.println("Final document content:");
        System.out.println(document.toString());
        
        // Small delay to show auto-saver might run one more time
        Thread.sleep(200);
        
        System.out.println("\n=== Program Ending ===");
        System.out.println("Note: The daemon auto-saver thread will be automatically");
        System.out.println("terminated by the JVM since all non-daemon threads have finished.");
        
        // The program ends here, terminating the daemon thread automatically
    }
}
