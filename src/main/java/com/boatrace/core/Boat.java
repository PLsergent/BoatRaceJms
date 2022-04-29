package com.boatrace.core;

import java.time.Duration;
import java.util.Random;

public class Boat {
    public int id;
    public Duration totalTimeToFinish;
    public Duration timeLeft;
    public boolean finished;
    public int velocity;

    public Boat(int id) {
        this.id = id;
        this.totalTimeToFinish = Duration.ofSeconds(new Random().nextInt(60));
        this.timeLeft = totalTimeToFinish;
        this.finished = false;
        this.velocity = 1;
    }

    public void timeTicking() {
        if (finished) {
            return;
        }
        this.timeLeft = timeLeft.minus(Duration.ofSeconds(velocity));

        if (timeLeft.getSeconds() == 0) {
            finished = true;
        }
    }

    public void accelerate() {
        velocity++;
    }

    public String toString() {
        if (finished) {
            return "\033[0;1m u" + id + " : " + timeLeft.getSeconds() + "s\033[0m";
        }
        else {
            return "u" + id + " : " + timeLeft.getSeconds() + "s";
        }
    }
}
