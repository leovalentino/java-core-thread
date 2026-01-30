package com.leolabs.threads.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Demonstrates the use of CompletableFuture for asynchronous task composition.
 * 
 * Key concepts:
 * 1. CompletableFuture: Represents a future result of an asynchronous computation
 * 2. Non-blocking composition: Combining multiple async operations without blocking
 * 3. Performance benefits: Parallel execution of independent tasks
 */
public class CompletableFutureLab {

    /**
     * Simulates fetching flight information from a remote service.
     * 
     * @param destination The travel destination
     * @return Flight information string
     */
    private static String fetchFlight(String destination) {
        System.out.println("[Flight Service] Starting flight search for " + destination + 
            " on " + Thread.currentThread().getName());
        try {
            // Simulate network latency and processing time
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Flight service interrupted", e);
        }
        String result = "Flight to " + destination;
        System.out.println("[Flight Service] Finished: " + result);
        return result;
    }

    /**
     * Simulates fetching hotel information from a remote service.
     * 
     * @param destination The travel destination
     * @return Hotel information string
     */
    private static String fetchHotel(String destination) {
        System.out.println("[Hotel Service] Starting hotel search for " + destination + 
            " on " + Thread.currentThread().getName());
        try {
            // Simulate network latency and processing time
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Hotel service interrupted", e);
        }
        String result = "Hotel in " + destination;
        System.out.println("[Hotel Service] Finished: " + result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== CompletableFuture Lab: Travel Booking Aggregator ===\n");
        System.out.println("Scenario: Booking a trip to Paris");
        System.out.println("We need to fetch flight and hotel information concurrently.\n");
        
        // Start timing
        long startTime = System.currentTimeMillis();
        
        // Create async tasks using CompletableFuture
        System.out.println("Starting asynchronous flight and hotel searches...\n");
        
        // Launch flight search asynchronously
        CompletableFuture<String> flightFuture = CompletableFuture.supplyAsync(
            () -> fetchFlight("Paris")
        );
        
        // Launch hotel search asynchronously
        CompletableFuture<String> hotelFuture = CompletableFuture.supplyAsync(
            () -> fetchHotel("Paris")
        );
        
        System.out.println("Both searches started concurrently!");
        System.out.println("Main thread is free to do other work while waiting...\n");
        
        // Simulate main thread doing other work
        try {
            System.out.println("[Main] Doing other aggregator tasks...");
            Thread.sleep(300);
            System.out.println("[Main] Processing user preferences...");
            Thread.sleep(300);
            System.out.println("[Main] Updating UI...\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Combine results when both are complete
        System.out.println("Combining flight and hotel results...");
        CompletableFuture<String> tripFuture = flightFuture.thenCombine(
            hotelFuture,
            (flight, hotel) -> {
                System.out.println("[Combiner] Merging results on " + Thread.currentThread().getName());
                return "Trip: " + flight + " + " + hotel;
            }
        );
        
        // Wait for the combined result (blocking for demonstration)
        String tripResult = tripFuture.join();
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        // Display results
        System.out.println("\n=== Booking Complete ===");
        System.out.println("Result: " + tripResult);
        System.out.println("Total time: " + totalTime + " ms");
        
        // Performance analysis
        System.out.println("\n=== Performance Analysis ===");
        System.out.println("Sequential execution would take: ~4000 ms (2 + 2 seconds)");
        System.out.println("Parallel execution took: " + totalTime + " ms");
        System.out.println("Time saved: " + (4000 - totalTime) + " ms");
        
        double speedup = 4000.0 / totalTime;
        System.out.printf("Speedup factor: %.2fx\n", speedup);
        
        // Educational content
        System.out.println("\n=== Key Learning Points ===");
        System.out.println("1. CompletableFuture.supplyAsync():");
        System.out.println("   • Launches task asynchronously on ForkJoinPool.commonPool()");
        System.out.println("   • Returns immediately with a CompletableFuture");
        System.out.println("   • Non-blocking: main thread continues execution");
        
        System.out.println("\n2. thenCombine():");
        System.out.println("   • Combines two independent CompletableFutures");
        System.out.println("   • Executes when BOTH futures complete");
        System.out.println("   • Returns a new CompletableFuture with combined result");
        
        System.out.println("\n3. join() vs get():");
        System.out.println("   • join(): Like get() but doesn't throw checked exceptions");
        System.out.println("   • Blocks until result is available");
        System.out.println("   • In real apps, avoid blocking - use thenAccept() etc.");
        
        System.out.println("\n4. Thread usage:");
        System.out.println("   • By default uses ForkJoinPool.commonPool()");
        System.out.println("   • Can specify custom Executor as second parameter");
        
        // Advanced examples
        System.out.println("\n=== Advanced Patterns ===");
        System.out.println("1. Error handling:");
        System.out.println("   • exceptionally(): handle exceptions");
        System.out.println("   • handle(): handle both result and exception");
        
        System.out.println("\n2. Chaining operations:");
        System.out.println("   • thenApply(): transform result");
        System.out.println("   • thenCompose(): chain dependent async operations");
        System.out.println("   • thenAccept(): consume result without returning");
        
        System.out.println("\n3. Combining multiple futures:");
        System.out.println("   • allOf(): wait for all futures");
        System.out.println("   • anyOf(): wait for any future");
        
        System.out.println("\n=== Try These Modifications ===");
        System.out.println("1. Add error handling with exceptionally()");
        System.out.println("2. Use custom ExecutorService for more control");
        System.out.println("3. Add a third service (car rental) and combine all three");
        System.out.println("4. Use thenAccept() to process result without blocking");
        
        // Clean shutdown if using custom executor
        System.out.println("\n=== Best Practices ===");
        System.out.println("• Prefer non-blocking composition (thenApply, thenAccept)");
        System.out.println("• Always handle exceptions in async chains");
        System.out.println("• Consider custom executors for I/O-bound tasks");
        System.out.println("• Avoid blocking calls (join(), get()) in production code");
        
        System.out.println("\n=== Real-World Applications ===");
        System.out.println("• Microservices orchestration");
        System.out.println("• Aggregating data from multiple APIs");
        System.out.println("• Parallel processing of independent tasks");
        System.out.println("• Reactive programming patterns");
    }
}
