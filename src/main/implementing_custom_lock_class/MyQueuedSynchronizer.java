package main.implementing_custom_lock_class;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by G.Chalauri on 03/31/17.
 */
public class MyQueuedSynchronizer extends AbstractQueuedSynchronizer {
    private AtomicInteger state;

    public MyQueuedSynchronizer() {
        state = new AtomicInteger(0);
    }

    /*
    This method tries to change the value
    of the state variable from zero to one. If it can, it returns the true value
    else it returns false.
     */
    @Override
    protected boolean tryAcquire(int arg) {
        return state.compareAndSet(0, 1);
    }

    /*
    This method tries to change the value
    of the state variable from one to zero. If it can, it returns the true value
    else it returns the false value.
     */
    @Override
    protected boolean tryRelease(int arg) {
        return state.compareAndSet(1, 0);
    }
}
