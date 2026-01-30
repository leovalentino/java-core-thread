package com.leolabs.threads.modern;

import java.time.Instant;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates the power of Java 21 Virtual Threads (Project Loom).
 * 
 * Key concepts:
 * 1. Virtual Threads: Lightweight threads managed by the JVM
 * 2. High-throughput concurrency: Supporting millions of concurrent operations
 * 3. Structured Concurrency: Managing thread lifecycle with try-with-resources
 */
public class VirtualThreadsLab {

    public static void main(String[] args) {
        System.out.println("=== Java 21 Virtual Threads Lab ===\n");
        System.out.println("Demonstrating:");
        System.out.println("1. Virtual Threads (Project Loom) for massive concurrency");
        System.out.println("2. Handling 100,000 concurrent I/O operations");
        System.out.println("3. Structured concurrency with try-with-resources\n");
        
        // Display system information
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available CPU cores: " + availableProcessors);
        System.out.println("Platform threads (carrier threads): " + availableProcessors);
        System.out.println("Virtual threads to create: 100,000\n");
        
        System.out.println("Warning: Creating 100,000 platform threads would crash the JVM!");
        System.out.println("But with virtual threads, we can handle this easily.\n");
        
        // Start timing
        Instant start = Instant.now();
        
        // Counter to track completed tasks
        AtomicInteger completedTasks = new AtomicInteger(0);
        
        // Create a virtual thread per task executor
        // The try-with-resources ensures all threads complete before continuing
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            System.out.println("Starting 100,000 concurrent tasks...");
            
            // Submit 100,000 tasks
            for (int i = 0; i < 100_000; i++) {
                final int taskId = i;
                
                executor.submit(() -> {
                    // Simulate a slow I/O operation (e.g., database query, API call)
                    try {
                        Thread.sleep(1000); // 1 second sleep
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    
                    // For the first task, show thread information
                    if (taskId == 0) {
                        System.out.println("\n[Sample Task] Thread info: " + 
                            Thread.currentThread().toString());
                        System.out.println("Is virtual thread: " + 
                            Thread.currentThread().isVirtual());
                    }
                    
                    // Track completion
                    int completed = completedTasks.incrementAndGet();
                    
                    // Print progress every 10,000 tasks
                    if (completed % 10_000 == 0) {
                        System.out.println("Progress: " + completed + " / 100,000 tasks completed");
                    }
                });
            }
            
            System.out.println("\nAll 100,000 tasks submitted!");
            System.out.println("Executor will automatically wait for all tasks to complete...");
            
        } // try-with-resources automatically calls executor.close() which waits for all tasks
        
        // Calculate elapsed time
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        // Display results
        System.out.println("\n=== Results ===");
        System.out.println("Total tasks: 100,000");
        System.out.println("Task duration per task: 1 second (simulated I/O)");
        System.out.println("Total execution time: " + duration.toMillis() + " ms");
        System.out.println("Completed tasks: " + completedTasks.get());
        
        // Performance analysis
        System.out.println("\n=== Performance Analysis ===");
        long sequentialTime = 100_000 * 1000L; // 100,000 seconds in milliseconds
        System.out.println("Sequential execution would take: " + 
            (sequentialTime / 1000) + " seconds ≈ " + 
            (sequentialTime / (1000 * 3600)) + " hours");
        System.out.println("Virtual threads execution took: " + 
            (duration.toMillis() / 1000.0) + " seconds");
        
        double speedup = (double) sequentialTime / duration.toMillis();
        System.out.printf("Speedup factor: %.0fx\n", speedup);
        
        // Educational content
        System.out.println("\n=== Key Learning Points ===");
        System.out.println("1. Virtual Threads vs Platform Threads:");
        System.out.println("   • Platform Threads: OS-managed, ~1MB stack each");
        System.out.println("   • Virtual Threads: JVM-managed, lightweight (~few KB)");
        System.out.println("   • Virtual threads are multiplexed onto platform threads");
        
        System.out.println("\n2. Why This Works:");
        System.out.println("   • 100,000 virtual threads sleep concurrently");
        System.out.println("   • During sleep, virtual threads are unmounted from carrier threads");
        System.out.println("   • Carrier threads can execute other virtual threads");
        System.out.println("   • Result: High throughput with minimal resources");
        
        System.out.println("\n3. Structured Concurrency:");
        System.out.println("   • try-with-resources ensures proper cleanup");
        System.out.println("   • All tasks complete before exiting the block");
        System.out.println("   • No manual thread management needed");
        
        System.out.println("\n4. When to Use Virtual Threads:");
        System.out.println("   ✓ High-concurrency I/O-bound applications");
        System.out.println("   ✓ Microservices handling many concurrent requests");
        System.out.println("   ✓ Anywhere you'd use thread pools for I/O");
        System.out.println("   ✗ CPU-bound computations (use parallel streams instead)");
        
        System.out.println("\n=== The Magic Explained ===");
        System.out.println("100,000 threads slept for 1 second concurrently.");
        System.out.println("If done sequentially, this would take 100,000 seconds ≈ 27.7 hours!");
        System.out.println("With virtual threads, it takes ~1-2 seconds because:");
        System.out.println("1. Virtual threads are cheap to create");
        System.out.println("2. During sleep, threads don't consume CPU");
        System.out.println("3. The JVM efficiently schedules them on few platform threads");
        
        System.out.println("\n=== Comparison with Traditional Threads ===");
        System.out.println("Traditional approach (platform threads):");
        System.out.println("  • 100,000 threads × 1MB stack = 100GB memory (crashes JVM)");
        System.out.println("  • High context-switching overhead");
        System.out.println("  • Limited by OS thread limits");
        
        System.out.println("\nVirtual threads approach:");
        System.out.println("  • 100,000 virtual threads × few KB = ~200-300MB memory");
        System.out.println("  • Efficient scheduling by JVM");
        System.out.println("  • Scales to millions of concurrent operations");
        
        System.out.println("\n=== Try These Modifications ===");
        System.out.println("1. Change sleep time to 100ms or 2 seconds");
        System.out.println("2. Try with platform threads (Executors.newFixedThreadPool)");
        System.out.println("3. Add CPU work after sleep to see the difference");
        System.out.println("4. Increase to 1,000,000 tasks");
        
        System.out.println("\n=== Best Practices ===");
        System.out.println("• Use virtual threads for I/O-bound operations");
        System.out.println("• Avoid thread-local variables with virtual threads");
        System.out.println("• Use structured concurrency patterns");
        System.out.println("• Monitor virtual thread usage in production");
        
        System.out.println("\n=== Real-World Applications ===");
        System.out.println("• Web servers handling millions of connections");
        System.out.println("• Database connection pools");
        System.out.println("• Microservices architectures");
        System.out.println("• Reactive programming implementations");
        
        System.out.println("\n=== Java 21+ Features ===");
        System.out.println("• Virtual Threads: java.lang.Thread.startVirtualThread()");
        System.out.println("• Structured Concurrency: JEP 453 (Preview)");
        System.out.println("• Scoped Values: Alternative to thread-local for virtual threads");
    }
}
