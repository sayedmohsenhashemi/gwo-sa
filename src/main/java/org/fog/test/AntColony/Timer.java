package org.fog.test.AntColony;

public class Timer {

    private long startTime;

    void start_timers() {
        startTime = System.currentTimeMillis();
    }

    double elapsed_time() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }

}
