package com.app.egguncle.weiegg.entities.weibo;

/**
 * Created by egguncle on 16.10.13.
 */
import java.util.List;

public class WeiBoRoot {
    private List<Statuses> statuses;

    private boolean hasvisible;

    private long previous_cursor;

    private long next_cursor;

    private int total_number;

    private int interval;

    public void setStatuses(List<Statuses> statuses) {
        this.statuses = statuses;
    }

    public List<Statuses> getStatuses() {
        return this.statuses;
    }

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
    }

    public boolean getHasvisible() {
        return this.hasvisible;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public long getPrevious_cursor() {
        return this.previous_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public long getNext_cursor() {
        return this.next_cursor;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public int getTotal_number() {
        return this.total_number;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return this.interval;
    }
}
