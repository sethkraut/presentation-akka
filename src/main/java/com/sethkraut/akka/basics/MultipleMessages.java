package com.sethkraut.akka.basics;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Shows an actor responding to different messages.
 */
public class MultipleMessages {
    private static abstract class GreetingMessage {
        private final String name;

        private GreetingMessage(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class HelloMessage extends GreetingMessage {

        private HelloMessage(String name) {
            super(name);
        }
    }

    private static class GoodbyeMessage extends GreetingMessage {

        private GoodbyeMessage(String name) {
            super(name);
        }
    }

    private static class Greeter extends AbstractActor {

        @Override
        public PartialFunction<Object, BoxedUnit> receive() {
            return ReceiveBuilder
                    .match(HelloMessage.class, m -> {
                        System.out.println("Hello " + m.getName());
                    })
                    .match(GoodbyeMessage.class, m -> {
                        System.out.println("Goodbye " + m.getName());
                    }).build();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef greeter = actorSystem.actorOf(Props.create(Greeter.class), "Greeter");
        greeter.tell(new HelloMessage("Seth"), null);
        greeter.tell(new GoodbyeMessage("Seth"), null);
        Thread.sleep(500);
        actorSystem.shutdown();
    }
}
