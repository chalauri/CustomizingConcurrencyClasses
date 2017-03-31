package main.implementing_own_atomic_object;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by G.Chalauri on 03/31/17.
 */
public class ParkingCounter extends AtomicInteger {

    private int maxNumber;

    public ParkingCounter(int maxNumber) {
        set(0);
        this.maxNumber = maxNumber;
    }

    public boolean carIn() {
        for (; ; ) {
            int value = get();
            if (value == maxNumber) {
                System.out.println("ParkingCounter: The parking lot is full. " + new Date() + " Thread " + Thread.currentThread().getId());
                return false;
            } else {
                int newValue = value + 1;
                boolean changed = compareAndSet(value, newValue);
                if (changed) {
                    System.out.println("ParkingCounter: A car has entered. " + new Date() + " Thread " + Thread.currentThread().getId());
                    return true;
                }
            }
        }
    }

    public boolean carOut() {
        for (; ; ) {
            int value = get();
            if (value == 0) {
                System.out.println("ParkingCounter: The parking lot is empty. " + new Date() + " Thread " + Thread.currentThread().getId());
                return false;
            } else {
                int newValue = value - 1;
                boolean changed = compareAndSet(value, newValue);
                if (changed) {
                    System.out.println("ParkingCounter: A car has gone out. " + new Date() + " Thread " + Thread.currentThread().getId());
                    return true;
                }
            }
        }
    }
}


/*
Use the compareAndSet() method to try to replace the old value by the new
one. If this method returns the true value, the old value you sent as a parameter
was the value of the variable, so it makes the change of values. The operation was
made in an atomic way as the carIn() method returns the true value. If the
compareAndSet() method returns the false value, the old value you sent as
a parameter is not the value of the variable (the other thread modified it), so the
operation can't be done in an atomic way. The operation begins again until it can be
done in an atomic way.
 */