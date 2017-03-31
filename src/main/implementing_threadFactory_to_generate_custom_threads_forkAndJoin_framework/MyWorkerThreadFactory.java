package main.implementing_threadFactory_to_generate_custom_threads_forkAndJoin_framework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * Created by G.Chalauri on 3/30/2017.
 */
public class MyWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        return new MyWorkerThread(pool);
    }
}
