package main.implementing_threadFactory_to_generate_custom_threads_forkAndJoin_framework;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by G.Chalauri on 3/30/2017.
 */
public class MyRecursiveTask extends RecursiveTask<Integer> {

    private int array[];
    private int start, end;

    public MyRecursiveTask(int array[], int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    private int summarize(int start, int end) {
        int res = 0;
        for (int i = start; i < end; i++) {
            res += array[i];
        }

        return res;
    }

    @Override
    protected Integer compute() {
        Integer ret = 0;

        MyWorkerThread thread = (MyWorkerThread) Thread.currentThread();
        thread.addTask();

        if (end - start < 50_000) {
            return summarize(start, end);
        } else {
            int middle = (start + end) / 2;
            System.out.printf("Task: Pending tasks: %s\n", getQueuedTaskCount());
            MyRecursiveTask t1 = new MyRecursiveTask(array, start, middle + 1);
            MyRecursiveTask t2 = new MyRecursiveTask(array, middle + 1, end);
            invokeAll(t1, t2);
            ret = addResults(t1, t2);
        }

        return ret;
    }

    private Integer addResults(MyRecursiveTask task1, MyRecursiveTask task2) {
        int value;
        try {
            value = task1.get().intValue() + task2.get().intValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
            value = 0;
        } catch (ExecutionException e) {
            e.printStackTrace();
            value = 0;
        }

        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return value;
    }
}
