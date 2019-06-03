package com.yun.base.limit.service;

/**
 * @author: yun
 * @createdOn: 2019-06-03 10:06.
 */

public class YunCounterLimit {
    private int count = 0;
    private int limitCount = 100;
    private long duration = 1; // 1s
    private long timeStamp = System.currentTimeMillis() / 1000; // 秒

    public YunCounterLimit() {
    }

    public YunCounterLimit(int limitCount, long duration) {
        this.limitCount = limitCount;
        this.duration = duration;
    }

    public synchronized boolean grant() {
        long now = System.currentTimeMillis() / 1000;
        if (now < timeStamp + duration) {
            count++;
            return count < limitCount;
        } else {
            timeStamp = now;
            count = 1;
            return true;
        }
    }

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
