package com.leolabs.threads.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the use of ExecutorService and Fixed Thread Pools.
 * 
 * Key concepts:
 * 1. Thread Pools: Reusing a fixed number of threads to execute multiple tasks
 * 2. ExecutorService: Higher-level API for managing thread execution
 * 3. Resource Management: Efficient thread reuse vs creating new threads per task
 */
public class ExecutorLab {

    public static void main(String[] args) {
        System.out.println("=== ExecutorService & Thread Pools Lab ===\n");
        System.out.println("Demonstrating:");
        System.out.println("1. Fixed thread pool with 3 worker threads");
        System.out.println("2. Reusing threads instead of creating new ones");
        System.out.println("3. Executing 10 tasks with only 3 threads\n");
        
        // Create a fixed thread pool with exactly 3 worker threads
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        System.out.println("Created FixedThreadPool with 3 worker threads");
        System.out.println("Submitting 10 tasks to the executor...\n");
        
        // Submit 10 tasks to the executor
        for (int i = 1; i <= 10; i++) {
            final int taskId = i; // Need final or effectively final for lambda
            
            executor.submit(() -> {
                System.out.println("Task [" + taskId + "] started by " + 
                    Thread.currentThread().getName());
                
                try {
                    // Simulate work by sleeping for 500ms
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Task [" + taskId + "] was interrupted!");
                }
                
                System.out.println("Task [" + taskId + "] finished.");
            });
        }
        
        System.out.println("\nAll 10 tasks submitted to the executor.");
        System.out.println("Notice: Only 3 threads will handle all 10 tasks!");
        
        // CRITICAL: Shutdown the executor
        // Without shutdown(), the application will never exit because
        // the pool stays alive waiting for more work
        executor.shutdown();
        
        try {
            // Wait for all tasks to complete (with timeout)
            boolean terminated = executor.awaitTermination(10, TimeUnit.SECONDS);
            
            if (terminated) {
                System.out.println("\n=== All tasks completed successfully ===");
            } else {
                System.out.println("\n=== Timeout reached, not all tasks completed ===");
                // Force shutdown if tasks are still running
                executor.shutdownNow();
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread was interrupted while waiting for tasks to complete");
            executor.shutdownNow();
        }
        
        // Analysis of what we observed
        System.out.println("\n=== Key Observations ===");
        System.out.println("1. Only 3 thread names appeared: pool-1-thread-1, pool-1-thread-2, pool-1-thread-3");
        System.out.println("2. Threads were REUSED for multiple tasks (not created/destroyed per task)");
        System.out.println("3. Tasks were executed concurrently (up to 3 at a time)");
        System.out.println("4. The executor managed task queuing automatically");
        
        System.out.println("\n=== Benefits of Thread Pools ===");
        System.out.println("• Resource Efficiency: Reusing threads avoids creation/destruction overhead");
        System.out.println("• Controlled Concurrency: Limits number of concurrent threads");
        System.out.println("• Task Management: Automatic queuing and scheduling");
        System.out.println("• Graceful Shutdown: Proper resource cleanup with shutdown()");
        
        System.out.println("\n=== Comparison with Manual Thread Creation ===");
        System.out.println("Without thread pool: 10 tasks = 10 threads created/destroyed");
        System.out.println("With thread pool (size 3): 10 tasks = 3 threads reused");
        System.out.println("Result: Less memory overhead, faster task startup");
        
        System.out.println("\n=== Important Note ===");
        System.out.println("Always call shutdown() on ExecutorService when done!");
        System.out.println("Otherwise, threads stay alive and JVM won't exit.");
    }
}
