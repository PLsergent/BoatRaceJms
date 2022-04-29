# BoatRaceJms
CLI App with Jms API, simulating a boat race.


## Concept

The idea of the app is pretty straight forward:
we have a boat race. The time left to finish the race for each boat is determined
at the beginning randomly.

Once all the boats have a time left of 0 the race is over.

To simulate the time ticking we have a while loop with a sleep
between each iteration, in this loop we will update the ranking
if necessary.

## JMS

A queue and topic between the organizer and each boat.

**Queue:**
- ***Producers***: boats
- ***Consumer***: organizer

Data: all boat ids

The organizer can then display the ranking and send a topic message if a boat has finished the race.

---

**Topic:**
- ***Producer***: organizer
- ***Consumers***: boats

Data: id of the boat who just finished

When the remaining boats receive the information that a boat has finished the race
they'll increase their speed accordingly to their ranks (velocity = velocity + rank). The lower
the rank is, the bigger the acceleration is.

## Classes

### Core

**Boat**

Class representing a boat in the race.

- *id*
- *totalTimeToFinish*: time left at the beginning of the race
- *timeLeft*: time left to finish after n loop
- *finished*: true if finished the race
- *velocity*: speed of the boat, represent the amount of seconds
retrieved from the timeLeft after each iteration, default: 1
- *race*: race object

---

**Organizer**

Class representing the organizer of the race. It will simply
handle the queue and topic messages, and displaying the current rank of the race.

- *race*: race object

---

**Race**

Class representing the actual boat race. It will initiate the boats,
iterate over the race while loop, and update the ranking.
This is also where we will initiate the Jms connection, the queue and the topic.
The method timeTicking() will take care of the rank update, boats timeLeft changes and
to send the queue messages to the organizer.

- *runners*: list of boat objects in the race
- *ranking*: list of the boat objects ranked
- *raceFinished*: if true program terminate
- *organizer*: organizer object
- *rankingQueue*: queue object, of the boats who will send their info to the organizer
- *finishedTopic*: topic object, of the organizer sending the id of the boat who just finished

### JMS

*Homemade classes*

**JmsFactory**

Class that will take care of the core operation of the JMS API.

---

**JmsQueue**

Class extending the JmsFactory and implements the method necessary
to initiate a queue.

---

**JmsTopic**

Class extending the JmsFactory and implements the method necessary
to initiate a topic.