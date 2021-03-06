package me.travis.wurstplus.wurstplustwo.util;

public class BadTimer {
    private long time;

    public BadTimer()
    {
        time = -1;
    }

    public boolean passed(double ms)
    {
        return System.currentTimeMillis() - this.time >= ms;
    }

    public void reset()
    {
        this.time = System.currentTimeMillis();
    }

    public void resetTimeSkipTo(long p_MS)
    {
        this.time = System.currentTimeMillis() + p_MS;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }
}
