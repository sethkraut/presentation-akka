package com.sethkraut.akka.become;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Become demonstration.
 */
public class BecomeDemo {
    private enum Flip {
        UP,
        DOWN;
    }

    private static class LightSwitch extends UntypedActor {
        public LightSwitch() {
            PartialFunction<Object, BoxedUnit> on =
                    ReceiveBuilder.matchEquals(Flip.DOWN, flip -> {
                        System.out.println("Turn off");
                        this.context().unbecome();
                    }).build();

            // Starts as off
            this.context().become(
                    ReceiveBuilder.matchEquals(Flip.UP, flip -> {
                        System.out.println("Turn on");
                        this.context().become(on, false);
                    }).build(),
                true
            );
        }

        public void onReceive(Object message) throws Throwable {
            // Not used.
            System.out.println("Received " + message);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef lightSwitch = actorSystem.actorOf(Props.create(LightSwitch.class), "lightSwitch");

        lightSwitch.tell(Flip.UP, null);
        lightSwitch.tell(Flip.UP, null);
        lightSwitch.tell(Flip.DOWN, null);
        lightSwitch.tell(Flip.DOWN, null);

        Thread.sleep(1000);

        actorSystem.shutdown();
    }
}
