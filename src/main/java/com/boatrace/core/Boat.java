package com.boatrace.core;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.time.Duration;
import java.util.Random;

public class Boat implements MessageListener {
    public int id;
    public Duration totalTimeToFinish;
    public Duration timeLeft;
    public boolean finished;
    public int velocity;
    public Race race;

    public Boat(int id, Race race) {
        this.id = id;
        this.totalTimeToFinish = Duration.ofSeconds(new Random().nextInt(60));
        this.timeLeft = totalTimeToFinish;
        this.finished = false;
        this.velocity = 1;
        this.race = race;
    }

    public void timeTicking() {
        if (finished) {
            return;
        }
        this.timeLeft = timeLeft.minus(Duration.ofSeconds(velocity));

        if (timeLeft.getSeconds() <= 0) {
            finished = true;
            timeLeft = Duration.ofSeconds(0);
        }
    }

    public void accelerate(int times) {
        velocity += times;
    }

    public String toString() {
        if (finished) {
            return "\033[0;1m u" + id + " : " + timeLeft.getSeconds() + "s\033[0m";
        }
        else {
            return "u" + id + " : " + timeLeft.getSeconds() + "s";
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (Integer.parseInt(((TextMessage) message).getText()) != this.id) {
                this.accelerate(race.ranking.indexOf(this));
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
