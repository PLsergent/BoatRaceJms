package com.boatrace.core;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Organizer implements MessageListener {
    public Race race;

    public Organizer(Race race){
        this.race = race;
    }

    @Override
    public void onMessage(Message message) {
        try {
            Boat boat = race.getBoatById(Integer.parseInt(((TextMessage) message).getText()));
            System.out.print(boat.toString() + "; ");

            if (boat.timeLeft.getSeconds() <= 0) {
                race.finishedTopic.createOrReplaceMessage(String.valueOf(boat.id));
                race.finishedTopic.send();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
