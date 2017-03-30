package main.implementing_threadFactoryInterface;

import java.util.concurrent.ThreadFactory;

/**
 * Created by G.Chalauri on 3/30/2017.
 */
public class MyThreadFactory implements ThreadFactory {

    private int counter;
    private String prefix;

    public MyThreadFactory(String prefix) {
        this.prefix = prefix;
        counter = 1;
    }

    @Override
    public Thread newThread(Runnable r) {
        MyThread myThread = new MyThread(r, prefix + "-" + counter);
        counter++;
        return myThread;
    }
}
