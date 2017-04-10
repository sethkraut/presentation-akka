package com.sethkraut.akka.become;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Become demonstration. Uses become to track state.
 */
public class BecomeCounterDemo {
    private static class Counter extends AbstractActor {
        public Counter() {
            receive(counter(0));
        }

        private PartialFunction<Object, BoxedUnit> counter(int i) {
            return ReceiveBuilder.match(String.class, s -> {
                        System.out.println(i + ": " + s);
                        this.context().become(counter(i + 1));
                    })
                    .matchAny(System.out::println)
                    .build();
        }

        public void onReceive(Object message) throws Throwable {
            // Not used.
            System.out.println("Received " + message);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef counter = actorSystem.actorOf(Props.create(Counter.class), "counter");

        for (int i = 0; i < 10; i++) {
            counter.tell("Message", null);
        }

        Thread.sleep(1000);

        actorSystem.shutdown();
    }
}
