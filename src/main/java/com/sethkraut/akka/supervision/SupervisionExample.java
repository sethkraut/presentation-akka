package com.sethkraut.akka.supervision;

import akka.actor.*;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Demonstrates supervision strategies.
 */
public class SupervisionExample {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef boss = actorSystem.actorOf(Props.create(Supervisor.class), "boss");

        for (int i = 0; i < 10; i++) {
            boss.tell("Message " + i, null);
        }

    }

    private static class Supervisor extends UntypedActor {
        private ActorRef counter;

        @Override
        public void preStart() throws Exception {
            counter = context().actorOf(Props.create(Counter.class), "counter");
        }

        @Override
        public void onReceive(Object message) throws Throwable {
            counter.tell(message, sender());
        }

        @Override
        public SupervisorStrategy supervisorStrategy() {
            return new AllForOneStrategy(
                    3, // Max number of retries
                    Duration.apply(1, TimeUnit.MINUTES), // Within this time range
                    t -> SupervisorStrategy.stop() // Mapping of Throwable to handling
            );
        }
    }

    private static class Counter extends UntypedActor {
        private int i;

        @Override
        public void onReceive(Object message) throws Throwable {
            if (i > 3) {
                throw new IllegalStateException("Counter too high.");
            }
            System.out.println("Received " + (++i) + " message(s): " + message);
        }
    }
}
