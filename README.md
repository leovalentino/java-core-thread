# Java Multithreading Lab

A comprehensive hands-on lab project designed to teach and demonstrate fundamental multithreading concepts in Java.

## 📋 Project Overview

This project provides a series of practical labs that explore different aspects of Java multithreading, from basic thread creation to advanced concepts like daemon threads and shared memory. Each lab is self-contained and focuses on specific learning objectives.

## 🎯 Learning Objectives

By completing these labs, you will understand:

1. **Thread Creation**: Different ways to create and start threads
2. **Thread Lifecycle**: The various states a thread goes through
3. **Thread Safety**: Race conditions and synchronization
4. **Performance Benefits**: How multithreading improves I/O-bound operations
5. **Daemon Threads**: Background threads that don't prevent JVM shutdown
6. **Shared Memory**: How threads communicate through shared state

## 🏗️ Project Structure

```
src/main/java/com/leolabs/threads/
├── basics/                    # Basic thread creation demos
│   └── ThreadCreationDemo.java
├── lifecycle/                 # Thread state monitoring
│   └── LifecycleLogger.java
├── safety/                    # Race conditions and synchronization
│   └── RaceConditionLab.java
├── performance/               # Performance comparison
│   └── PerformanceLab.java
├── background/                # Daemon threads and shared memory
│   └── AutoSaveLab.java
├── executors/                 # Thread pools and executors
│   └── ExecutorLab.java
├── futures/                   # Callable and Future interfaces
│   └── FutureLab.java
├── forkjoin/                  # ForkJoinPool and parallel streams
│   └── ParallelStreamLab.java
└── async/                     # CompletableFuture and async composition
    └── CompletableFutureLab.java
```

## 🧪 Labs

### 1. Thread Creation Basics (`ThreadCreationDemo.java`)
**Concepts**: `Thread` class vs `Runnable` interface, lambda expressions
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.basics.ThreadCreationDemo"
```
**Key Takeaways**:
- Two ways to create threads: extending `Thread` class or implementing `Runnable`
- Using lambda expressions for concise `Runnable` implementation
- The importance of `join()` for waiting on thread completion

### 2. Thread Lifecycle (`LifecycleLogger.java`)
**Concepts**: Thread states (NEW, RUNNABLE, TIMED_WAITING, TERMINATED)
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.lifecycle.LifecycleLogger"
```
**Key Takeaways**:
- How to monitor thread state transitions
- Understanding when threads enter TIMED_WAITING state (during `sleep()`)
- The complete lifecycle from creation to termination

### 3. Race Conditions (`RaceConditionLab.java`)
**Concepts**: Race conditions, atomic operations, `synchronized` keyword
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.safety.RaceConditionLab"
```
**Key Takeaways**:
- Why `count++` is not atomic and causes race conditions
- How to detect race conditions through inconsistent results
- Fixing race conditions with the `synchronized` keyword
- **Note**: Run multiple times to see different results due to thread scheduling

### 4. Performance Benefits (`PerformanceLab.java`)
**Concepts**: Sequential vs parallel execution, I/O-bound operations
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.performance.PerformanceLab"
```
**Key Takeaways**:
- Measuring execution time with `System.currentTimeMillis()`
- How multithreading speeds up I/O-bound operations
- Understanding that total parallel time ≈ duration of longest task
- The concept of "speedup factor" in concurrent execution

### 5. Daemon Threads & Shared Memory (`AutoSaveLab.java`)
**Concepts**: Daemon threads, shared memory, automatic thread termination
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.background.AutoSaveLab"
```
**Key Takeaways**:
- Daemon threads automatically terminate when all non-daemon threads finish
- Threads can communicate through shared objects (like `StringBuilder`)
- Real-time access to shared memory without explicit data passing
- The trade-off between convenience and thread safety in shared memory

### 6. Thread Pools (`ExecutorLab.java`)
**Concepts**: ExecutorService, FixedThreadPool, thread reuse
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.executors.ExecutorLab"
```
**Key Takeaways**:
- How thread pools reuse threads for multiple tasks
- Efficient resource management vs creating new threads per task
- The importance of proper executor shutdown
- Automatic task queuing and scheduling

### 7. Callable & Future (`FutureLab.java`)
**Concepts**: Callable interface, Future objects, asynchronous computation
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.futures.FutureLab"
```
**Key Takeaways**:
- Callable can return values and throw exceptions (unlike Runnable)
- Future represents the result of an asynchronous computation
- Non-blocking task submission vs blocking result retrieval
- Real-world analogy: restaurant order tickets

### 8. ForkJoinPool & Parallel Streams (`ParallelStreamLab.java`)
**Concepts**: Parallel streams, ForkJoinPool, CPU-bound optimization
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.forkjoin.ParallelStreamLab"
```
**Key Takeaways**:
- How parallel streams automatically use ForkJoinPool.commonPool()
- Performance benefits for CPU-intensive operations on large datasets
- Measuring speedup factor and efficiency
- When to use parallel vs sequential streams

### 9. CompletableFuture (`CompletableFutureLab.java`)
**Concepts**: Asynchronous task composition, non-blocking operations
```bash
mvn exec:java -Dexec.mainClass="com.leolabs.threads.async.CompletableFutureLab"
```
**Key Takeaways**:
- Creating asynchronous tasks with `supplyAsync()`
- Combining multiple futures with `thenCombine()`
- Performance benefits of parallel service calls
- Non-blocking composition patterns

## 🚀 Getting Started

### Prerequisites
- Java 21 or later
- Maven 3.6+
- Git (for version control)

### Installation & Setup

1. **Clone and navigate to the project**:
   ```bash
   git clone <repository-url>
   cd multithreading-lab
   ```

2. **Compile the project**:
   ```bash
   mvn compile
   ```

3. **Run individual labs** (see commands above for each lab)

4. **Run all labs sequentially**:
   ```bash
   # Create a simple script to run all demos
   for lab in basics lifecycle safety performance background executors futures forkjoin async; do
     echo "=== Running $lab lab ==="
     mvn exec:java -Dexec.mainClass="com.leolabs.threads.$lab.*" 2>/dev/null || true
     echo ""
   done
   ```

## 🔧 Technical Details

### Build Configuration
- **Java Version**: 21 (using modern language features)
- **Encoding**: UTF-8 throughout the project
- **Build Tool**: Maven with minimal dependencies (standard library only)

### Key Java Features Used
- Lambda expressions for concise thread creation
- `Thread.sleep()` for simulating I/O operations
- `synchronized` keyword for thread safety
- `StringBuilder` for mutable shared state
- `System.currentTimeMillis()` for performance measurement

## 📚 Learning Path

### Recommended Order for Beginners
1. Start with `ThreadCreationDemo` to understand basic thread creation
2. Move to `LifecycleLogger` to see how threads transition between states
3. Try `RaceConditionLab` to understand thread safety issues
4. Run `PerformanceLab` to see the benefits of multithreading
5. Finish with `AutoSaveLab` for advanced concepts

### For Experienced Developers
- Examine the race condition lab and implement additional synchronization mechanisms
- Modify the performance lab to test with more threads (3, 4, 5...)
- Add thread pools to the performance comparison
- Implement proper thread interruption handling in all labs

## 🧠 Common Pitfalls & Tips

1. **Race Conditions**: Always run the race condition lab multiple times to see different results
2. **Daemon Threads**: Remember that daemon threads may not complete their work if the main thread exits
3. **Thread Safety**: The `AutoSaveLab` intentionally doesn't synchronize access to `StringBuilder` - consider what could go wrong
4. **Performance**: The performance benefits shown are for I/O-bound tasks; CPU-bound tasks have different characteristics

## 🔍 Debugging Tips

1. **Thread Names**: Always name your threads for easier debugging
   ```java
   Thread worker = new Thread(() -> {...}, "Worker-Thread");
   ```

2. **State Monitoring**: Use `thread.getState()` to debug stuck threads

3. **Interruption Handling**: Always handle `InterruptedException` properly
   ```java
   try {
       Thread.sleep(1000);
   } catch (InterruptedException e) {
       Thread.currentThread().interrupt(); // Preserve interruption status
       // Clean up and exit
   }
   ```

## 📈 Next Steps

After mastering these labs, consider exploring:

1. **Reactive Programming**: Project Reactor or RxJava for async streams
2. **Concurrent Collections**: `ConcurrentHashMap`, `CopyOnWriteArrayList`
3. **Advanced Synchronization**: `ReentrantLock`, `Semaphore`, `CountDownLatch`
4. **ScheduledExecutorService**: Delayed and periodic task execution
5. **Virtual Threads**: Project Loom's lightweight threads (Java 19+)
6. **Custom ForkJoinPool**: Creating and configuring custom thread pools
7. **Java 21 Structured Concurrency**: Preview feature for managing related tasks

## 🤝 Contributing

Feel free to extend this lab with:
- Additional examples of thread synchronization
- More complex race condition scenarios
- Performance comparisons with different numbers of threads
- Examples using Java's concurrent utilities

## 📄 License

This educational project is open for learning and adaptation.

---

**Happy Threading!** 🧵⚡

*Remember: With great concurrency comes great responsibility.*
