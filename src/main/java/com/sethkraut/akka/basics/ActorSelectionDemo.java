package com.sethkraut.akka.basics;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Demonstrates an actor selection
 */
public class ActorSelectionDemo {
    private static class Println extends UntypedActor {
        private String name;

        public Println(String name) {
            this.name = name;
        }

        public void onReceive(Object message) throws Throwable {
            System.out.println(name + ": " + message);
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();

        actorSystem.actorOf(Props.create(ActorSelectionDemo.Println.class, "First"), "first");
        actorSystem.actorOf(Props.create(ActorSelectionDemo.Println.class, "Second"), "second");

        actorSystem.actorSelection("/user/*").tell("Hi", null);

        actorSystem.shutdown();
    }
}
