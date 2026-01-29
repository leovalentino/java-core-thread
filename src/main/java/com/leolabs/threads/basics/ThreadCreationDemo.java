package com.leolabs.threads.basics;

/**
 * Demonstrates two ways to create threads in Java:
 * 1. By extending the Thread class
 * 2. By implementing the Runnable interface (using lambda)
 */
public class ThreadCreationDemo {

    public static void main(String[] args) {
        System.out.println("=== Thread Creation Demo ===");
        System.out.println("Main thread: " + Thread.currentThread().getName());
        
        // Method 1: Extending Thread class
        Thread thread1 = new MyThread();
        thread1.start();
        
        // Method 2: Implementing Runnable interface (using lambda)
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread from Runnable (lambda): " + 
                Thread.currentThread().getName());
        });
        thread2.start();
        
        // Wait for threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread was interrupted");
        }
        
        System.out.println("All threads completed!");
    }
    
    // Inner class extending Thread
    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Thread from Thread class: " + 
                Thread.currentThread().getName());
        }
    }
}
