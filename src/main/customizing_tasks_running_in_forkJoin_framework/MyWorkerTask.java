package main.customizing_tasks_running_in_forkJoin_framework;

import java.util.Date;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by G.Chalauri on 03/31/17.
 */
public abstract class MyWorkerTask extends ForkJoinTask<Void> {
    private String name;
    public MyWorkerTask(String name) {
        this.name=name;
    }

    /*
    This method is used to return the result of the task. As your tasks
    don't return any results, this method returns the null value.
     */
    @Override
    public Void getRawResult() {
        return null;
    }

    /*
    This method is used to establish the result of the task. As your
    tasks don't return any results, you leave this method empty.
     */
    @Override
    protected void setRawResult(Void value) {
    }

/*
    This is the main method of the task. In this case,
    delegate the logic of the task to the compute() method. Calculate the execution
    time of that method and write it in the console.
 */
    @Override
    protected boolean exec() {
        Date startDate=new Date();
        compute();
        Date finishDate=new Date();
        long diff=finishDate.getTime()-startDate.getTime();
        System.out.printf("MyWorkerTask: %s : %d Milliseconds to complete.\n",name,diff);

        return true;
    }

    public String getName(){
        return name;
    }

    protected abstract void compute();
}
