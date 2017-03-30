package main.implementing_threadFactoryInterface;

import java.util.concurrent.TimeUnit;

/**
 * Created by G.Chalauri on 3/30/2017.
 */
public class MyTask implements Runnable {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}