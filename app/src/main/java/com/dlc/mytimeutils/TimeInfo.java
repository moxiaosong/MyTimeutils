package com.dlc.mytimeutils;

import com.dyhdyh.support.countdowntimer.TimerState;

/**
 * @author : LGQ
 * @date : 2020/06/01 09
 * @desc :
 */

public class TimeInfo {
    private long duration;
    private long remainingTime;
    private TimerState state;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public TimerState getState() {
        return state;
    }

    public void setState(TimerState state) {
        this.state = state;
    }
}