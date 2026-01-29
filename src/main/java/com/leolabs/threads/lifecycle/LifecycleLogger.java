package com.leolabs.threads.lifecycle;

/**
 * Demonstrates the lifecycle of a thread by monitoring its state changes.
 */
public class LifecycleLogger {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Lifecycle Logger ===");
        System.out.println("Main thread: " + Thread.currentThread().getName());
        
        // Create a thread that will go through various states
        Thread workerThread = new Thread(() -> {
            System.out.println("Worker thread started: " + 
                Thread.currentThread().getName());
            
            try {
                // Go to TIMED_WAITING state
                System.out.println("Worker: About to sleep for 2 seconds...");
                Thread.sleep(2000);
                System.out.println("Worker: Finished sleeping!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Worker thread was interrupted");
            }
            
            System.out.println("Worker thread finishing...");
        });
        
        // Track and print state changes
        Thread.State previousState = null;
        
        System.out.println("\n=== Monitoring Thread States ===");
        
        // Check initial state (NEW)
        Thread.State currentState = workerThread.getState();
        System.out.println("Initial state: " + currentState);
        previousState = currentState;
        
        // Start the thread (NEW -> RUNNABLE)
        workerThread.start();
        
        // Monitor state changes until thread terminates
        while (workerThread.isAlive()) {
            currentState = workerThread.getState();
            
            // Only print when state changes
            if (currentState != previousState) {
                System.out.println("State changed: " + previousState + 
                    " -> " + currentState);
                previousState = currentState;
                
                // Add some context about what the state means
                switch (currentState) {
                    case NEW:
                        System.out.println("  (Thread created but not started)");
                        break;
                    case RUNNABLE:
                        System.out.println("  (Thread is executing or ready to execute)");
                        break;
                    case BLOCKED:
                        System.out.println("  (Thread blocked waiting for a monitor lock)");
                        break;
                    case WAITING:
                        System.out.println("  (Thread waiting indefinitely for another thread)");
                        break;
                    case TIMED_WAITING:
                        System.out.println("  (Thread waiting for a specified time)");
                        break;
                    case TERMINATED:
                        System.out.println("  (Thread has completed execution)");
                        break;
                }
            }
            
            // Small delay to avoid busy waiting
            Thread.sleep(100);
        }
        
        // Check final state (TERMINATED)
        currentState = workerThread.getState();
        if (currentState != previousState) {
            System.out.println("Final state: " + currentState);
        }
        
        System.out.println("\n=== Thread Lifecycle Complete ===");
        System.out.println("States observed: NEW -> RUNNABLE -> TIMED_WAITING -> TERMINATED");
    }
}
