package com.leolabs.threads.forkjoin;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.concurrent.ForkJoinPool;

/**
 * Demonstrates the use of ForkJoinPool via Java Parallel Streams.
 * 
 * Key concepts:
 * 1. Parallel Streams: Automatically use ForkJoinPool.commonPool()
 * 2. CPU-bound tasks: Where parallel processing provides significant benefits
 * 3. Speedup factor: Measuring performance improvement from parallelization
 */
public class ParallelStreamLab {

    public static void main(String[] args) {
        System.out.println("=== ForkJoinPool & Parallel Streams Lab ===\n");
        System.out.println("Demonstrating:");
        System.out.println("1. Parallel Streams for CPU-intensive operations");
        System.out.println("2. Automatic use of ForkJoinPool.commonPool()");
        System.out.println("3. Performance comparison: Sequential vs Parallel\n");
        
        // Display system information
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available CPU cores: " + availableProcessors);
        System.out.println("ForkJoinPool.commonPool() parallelism: " + 
            ForkJoinPool.commonPool().getParallelism());
        System.out.println();
        
        // Data Setup: Create 10 million random integers
        System.out.println("Generating 10,000,000 random integers...");
        List<Integer> numbers = new ArrayList<>(10_000_000);
        Random random = new Random(42); // Fixed seed for reproducibility
        
        for (int i = 0; i < 10_000_000; i++) {
            numbers.add(random.nextInt(1000));
        }
        System.out.println("Data generation complete.\n");
        
        // CPU-intensive operation: compute Math.sin(Math.sqrt(number))
        System.out.println("CPU-intensive operation: Math.sin(Math.sqrt(number))");
        System.out.println("This operation will be performed on all 10,000,000 numbers.\n");
        
        // Experiment A: Sequential Stream
        System.out.println("--- Experiment A: Sequential Stream ---");
        long startSequential = System.currentTimeMillis();
        
        List<Double> sequentialResult = numbers.stream()
            .map(number -> Math.sin(Math.sqrt(number)))
            .collect(Collectors.toList());
        
        long endSequential = System.currentTimeMillis();
        long sequentialTime = endSequential - startSequential;
        
        System.out.println("Sequential execution time: " + sequentialTime + " ms");
        System.out.println("Processed " + sequentialResult.size() + " elements sequentially.\n");
        
        // Small pause between experiments
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Experiment B: Parallel Stream (uses ForkJoinPool)
        System.out.println("--- Experiment B: Parallel Stream ---");
        System.out.println("Note: Parallel streams use ForkJoinPool.commonPool() under the hood.");
        
        long startParallel = System.currentTimeMillis();
        
        List<Double> parallelResult = numbers.parallelStream()
            .map(number -> Math.sin(Math.sqrt(number)))
            .collect(Collectors.toList());
        
        long endParallel = System.currentTimeMillis();
        long parallelTime = endParallel - startParallel;
        
        System.out.println("Parallel execution time: " + parallelTime + " ms");
        System.out.println("Processed " + parallelResult.size() + " elements in parallel.\n");
        
        // Verify results are the same (optional but good for demonstration)
        boolean resultsMatch = sequentialResult.equals(parallelResult);
        System.out.println("Results verification: " + 
            (resultsMatch ? "✓ Sequential and parallel results match!" : "✗ Results differ!"));
        
        // Performance Analysis
        System.out.println("\n=== Performance Analysis ===");
        System.out.println("Sequential time: " + sequentialTime + " ms");
        System.out.println("Parallel time:   " + parallelTime + " ms");
        
        if (parallelTime > 0) {
            double speedup = (double) sequentialTime / parallelTime;
            System.out.printf("Speedup factor:  %.2fx\n", speedup);
            
            double efficiency = (speedup / availableProcessors) * 100;
            System.out.printf("Efficiency:      %.1f%% (of ideal %dx speedup)\n", 
                efficiency, availableProcessors);
        }
        
        System.out.println("\n=== Key Learning Points ===");
        System.out.println("1. Parallel Streams:");
        System.out.println("   • Automatically use ForkJoinPool.commonPool()");
        System.out.println("   • Split work into smaller tasks (fork)");
        System.out.println("   • Combine results (join)");
        System.out.println("   • Ideal for CPU-bound operations on large datasets");
        
        System.out.println("\n2. When to use Parallel Streams:");
        System.out.println("   ✓ Large datasets (10,000+ elements)");
        System.out.println("   ✓ CPU-intensive operations");
        System.out.println("   ✓ Stateless, independent operations");
        System.out.println("   ✗ Small datasets (overhead > benefit)");
        System.out.println("   ✗ I/O-bound operations");
        System.out.println("   ✗ Stateful or order-dependent operations");
        
        System.out.println("\n3. ForkJoinPool Details:");
        System.out.println("   • Default parallelism = CPU cores - 1");
        System.out.println("   • Work-stealing algorithm for load balancing");
        System.out.println("   • Can be customized with custom ForkJoinPool");
        
        System.out.println("\n=== Advanced Notes ===");
        System.out.println("Custom ForkJoinPool example:");
        System.out.println("  ForkJoinPool customPool = new ForkJoinPool(4);");
        System.out.println("  customPool.submit(() -> numbers.parallelStream()...);");
        System.out.println("  customPool.shutdown();");
        
        System.out.println("\n=== Try These Modifications ===");
        System.out.println("1. Change dataset size: 1,000, 100,000, 1,000,000");
        System.out.println("2. Try different CPU operations: Math.log(), Math.pow()");
        System.out.println("3. Add .sorted() to see impact on parallel performance");
        System.out.println("4. Create custom ForkJoinPool with different parallelism");
        
        System.out.println("\n=== Important Considerations ===");
        System.out.println("• Parallel streams have overhead (splitting, combining)");
        System.out.println("• Not all operations benefit from parallelization");
        System.out.println("• Thread safety: Ensure operations are thread-safe");
        System.out.println("• Ordering: parallelStream() may not preserve order");
    }
}
