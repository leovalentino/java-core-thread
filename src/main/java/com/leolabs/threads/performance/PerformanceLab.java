package com.leolabs.threads.performance;

/**
 * Demonstrates the performance benefits of multithreading
 * by comparing sequential vs parallel execution of I/O-bound tasks.
 */
public class PerformanceLab {

    /**
     * Simulates an I/O-bound task (e.g., network request, file reading)
     * that takes a specified number of seconds to complete.
     * 
     * @param taskName Name of the task for logging
     * @param seconds Duration to simulate I/O operation
     */
    private static void simulateIO(String taskName, int seconds) {
        System.out.println("[Starting] " + taskName);
        try {
            // Simulate I/O operation by sleeping (blocking)
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(taskName + " was interrupted!");
        }
        System.out.println("[Finished] " + taskName);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Multithreading Performance Lab ===\n");
        
        // Experiment A: Sequential Execution
        System.out.println("--- Experiment A: Sequential Execution ---");
        long startSequential = System.currentTimeMillis();
        
        simulateIO("Task 1", 2);
        simulateIO("Task 2", 2);
        
        long endSequential = System.currentTimeMillis();
        long sequentialTime = endSequential - startSequential;
        
        System.out.println("Sequential execution time: " + sequentialTime + " ms");
        System.out.println("Expected: ~4000 ms (2 + 2 seconds)\n");
        
        // Small pause between experiments
        Thread.sleep(500);
        
        // Experiment B: Parallel Execution
        System.out.println("--- Experiment B: Parallel Execution ---");
        long startParallel = System.currentTimeMillis();
        
        // Create two threads to run tasks in parallel
        Thread thread1 = new Thread(() -> simulateIO("Task 1 (Parallel)", 2));
        Thread thread2 = new Thread(() -> simulateIO("Task 2 (Parallel)", 2));
        
        // Start both threads
        thread1.start();
        thread2.start();
        
        // Wait for both threads to complete
        thread1.join();
        thread2.join();
        
        long endParallel = System.currentTimeMillis();
        long parallelTime = endParallel - startParallel;
        
        System.out.println("Parallel execution time: " + parallelTime + " ms");
        System.out.println("Expected: ~2000 ms (max of 2 seconds)\n");
        
        // Performance Summary
        System.out.println("=== Performance Summary ===");
        System.out.println("Sequential time: " + sequentialTime + " ms");
        System.out.println("Parallel time:   " + parallelTime + " ms");
        System.out.println("Time saved:      " + (sequentialTime - parallelTime) + " ms");
        
        double speedup = (double) sequentialTime / parallelTime;
        System.out.printf("Speedup factor:  %.2fx\n", speedup);
        
        // Explanation
        System.out.println("\n=== Key Learning Points ===");
        System.out.println("1. Sequential execution: Tasks run one after another.");
        System.out.println("   Total time = sum of all task durations.");
        System.out.println("2. Parallel execution: Tasks run concurrently.");
        System.out.println("   Total time ≈ duration of the longest task.");
        System.out.println("3. During Thread.sleep(), the CPU is idle and can");
        System.out.println("   switch to another thread, maximizing utilization.");
        System.out.println("4. This demonstrates the benefit of multithreading");
        System.out.println("   for I/O-bound operations where tasks spend time");
        System.out.println("   waiting for external resources.");
        
        // Note about CPU-bound vs I/O-bound
        System.out.println("\nNote: This example simulates I/O-bound tasks.");
        System.out.println("For CPU-bound tasks, the benefits depend on the");
        System.out.println("number of available CPU cores.");
    }
}
