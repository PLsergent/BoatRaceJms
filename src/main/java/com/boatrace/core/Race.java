package main.java.com.boatrace.core;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Race {
    public ArrayList<Boat> runners;
    public ArrayList<Boat> ranking;
    public boolean raceFinished;

    public Race(int numberOfBoats) throws InterruptedException {
        initRace(numberOfBoats);
    }

    public void updateRanking() {
        runners.sort((b1, b2) -> (int) (b1.timeLeft.getSeconds() - b2.timeLeft.getSeconds()));
    }

    public void initBoats(int numberOfBoats) {
        this.runners = new ArrayList<>();
        for(int i = 0; i < numberOfBoats; i++) {
            runners.add(new Boat(i));
        }
        ranking = runners;
    }

    public void timeTicking() {
        int totalTimeLeft = 0;

        for (Boat boat : runners) {
            boat.timeTicking();
            totalTimeLeft += boat.timeLeft.getSeconds();
        }
        this.raceFinished = totalTimeLeft == 0;
        updateRanking();
    }

    public void displayRanking() {
        System.out.print("[ ");
        for (Boat boat : runners) {
            System.out.print(boat.toString() + "; ");
        }
        System.out.println("]");
    }

    public void initRace(int numberOfBoats) throws InterruptedException {
        initBoats(numberOfBoats);

        while (!raceFinished) {
            Thread.sleep(100);
            timeTicking();
            displayRanking();
        }
        System.out.println("Race finished!");
    }
}
