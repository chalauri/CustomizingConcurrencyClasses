package main.implementing_transfer_queue_based_on_priorities;

/**
 * Created by G.Chalauri on 03/31/17.
 */
public class Producer implements Runnable {
    private MyPriorityTransferQueue<Event> buffer;

    public Producer(MyPriorityTransferQueue<Event> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            Event event = new Event(Thread.currentThread().getName(), i);
            buffer.put(event);
        }
    }
}
