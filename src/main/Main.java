package main;

import main.customizing_threadPoolExecutor_class.MyExecutor;
import main.customizing_threadPoolExecutor_class.SleepTwoSecondsTask;
import main.implementing_priority_based_executor_class.MyPriorityTask;
import main.implementing_threadFactoryInterface.MyTask;
import main.implementing_threadFactoryInterface.MyThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by G.Chalauri on 3/30/2017.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        // customizeThreadPoolExecutorClassExample();
        // implementingPriorityBasedExecutorExample();
        // implementingThreadFactoryInterfaceExample();
        usingThreadFactoryInExecutorExample();
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
