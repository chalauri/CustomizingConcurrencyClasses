package main;

import main.customizing_tasks_running_in_forkJoin_framework.WTask;
import main.customizing_tasks_running_in_scheduled_thread_pool.MyScheduledThreadPoolExecutor;
import main.customizing_tasks_running_in_scheduled_thread_pool.ScTask;
import main.customizing_threadPoolExecutor_class.MyExecutor;
import main.customizing_threadPoolExecutor_class.SleepTwoSecondsTask;
import main.implementing_custom_lock_class.LTask;
import main.implementing_custom_lock_class.MyLock;
import main.implementing_own_atomic_object.ParkingCounter;
import main.implementing_own_atomic_object.Sensor1;
import main.implementing_own_atomic_object.Sensor2;
import main.implementing_priority_based_executor_class.MyPriorityTask;
import main.implementing_threadFactoryInterface.MyTask;
import main.implementing_threadFactoryInterface.MyThreadFactory;
import main.implementing_threadFactory_to_generate_custom_threads_forkAndJoin_framework.MyRecursiveTask;
import main.implementing_threadFactory_to_generate_custom_threads_forkAndJoin_framework.MyWorkerThreadFactory;
import main.implementing_transfer_queue_based_on_priorities.Consumer;
import main.implementing_transfer_queue_based_on_priorities.Event;
import main.implementing_transfer_queue_based_on_priorities.MyPriorityTransferQueue;
import main.implementing_transfer_queue_based_on_priorities.Producer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by G.Chalauri on 3/30/2017.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // customizeThreadPoolExecutorClassExample();
        // implementingPriorityBasedExecutorExample();
        // implementingThreadFactoryInterfaceExample();
        // usingThreadFactoryInExecutorExample();
        // customizingTasksRunningInScheduledThreadPoolExample();
        // threadFactoryToGenerateCustomForkJoinExample();
        // customizingTasksRunningInForkJoinFrameworkExample();
        // implementingCustomLockExample();
        // implementingTransferQueueBasedOnPrioritiesExample();
        implementingOwnAtomicObjectExample();
    }

    private static void implementingOwnAtomicObjectExample() throws InterruptedException {
        ParkingCounter counter = new ParkingCounter(5);

        Sensor1 sensor1 = new Sensor1(counter);
        Sensor2 sensor2 = new Sensor2(counter);
        Thread thread1 = new Thread(sensor1);
        Thread thread2 = new Thread(sensor2);
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.printf("Main: Number of cars: %d\n", counter.get());

        System.out.printf("Main: End of the program.\n");
    }

    private static void implementingTransferQueueBasedOnPrioritiesExample() throws InterruptedException {
        MyPriorityTransferQueue<Event> buffer = new MyPriorityTransferQueue<Event>();

        Producer producer = new Producer(buffer);
        Thread producerThreads[] = new Thread[10];
        for (int i = 0; i < producerThreads.length; i++) {
            producerThreads[i] = new Thread(producer);
            producerThreads[i].start();
        }

        Consumer consumer = new Consumer(buffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.
                getWaitingConsumerCount());

        Event myEvent = new Event("Core Event", 0);
        buffer.transfer(myEvent);
        System.out.printf("Main: My Event has ben transferred.\n");

        for (int i = 0; i < producerThreads.length; i++) {
            try {
                producerThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        TimeUnit.SECONDS.sleep(1);

        System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.
                getWaitingConsumerCount());

        myEvent = new Event("Core Event 2", 0);
        buffer.transfer(myEvent);

        consumerThread.join();

        System.out.printf("Main: End of the program\n");
    }


    private static void implementingCustomLockExample() {
        MyLock lock = new MyLock();
        for (int i = 0; i < 10; i++) {
            LTask task = new LTask("Task-" + i, lock);
            Thread thread = new Thread(task);
            thread.start();
        }

        boolean value;
        do {
            try {
                value = lock.tryLock(1, TimeUnit.SECONDS);
                if (!value) {
                    System.out.printf("Main: Trying to get the Lock\n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                value = false;
            }
        } while (!value);

        System.out.printf("Main: Got the lock\n");
        lock.unlock();

        System.out.printf("Main: End of the program\n");
    }


    private static void customizingTasksRunningInForkJoinFrameworkExample() {
        int array[] = new int[10000];
        ForkJoinPool pool = new ForkJoinPool();
        WTask task = new WTask("Task", array, 0, array.length);
        pool.invoke(task);
        pool.shutdown();
        System.out.printf("Main: End of the program.\n");
    }

    private static void threadFactoryToGenerateCustomForkJoinExample() throws InterruptedException, ExecutionException {
        MyWorkerThreadFactory factory = new MyWorkerThreadFactory();

        ForkJoinPool pool = new ForkJoinPool(4, factory, null, false);

        int array[] = new int[100000];
        for (int i = 0; i < array.length; i++) {
            array[i] = 1;
        }

        MyRecursiveTask task = new MyRecursiveTask(array, 0, array.
                length);

        pool.execute(task);

        task.join();

        pool.shutdown();

        pool.awaitTermination(1, TimeUnit.DAYS);

        System.out.printf("Main: Result: %d\n", task.get());

        System.out.printf("Main: End of the program\n");
    }

    private static void customizingTasksRunningInScheduledThreadPoolExample() throws InterruptedException {
        MyScheduledThreadPoolExecutor executor = new MyScheduledThreadPoolExecutor(2);
        ScTask task = new ScTask();
        System.out.printf("Main: %s\n", new Date());
        executor.schedule(task, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(3);
        task = new ScTask();
        System.out.printf("Main: %s\n", new Date());
        executor.scheduleAtFixedRate(task, 1, 3, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(10);

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);

        System.out.printf("Main: End of the program.\n");
    }

    private static void usingThreadFactoryInExecutorExample() throws InterruptedException {
        MyThreadFactory threadFactory = new MyThreadFactory
                ("MyThreadFactory");

        ExecutorService executor = Executors.newCachedThreadPool
                (threadFactory);

        MyTask task = new MyTask();
        executor.submit(task);

        executor.shutdown();

        executor.awaitTermination(1, TimeUnit.DAYS);

        System.out.printf("Main: End of the program.\n");
    }

    private static void implementingThreadFactoryInterfaceExample() throws InterruptedException {
        MyThreadFactory myFactory = new MyThreadFactory("MyThreadFactory");

        MyTask task = new MyTask();
        Thread thread = myFactory.newThread(task);

        thread.start();
        thread.join();

        System.out.printf("Main: Thread information.\n");
        System.out.printf("%s\n", thread);
        System.out.printf("Main: End of the example.\n");
    }

    private static void implementingPriorityBasedExecutorExample() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1,
                TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

        for (int i = 0; i < 4; i++) {
            MyPriorityTask task = new MyPriorityTask("Task " + i, i);
            executor.execute(task);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 4; i < 8; i++) {
            MyPriorityTask task = new MyPriorityTask("Task " + i, i);
            executor.execute(task);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: End of the program.\n");
    }

    private static void customizeThreadPoolExecutorClassExample() {
        MyExecutor myExecutor = new MyExecutor(2, 4, 1000, TimeUnit.
                MILLISECONDS, new LinkedBlockingDeque<Runnable>());

        List<Future<String>> results = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            SleepTwoSecondsTask task = new SleepTwoSecondsTask();
            Future<String> result = myExecutor.submit(task);
            results.add(result);
        }


        for (int i = 0; i < 5; i++) {
            try {
                String result = results.get(i).get();
                System.out.printf("Main: Result for Task %d : %s\n", i, result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        myExecutor.shutdown();

        for (int i = 5; i < 10; i++) {
            try {
                String result = results.get(i).get();
                System.out.printf("Main: Result for Task %d : %s\n", i, result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        try {
            myExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: End of the program.\n");
    }
}
