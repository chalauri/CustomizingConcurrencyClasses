package main.customizing_tasks_running_in_forkJoin_framework;

/**
 * Created by G.Chalauri on 03/31/17.
 */
public class WTask extends MyWorkerTask {

    private int array[];
    private int start;
    private int end;

    public WTask(String name, int array[], int start, int end) {
        super(name);
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start > 100) {
            int mid = (end + start) / 2;
            WTask task1 = new WTask(this.getName() + "1", array, start, mid);
            WTask task2 = new WTask(this.getName() + "2", array, mid, end);
            invokeAll(task1, task2);
        } else {
            for (int i = start; i < end; i++) {
                array[i]++;
            }
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
