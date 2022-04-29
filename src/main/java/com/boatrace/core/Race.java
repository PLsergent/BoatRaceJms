package com.boatrace.core;

import com.boatrace.jms.JmsQueue;
import com.boatrace.jms.JmsTopic;

import javax.jms.JMSException;
import java.util.ArrayList;

public class Race {
    public ArrayList<Boat> runners;
    public ArrayList<Boat> ranking;
    public boolean raceFinished;
    public Organizer organizer;
    public JmsQueue<Organizer> rankingQueue;
    public JmsTopic<Boat> finishedTopic;

    public Race(int numberOfBoats) throws Exception {
        this.organizer = new Organizer(this);
        initRace(numberOfBoats);
    }

    public void updateRanking() {
        ranking.sort((b1, b2) -> (int) (b1.timeLeft.getSeconds() - b2.timeLeft.getSeconds()));
    }

    public void initBoats(int numberOfBoats) throws Exception {
        this.runners = new ArrayList<>();

        for(int i = 0; i < numberOfBoats; i++) {
            Boat boat = new Boat(i, this);
            runners.add(boat);
        }

        this.rankingQueue = new JmsQueue<>("rankBoatQueue", 61616);
        this.rankingQueue.subscribe(organizer);

        this.finishedTopic = new JmsTopic<>("finishedBoatTopic", 61617);
        this.finishedTopic.subscribe(runners);

        this.rankingQueue.startConnection();
        this.finishedTopic.startConnection();

        ranking = runners;
    }

    public void timeTicking() throws JMSException {
        int totalTimeLeft = 0;

        for (Boat boat : ranking) {
            boat.timeTicking();
            this.rankingQueue.createOrReplaceMessage(String.valueOf(boat.id));
            this.rankingQueue.send();
            totalTimeLeft += boat.timeLeft.getSeconds();
        }
        System.out.println();
        this.raceFinished = totalTimeLeft == 0;
        updateRanking();
    }

    public Boat getBoatById(int id) {
        for (Boat boat : runners) {
            if (boat.id == id) {
                return boat;
            }
        }
        return null;
    }

    public void initRace(int numberOfBoats) throws Exception {
        initBoats(numberOfBoats);

        while (!raceFinished) {
            Thread.sleep(2000);
            timeTicking();
        }

        System.out.println("Race finished!");
        this.rankingQueue.closeConnection();
        this.finishedTopic.closeConnection();
    }
}
