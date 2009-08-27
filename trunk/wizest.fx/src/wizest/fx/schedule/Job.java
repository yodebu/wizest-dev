package wizest.fx.schedule;

import java.io.Serializable;


public class Job implements Serializable
{
    private String name;
    private ScheduledRunnable runnable;
    private AtTime at;
    private int priority; // should be 1~10

    final public static int MAX_PRIORITY=Thread.MAX_PRIORITY;
    final public static int MIN_PRIORITY=Thread.MIN_PRIORITY;
    final public static int NORM_PRIORITY=Thread.NORM_PRIORITY;

    public Job(String jobName,ScheduledRunnable runnable,AtTime at)
    {
        this(jobName,runnable,at,NORM_PRIORITY);
    }

    public Job(String jobName,ScheduledRunnable runnable,AtTime at,int priority)
    {
        this.name=jobName;
        this.runnable=runnable;
        this.at=at;
        this.priority=priority;
    }

    public String getName()
    {
        return this.name;
    }

    public ScheduledRunnable getScheduledRunnable()
    {
        return this.runnable;
    }

    public void setScheduledRunnable(ScheduledRunnable runnable)
    {
        this.runnable=runnable;
    }

    public AtTime getAtTime()
    {
        return this.at;
    }

    public void setAtTime(AtTime at)
    {
        this.at=at;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public void setPriority(int priority)
    {
        this.priority=priority;
    }

    public String toString()
    {
        return "{Job Name=" + this.getName() + ",ScheduledRunnable=" + this.runnable.toString() + ",AtTime=" +
            this.at.toString() + ",priority=" + this.priority + "}";
    }
}