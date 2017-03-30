package main.customizing_tasks_running_in_scheduled_thread_pool;

import java.util.concurrent.TimeUnit;

/**
 * Created by G.Chalauri on 3/30/2017.
 */
public class ScTask implements Runnable {
    @Override
    public void run() {
        System.out.printf("Task: Begin.\n");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Task: End.\n");
    }
}
