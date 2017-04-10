package com.sethkraut.akka.basics;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Demonstrates a simple actor
 */
public class HelloWorld {
    private static class Println extends UntypedActor {
        public void onReceive(Object message) throws Throwable {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef println = actorSystem.actorOf(Props.create(Println.class), "println");
        println.tell("Hello, World", null);
        actorSystem.shutdown();
    }
}
