package main.implementing_transfer_queue_based_on_priorities;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by G.Chalauri on 03/31/17.
 */
public class MyPriorityTransferQueue<E> extends PriorityBlockingQueue<E>
        implements TransferQueue<E> {

    private AtomicInteger counter;
    private LinkedBlockingQueue<E> transfered;
    private ReentrantLock lock;

    public MyPriorityTransferQueue() {
        counter = new AtomicInteger(0);
        lock = new ReentrantLock();
        transfered = new LinkedBlockingQueue<E>();
    }

    @Override
    public boolean tryTransfer(E e) {
        lock.lock();
        boolean value;
        if (counter.get() == 0) {
            value = false;
        } else {
            put(e);
            value = true;
        }
        lock.unlock();
        return value;
    }

    /*
    This method tries to send the element to
    a waiting consumer immediately, if possible. If there isn't a waiting consumer,
    the method stores the element in a special queue to be sent to the first consumer
    that tries to get an element and blocks the thread until the element is consumed.
     */
    @Override
    public void transfer(E e) throws InterruptedException {
        lock.lock();
        if (counter.get() != 0) {
            put(e);
            lock.unlock();
        } else {
            transfered.add(e);
            lock.unlock();
            synchronized (e) {
                e.wait();
            }
        }
    }

    /*
    The element, the time to wait for a consumer, if there is none, and the unit of time used
    to specify that time. If there is a consumer waiting, it sends the element immediately.
    Otherwise, convert the time specified to milliseconds and use the wait() method
    to put the thread to sleep. When the consumer takes the element, if the thread is
    sleeping in the wait() method, you are going to wake it up using the notify()
    method as you'll see in a moment.
     */
    @Override
    public boolean tryTransfer(E e, long timeout, TimeUnit unit)
            throws InterruptedException {
        lock.lock();
        if (counter.get() != 0) {
            put(e);
            lock.unlock();
            return true;
        } else {
            transfered.add(e);
            long newTimeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
            lock.unlock();
            e.wait(newTimeout);
            lock.lock();
            if (transfered.contains(e)) {
                transfered.remove(e);
                lock.unlock();
                return false;
            } else {
                lock.unlock();
                return true;
            }
        }
    }

    /*
    Use the value of the counter
    attribute to calculate the return value of this method. If the counter has a value
    bigger than zero, return true. Else, return false.
     */
    @Override
    public boolean hasWaitingConsumer() {
        return (counter.get() != 0);
    }

    @Override
    public int getWaitingConsumerCount() {
        return counter.get();
    }

    /*
    If there aren't any elements in the transferred queue, free the lock and try to get an
    element from the queue using the take() element and get the lock again. If there
    aren't any elements in the queue, this method will put the thread to sleep until there
    are elements to consume.
     */
    @Override
    public E take() throws InterruptedException {
        lock.lock();
        counter.incrementAndGet();
        E value = transfered.poll();
        if (value == null) {
            lock.unlock();
            value = super.take();
            lock.lock();
        } else {
            synchronized (value) {
                value.notify();
            }
        }

        counter.decrementAndGet();
        lock.unlock();
        return value;
    }
}
