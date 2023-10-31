# Multithreading

## 1. What is Multithreading?

Computer users take it for granted that their systems can do more than one thing at a time. They assume that they can continue to work in a word processor, while other applications download files, manage the print queue, and stream audio. Even a single application is often expected to do more than one thing at a time. For example, that streaming audio application must simultaneously read the digital audio off the network, decompress it, manage playback, and update its display. Even the word processor should always be ready to respond to keyboard and mouse events, no matter how busy it is reformatting text or updating the display. Software that can do such things is known as concurrent software.

The Java platform is designed from the ground up to support concurrent programming, with basic concurrency support in the Java programming language and the Java class libraries. Since version 5.0, the Java platform has also included high-level concurrency APIs. This lesson introduces the platform's basic concurrency support and summarizes some of the high-level APIs in the java.util.concurrent packages.

Processes and Threads
In concurrent programming, there are two basic units of execution: processes and threads. In the Java programming language, concurrent programming is mostly concerned with threads. However, processes are also important.

A computer system normally has many active processes and threads. This is true even in systems that only have a single execution core, and thus only have one thread actually executing at any given moment. Processing time for a single core is shared among processes and threads through an OS feature called time slicing.

It's becoming more and more common for computer systems to have multiple processors or processors with multiple execution cores. This greatly enhances a system's capacity for concurrent execution of processes and threads — but concurrency is possible even on simple systems, without multiple processors or execution cores.

## 2. Processes and Threads

### 2.1. Processes

A process has a self-contained execution environment. A process generally has a complete, private set of basic run-time resources; in particular, each process has its own memory space.

Processes are often seen as synonymous with programs or applications. However, what the user sees as a single application may in fact be a set of cooperating processes. To facilitate communication between processes, most operating systems support Inter Process Communication (IPC) resources, such as pipes and sockets. IPC is used not just for communication between processes on the same system, but processes on different systems.

Most implementations of the Java virtual machine run as a single process. A Java application can create additional processes using a ProcessBuilder object. Multiprocess applications are beyond the scope of this lesson.

### 2.2. Threads

Threads are sometimes called lightweight processes. Both processes and threads provide an execution environment, but creating a new thread requires fewer resources than creating a new process.

Threads exist within a process — every process has at least one. Threads share the process's resources, including memory and open files. This makes for efficient, but potentially problematic, communication.

Multithreaded execution is an essential feature of the Java platform. Every application has at least one thread — or several, if you count "system" threads that do things like memory management and signal handling. But from the application programmer's point of view, you start with just one thread, called the main thread. This thread has the ability to create additional threads, as we'll demonstrate in the next section.

## 3. Thread Objects

Each thread is associated with an instance of the class Thread. There are two basic strategies for using Thread objects to create a concurrent application.

To directly control thread creation and management, simply instantiate Thread each time the application needs to initiate an asynchronous task.
To abstract thread management from the rest of your application, pass the application's tasks to an executor.

### 3.1. Defining and Starting a Thread

An application that creates an instance of Thread must provide the code that will run in that thread. There are two ways to do this:

Provide a Runnable object. The Runnable interface defines a single method, run, meant to contain the code executed in the thread. The Runnable object is passed to the Thread constructor, as in the HelloRunnable example:

```java
public class HelloRunnable implements Runnable {

    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        (new Thread(new HelloRunnable())).start();
    }

}
```

Subclass Thread. The Thread class itself implements Runnable, though its run method does nothing. An application can subclass Thread, providing its own implementation of run, as in the HelloThread example:

```java
public class HelloThread extends Thread {

    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        (new HelloThread()).start();
    }

}
```

Notice that both examples invoke Thread.start in order to start the new thread.

Which of these idioms should you use? The first idiom, which employs a Runnable object, is more general, because the Runnable object can subclass a class other than Thread. The second idiom is easier to use in simple applications, but is limited by the fact that your task class must be a descendant of Thread. This lesson focuses on the first approach, which separates the Runnable task from the Thread object that executes the task. Not only is this approach more flexible, but it is applicable to the high-level thread management APIs covered later.

The Thread class defines a number of methods useful for thread management. These include static methods, which provide information about, or affect the status of, the thread invoking the method. The other methods are invoked from other threads involved in managing the thread and Thread object. We'll examine some of these methods in the following sections.

### 3.2. Pausing Execution with Sleep

Thread.sleep causes the current thread to suspend execution for a specified period. This is an efficient means of making processor time available to the other threads of an application or other applications that might be running on a computer system. The sleep method can also be used for pacing, as shown in the example that follows, and waiting for another thread with duties that are understood to have time requirements, as with the SimpleThreads example in a later section.

Two overloaded versions of sleep are provided: one that specifies the sleep time to the millisecond and one that specifies the sleep time to the nanosecond. However, these sleep times are not guaranteed to be precise, because they are limited by the facilities provided by the underlying OS. Also, the sleep period can be terminated by interrupts, as we'll see in a later section. In any case, you cannot assume that invoking sleep will suspend the thread for precisely the time period specified.

The SleepMessages example uses sleep to print messages at four-second intervals:

```java
public class SleepMessages {
public static void main(String args[])
throws InterruptedException {
String importantInfo[] = {
"Mares eat oats",
"Does eat oats",
"Little lambs eat ivy",
"A kid will eat ivy too"
};

        for (int i = 0;
             i < importantInfo.length;
             i++) {
            //Pause for 4 seconds
            Thread.sleep(4000);
            //Print a message
            System.out.println(importantInfo[i]);
        }
    }

}
```

Notice that main declares that it throws InterruptedException. This is an exception that sleep throws when another thread interrupts the current thread while sleep is active. Since this application has not defined another thread to cause the interrupt, it doesn't bother to catch InterruptedException.

### 3.3. Joins

The join method allows one thread to wait for the completion of another. If t is a Thread object whose thread is currently executing,

`t.join();`

causes the current thread to pause execution until t's thread terminates. Overloads of join allow the programmer to specify a waiting period. However, as with sleep, join is dependent on the OS for timing, so you should not assume that join will wait exactly as long as you specify.

Like sleep, join responds to an interrupt by exiting with an InterruptedException.

## 4. Liveness

A concurrent application's ability to execute in a timely manner is known as its liveness. This section describes the most common kind of liveness problem, deadlock, and goes on to briefly describe two other liveness problems, starvation and livelock.

### 4.1. Deadlock

Deadlock describes a situation where two or more threads are blocked forever, waiting for each other. Here's an example.

Alphonse and Gaston are friends, and great believers in courtesy. A strict rule of courtesy is that when you bow to a friend, you must remain bowed until your friend has a chance to return the bow. Unfortunately, this rule does not account for the possibility that two friends might bow to each other at the same time. This example application, Deadlock, models this possibility:

```java
public class Deadlock {
    static class Friend {
        private final String name;
        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s"
                + "  has bowed to me!%n",
                this.name, bower.getName());
            bower.bowBack(this);
        }
        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s"
                + " has bowed back to me!%n",
                this.name, bower.getName());
        }
    }

    public static void main(String[] args) {
        final Friend alphonse =
            new Friend("Alphonse");
        final Friend gaston =
            new Friend("Gaston");
        new Thread(new Runnable() {
            public void run() { alphonse.bow(gaston); }
        }).start();
        new Thread(new Runnable() {
            public void run() { gaston.bow(alphonse); }
        }).start();
    }
}
```

When Deadlock runs, it's extremely likely that both threads will block when they attempt to invoke bowBack. Neither block will ever end, because each thread is waiting for the other to exit bow.

### 4.2. Starvation and Livelock

Starvation and livelock are much less common a problem than deadlock, but are still problems that every designer of concurrent software is likely to encounter.

#### 4.2.1. Starvation

Starvation describes a situation where a thread is unable to gain regular access to shared resources and is unable to make progress. This happens when shared resources are made unavailable for long periods by "greedy" threads. For example, suppose an object provides a synchronized method that often takes a long time to return. If one thread invokes this method frequently, other threads that also need frequent synchronized access to the same object will often be blocked.

#### 4.2.2. Livelock

A thread often acts in response to the action of another thread. If the other thread's action is also a response to the action of another thread, then livelock may result. As with deadlock, livelocked threads are unable to make further progress. However, the threads are not blocked — they are simply too busy responding to each other to resume work. This is comparable to two people attempting to pass each other in a corridor: Alphonse moves to his left to let Gaston pass, while Gaston moves to his right to let Alphonse pass. Seeing that they are still blocking each other, Alphone moves to his right, while Gaston moves to his left. They're still blocking each other, so...

## 5. Thread Example

```java
    public static void main(String[] args) throws InterruptedException {
//        MultithreadThing myThing = new MultithreadThing();
//        MultithreadThing myThing2 = new MultithreadThing();

        for(int i = 0; i <= 3; i++) {
            MultithreadThing myThing = new MultithreadThing(i);
            Thread myThread = new Thread(myThing);
            myThread.start();

            //myThread.join();
            //System.out.println(myThread.isAlive());
        }

//        myThing.start();
//        myThing2.start();
    }



public class MultithreadThing implements Runnable {

    private int threadNumber;

    public MultithreadThing(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        for(int i = 0; i <= 5; i++) {
            System.out.println(i + " from thread " + threadNumber);

//            if(threadNumber == 3) {
//                throw new RuntimeException();
//            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MultithreadThing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
```

## 6. Threadpool Example

```java
class WorkerThread implements Runnable {

    private String message;

    public WorkerThread(String s) {
        this.message = s;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " (Start) message = " + message);
        processmessage();   //call processmessage method that sleeps the thread for 2 seconds
        System.out.println(Thread.currentThread().getName() + " (End)");    //prints thread name
    }

    private void processmessage() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


public class TestThreadPool {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5); //creating a pool of 5 threads

        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread("" + i);
            executor.execute(worker);   //calling execute method of ExecutorService
        }
        executor.shutdown();

        while (!executor.isTerminated()) {
        }

        System.out.println("Finished all threads");
    }

}
```

## 7. Semaphore Example

```java
import java.util.concurrent.Semaphore;

/**
 *
 * @author hashemim
 */

class WorkerThread implements Runnable {

    private String message;
    private Semaphore semaphore;

    public WorkerThread(Semaphore semaphore, String s) {
        this.semaphore = semaphore;
        this.message = s;
    }

    public void run() {
        semaphore.acquireUninterruptibly();
        System.out.println(Thread.currentThread().getName() + " (Start) message = " + message);
        processmessage();   //call processmessage method that sleeps the thread for 2 seconds
        System.out.println(Thread.currentThread().getName() + " (End)");    //prints thread name
        semaphore.release();
    }

    private void processmessage() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 *
 * @author hashemim
 */
public class TestThreadPool {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        ExecutorService executor = Executors.newFixedThreadPool(10); //creating a pool of 10 threads

        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread(semaphore, "" + i);
            executor.execute(worker);   //calling execute method of ExecutorService
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
        }

        System.out.println("Finished all threads");
    }

}
```

## 8. ForkJoinPool Example

```java
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;

/**
 *
 * @author hashemim
 */
public class ForkBlur extends RecursiveAction {

    private int[] mSource;
    private int mStart;
    private int mLength;
    private int[] mDestination;
    private int mBlurWidth = 15; // Processing window size, should be odd.

    public ForkBlur(int[] src, int start, int length, int[] dst) {
        mSource = src;
        mStart = start;
        mLength = length;
        mDestination = dst;
    }

    // Average pixels from source, write results into destination.
    protected void computeDirectly() {
        int sidePixels = (mBlurWidth - 1) / 2;
        for (int index = mStart; index < mStart + mLength; index++) {
            // Calculate average.
            float rt = 0, gt = 0, bt = 0;
            for (int mi = -sidePixels; mi <= sidePixels; mi++) {
                int mindex = Math.min(Math.max(mi + index, 0), mSource.length - 1);
                int pixel = mSource[mindex];
                rt += (float) ((pixel & 0x00ff0000) >> 16) / mBlurWidth;
                gt += (float) ((pixel & 0x0000ff00) >> 8) / mBlurWidth;
                bt += (float) ((pixel & 0x000000ff) >> 0) / mBlurWidth;
            }

            // Re-assemble destination pixel.
            int dpixel = (0xff000000)
                    | (((int) rt) << 16)
                    | (((int) gt) << 8)
                    | (((int) bt) << 0);
            mDestination[index] = dpixel;
        }
    }
    protected static int sThreshold = 10000;

    @Override
    protected void compute() {
        if (mLength < sThreshold) {
            computeDirectly();
            return;
        }

        int split = mLength / 2;

        invokeAll(new ForkBlur(mSource, mStart, split, mDestination),
                new ForkBlur(mSource, mStart + split, mLength - split,
                mDestination));
    }

    // Plumbing follows.
    public static void main(String[] args) throws Exception {
        String srcName = "red-tulips.jpg";
        File srcFile = new File(srcName);
        BufferedImage image = ImageIO.read(srcFile);

        System.out.println("Source image: " + srcName);

        BufferedImage blurredImage = blur(image);

        String dstName = "blurred-tulips.jpg";
        File dstFile = new File(dstName);
        ImageIO.write(blurredImage, "jpg", dstFile);

        System.out.println("Output image: " + dstName);

    }

    public static BufferedImage blur(BufferedImage srcImage) {
        int w = srcImage.getWidth();
        int h = srcImage.getHeight();

        int[] src = srcImage.getRGB(0, 0, w, h, null, 0, w);
        int[] dst = new int[src.length];

        System.out.println("Array size is " + src.length);
        System.out.println("Threshold is " + sThreshold);

        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(Integer.toString(processors) + " processor"
                + (processors != 1 ? "s are " : " is ")
                + "available");

        ForkBlur fb = new ForkBlur(src, 0, src.length, dst);

        ForkJoinPool pool = new ForkJoinPool();

        long startTime = System.currentTimeMillis();
        pool.invoke(fb);
        long endTime = System.currentTimeMillis();

        System.out.println("Image blur took " + (endTime - startTime) +
                " milliseconds.");

        BufferedImage dstImage =
                new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        dstImage.setRGB(0, 0, w, h, dst, 0, w);

        return dstImage;
    }
}
```

## 9. Problem Example

### 9.1. Description

Suppose we have a class:

```java
public class Foo {
  public void first() { print("first"); }
  public void second() { print("second"); }
  public void third() { print("third"); }
}
```

The same instance of `Foo` will be passed to three different threads. Thread A will call `first()`, thread B will call `second()`, and thread C will call `third()`. Design a mechanism and modify the program to ensure that `second()` is executed after `first()`, and `third()` is executed after `second()`.

Note:

We do not know how the threads will be scheduled in the operating system, even though the numbers in the input seem to imply the ordering. The input format you see is mainly to ensure our tests' comprehensiveness.

Example 1:

```
Input: nums = [1,2,3]
Output: "firstsecondthird"
Explanation: There are three threads being fired asynchronously. The input [1,2,3] means thread A calls first(), thread B calls second(), and thread C calls third(). "firstsecondthird" is the correct output.
```

Example 2:

```
Input: nums = [1,3,2]
Output: "firstsecondthird"
Explanation: The input [1,3,2] means thread A calls first(), thread B calls third(), and thread C calls second(). "firstsecondthird" is the correct output.
```

**Constraints**

`nums` is a permutation of `[1, 2, 3]`.

```java
class Foo {

    public Foo() {

    }

    public void first(Runnable printFirst) throws InterruptedException {

        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
    }

    public void second(Runnable printSecond) throws InterruptedException {

        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
    }

    public void third(Runnable printThird) throws InterruptedException {

        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
```

### 9.2. Solution

**Problems of Concurrency**

```
The concurrency problems arise from the scenario of concurrent computing, where the execution of a program is conducted in multiple processes (or threads) simultaneously.
```

By simultaneousness, the processes or threads are not necessarily running independently in different physical CPUs, but more often they interleave in the same physical CPU. Note that, the concurrency could apply to either process or thread, we use the words of "process" and "thread" interchangeably in the following sections.

The concurrency is designed to above all enable multitasking, yet it could easily bring some bugs into the program if not applied properly. Depending on the consequences, the problems caused by concurrency can be categorized into three types:

- **race conditions:** the program ends with an undesired output, resulting from the sequence of execution among the processes.

- **deadlocks:** the concurrent processes wait for some necessary resources from each other. As a result, none of them can make progress.

- **resource starvation:** a process is perpetually denied necessary resources to progress its works.

In particular, our problem here can be attributed to the race conditions. Before diving into the solutions, we show an example of race condition.

Suppose we have a function called `withdraw(amount)` which deduces certain amount of money from the balance, if the demanding amount is less than the current balance. At the end, the function returns the remaining balance. The function is defined as follows:

```java
int balance = 500;
int withdraw(int amount) {
  if (amount < balance) {
    balance -= amount;
  }
  return balance;
}
```

As we can see, in the normal case, we expect that the `balance` would never become negative after the execution of the function, which is also the desired behavior of the function.

However, unfortunately we could run into a race condition where the `balance` becomes negative. Here is how it could happen. Imagine we have two threads invoking the function at the same time with different input parameters, e.g. for thread #1, `withdraw(amount=400)` and for thread #2, `withdraw(amount=200)`. The execution of the two threads is scheduled as the graph below, where at each time instance, we run exclusively only a statement from either threads.

![](../Imgs/week_11_problem_1.png)

As one can see, at the end of the above execution flow, we would end up with a negative balance, which is not a desired output.

**Race-free Concurrency**

The concurrency problems share one common characteristic: multiple processes/threads share some resources (e.g. the variable balance). Since we cannot eliminate the constraint of resource sharing, the key to prevent the concurrency problems boils down to the coordination of resource sharing.

The idea is that if we could ensure the exclusivity of certain critical code section (e.g. the statements to check and deduce the balance), we could prevent the program from running into certain inconsistent states.

`The solution to the race condition becomes clear: we need certain mechanism that could enforce the exclusivity of certain critical code section, i.e. at a given time, only one thread can enter the critical section.`

One can consider the mechanism as a sort of lock that restricts the access of the critical section. Following the previous example, we apply the lock on the critical section, i.e. the statements of balance check and balance deduction. We then rerun the two threads, which could lead to the following flow:

![](../Imgs/week_11_problem_2.png)

With the mechanism, once a thread enters the critical section, it would prevent other threads from entering the same critical section. For example, at the timestamp #3, the `thread #2` enters the critical section. Then at the next timestamp #4, the `thread #1` could have sneaked into the dangerous critical section if the statement was not protected by the lock. At the end, the two threads run concurrently, while the consistency of the system is maintained, i.e. the balance remains positive.

If the thread is not granted with the access of the critical section, we can say that the thread is blocked or put into sleep, e.g. the `thread #1` is blocked at the timestamp #4. As one can imagine, once the critical section is released, it would be nice to notify the waiting threads. For instance, as soon as the `thread #2` releases the critical section at the timestamp #5, the `thread #1` got notified to take over the critical section.

`As a result, it is often the case that the mechanism also comes with the capability to wake up those waiting peers.`

To summarize, in order to prevent the race condition in concurrency, we need a mechanism that possess two capabilities: 1). access control on critical section. 2). notification to the blocking threads.

**Approach: Pair Synchronization**

**Intuition**

The problem asks us to complete three jobs in order, while each job is running in a separated thread. In order to enforce the execution sequence of the jobs, we could create some dependencies between pairs of jobs, i.e. the second job should depend on the completion of the first job and the third job should depend on the completion of the second job.

The dependency between pairs of jobs construct a partial order on the execution sequence of all jobs, e.g. with `A < B`, `B < C`, we could obtain the sequence of `A < B < C`.

![](../Imgs/week_11_problem_3.png)

The dependency can be implemented by the concurrency mechanism as we discussed in the previous section. The idea is that we could use a shared variable named `firstJobDone` to coordinate the execution order between the first job and the second job. Similarly, we could use another variable `secondJobDone` to enforce the order of execution between the second and the third jobs.

**Algorithm**

- First of all, we initialize the coordination variables `firstJobDone` and `secondJobDone`, to indicate that the jobs are not done yet.

- In the `first()` function, we have no dependency so that we could get straight down to the job. At the end of the function, we then update the variable `firstJobDone` to indicate that the first job is done.

- In the `second()` function, we check the status of `firstJobDone`. If it is not updated, we then wait, otherwise we proceed to the task of the second job. And at the end of function, we update the variable `secondJobDone` to mark the completion of the second job.

- In the `third()` function, we check the status of the `secondJobDone`. Similarly as the `second()` function, we wait for the signal of the `secondJobDone`, before proceeding to the task of the third job.

![](../Imgs/week_11_problem_4.png)

**Implementation**

```java
class Foo {

  private AtomicInteger firstJobDone = new AtomicInteger(0);
  private AtomicInteger secondJobDone = new AtomicInteger(0);

  public Foo() {}

  public void first(Runnable printFirst) throws InterruptedException {
    // printFirst.run() outputs "first".
    printFirst.run();
    // mark the first job as done, by increasing its count.
    firstJobDone.incrementAndGet();
  }

  public void second(Runnable printSecond) throws InterruptedException {
    while (firstJobDone.get() != 1) {
      // waiting for the first job to be done.
    }
    // printSecond.run() outputs "second".
    printSecond.run();
    // mark the second as done, by increasing its count.
    secondJobDone.incrementAndGet();
  }

  public void third(Runnable printThird) throws InterruptedException {
    while (secondJobDone.get() != 1) {
      // waiting for the second job to be done.
    }
    // printThird.run() outputs "third".
    printThird.run();
  }
}
```
