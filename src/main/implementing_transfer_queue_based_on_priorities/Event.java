package main.implementing_transfer_queue_based_on_priorities;

/**
 * Created by G.Chalauri on 03/31/17.
 */
public class Event implements Comparable<Event> {
    private String thread;
    private int priority;


    public Event(String thread, int priority) {
        this.thread = thread;
        this.priority = priority;
    }

    public String getThread() {
        return thread;
    }

    public int getPriority() {
        return priority;
    }

    public int compareTo(Event e) {
        if (this.priority > e.getPriority()) {
            return -1;
        } else if (this.priority < e.getPriority()) {
            return 1;
        } else {
            return 0;
        }
    }
}
