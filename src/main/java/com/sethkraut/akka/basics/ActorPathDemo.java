package com.sethkraut.akka.basics;

import akka.actor.*;

/**
 * Demonstrates an actor path
 */
public class ActorPathDemo {
    private static class Println extends UntypedActor {
        public void onReceive(Object message) throws Throwable {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();

        actorSystem.actorOf(Props.create(ActorPathDemo.Println.class), "println");

        ActorRef println = actorSystem.actorFor("/user/println");

        println.tell("Hello, World", null);

        actorSystem.shutdown();
    }

}
