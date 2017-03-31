package main.implementing_custom_lock_class;

import java.util.concurrent.TimeUnit;

/**
 * Created by G.Chalauri on 03/31/17.
 */
public class LTask implements Runnable {
    private MyLock lock;
    private String name;

    public LTask(String name, MyLock lock) {
        this.lock = lock;
        this.name = name;
    }

    @Override
    public void run() {
        lock.lock();
        System.out.printf("Task: %s: Take the lock\n", name);
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.printf("Task: %s: Free the lock\n", name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
