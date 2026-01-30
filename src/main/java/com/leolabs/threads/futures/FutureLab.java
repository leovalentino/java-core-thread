package com.leolabs.threads.futures;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the use of Callable and Future interfaces.
 * 
 * Key concepts:
 * 1. Callable: Like Runnable but can return a value and throw checked exceptions
 * 2. Future: Represents the result of an asynchronous computation
 * 3. Non-blocking submission vs blocking result retrieval
 */
public class FutureLab {

    /**
     * Creates a Callable task that simulates preparing food.
     * 
     * @param foodName Name of the food to prepare
     * @return A Callable that returns a status message when complete
     */
    private static Callable<String> makeFood(String foodName) {
        return () -> {
            System.out.println("Kitchen: Preparing " + foodName + " on " + 
                Thread.currentThread().getName());
            
            try {
                // Simulate time needed to prepare the food
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(foodName + " preparation was interrupted", e);
            }
            
            System.out.println("Kitchen: Finished preparing " + foodName);
            return "Ready: " + foodName;
        };
    }

    public static void main(String[] args) {
        System.out.println("=== Future & Callable Lab ===\n");
        System.out.println("Scenario: Restaurant Kitchen");
        System.out.println("We'll order Pizza and Pasta, then do other work while they're being prepared.\n");
        
        // Create a fixed thread pool with 2 threads (one for each food item)
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        System.out.println("Main: Opening kitchen with 2 chefs (threads)...");
        
        try {
            // Submit orders to the kitchen (non-blocking)
            System.out.println("\nMain: Placing orders...");
            Future<String> pizzaTicket = executor.submit(makeFood("Pizza"));
            Future<String> pastaTicket = executor.submit(makeFood("Pasta"));
            
            System.out.println("Main: Orders submitted! Received future tickets immediately.");
            System.out.println("Main: Doing other restaurant work while food is being prepared...");
            
            // Simulate main thread doing other work
            Thread.sleep(500);
            System.out.println("Main: Checked on customers...");
            
            Thread.sleep(500);
            System.out.println("Main: Cleaned some tables...");
            
            Thread.sleep(500);
            System.out.println("Main: Restocked supplies...\n");
            
            System.out.println("Main: Now asking for the food results...");
            System.out.println("Note: get() will block until the result is available\n");
            
            // Retrieve results (blocking calls)
            String pizzaResult = pizzaTicket.get();
            System.out.println("Main: Received -> " + pizzaResult);
            
            String pastaResult = pastaTicket.get();
            System.out.println("Main: Received -> " + pastaResult);
            
            System.out.println("\n=== All orders completed! ===");
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always shutdown the executor
            executor.shutdown();
            
            try {
                // Wait for any remaining tasks to complete
                if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                    System.out.println("Forcing shutdown...");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executor.shutdownNow();
            }
        }
        
        // Educational summary
        System.out.println("\n=== Key Learning Points ===");
        System.out.println("1. Callable vs Runnable:");
        System.out.println("   • Runnable: run() returns void");
        System.out.println("   • Callable: call() returns a value and can throw exceptions");
        System.out.println();
        System.out.println("2. Future represents a pending result:");
        System.out.println("   • submit() returns immediately (non-blocking)");
        System.out.println("   • get() blocks until result is available");
        System.out.println("   • isDone() checks if computation is complete");
        System.out.println("   • cancel() attempts to cancel the task");
        System.out.println();
        System.out.println("3. Real-world analogy:");
        System.out.println("   • Submitting a task = giving an order to the kitchen");
        System.out.println("   • Future = the order ticket");
        System.out.println("   • get() = waiting at the counter for your food");
        System.out.println();
        System.out.println("4. Best practices:");
        System.out.println("   • Always shutdown ExecutorService");
        System.out.println("   • Handle InterruptedException properly");
        System.out.println("   • Consider timeouts with get(timeout, unit)");
        
        System.out.println("\n=== Try These Modifications ===");
        System.out.println("1. Add a timeout: pizzaTicket.get(1, TimeUnit.SECONDS)");
        System.out.println("2. Check if done: pizzaTicket.isDone() before calling get()");
        System.out.println("3. Add more food items to see thread pool queuing");
    }
}
