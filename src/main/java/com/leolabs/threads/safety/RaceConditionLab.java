package com.leolabs.threads.safety;

/**
 * Demonstrates a race condition when multiple threads access
 * shared data without proper synchronization.
 */
public class RaceConditionLab {
    
    // Shared counter class
    static class Counter {
        private int count = 0;
        
        // Non-atomic increment - RACE CONDITION HERE!
        // To fix, add the synchronized keyword:
        // public synchronized void increment() {
        public void increment() {
            count++; // This is NOT atomic: read -> modify -> write
        }
        
        public int getCount() {
            return count;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Race Condition Lab ===");
        System.out.println("Demonstrating race condition with shared counter...");
        
        final Counter counter = new Counter();
        final int iterations = 10000;
        
        // Create two threads that increment the same counter
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < iterations; i++) {
                counter.increment();
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < iterations; i++) {
                counter.increment();
            }
        });
        
        // Start both threads
        thread1.start();
        thread2.start();
        
        // Wait for both threads to complete
        thread1.join();
        thread2.join();
        
        // Print results
        int expected = 2 * iterations;
        int actual = counter.getCount();
        
        System.out.println("\n=== Results ===");
        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + actual);
        System.out.println("Difference:     " + (expected - actual));
        
        if (actual != expected) {
            System.out.println("\n✓ RACE CONDITION DETECTED!");
            System.out.println("The count is incorrect due to thread interference.");
            System.out.println("To fix, add 'synchronized' keyword to increment() method.");
        } else {
            System.out.println("\n⚠ No race condition detected this time (lucky!).");
            System.out.println("Run the program multiple times to see the race condition.");
        }
    }
}
