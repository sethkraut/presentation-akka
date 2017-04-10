package com.sethkraut.akka.patterns;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.concurrent.Future;

import static akka.pattern.Patterns.*;
import static akka.pattern.Patterns.ask;

/**
 * Demonstrates a simple actor
 */
public class PipeDemo {
    private static class Namer extends UntypedActor {
        public void onReceive(Object message) throws Throwable {
            sender().tell(message + " Seth" , self());
        }
    }

    private static class Println extends UntypedActor {
        public void onReceive(Object message) throws Throwable {
            System.out.println(message);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef namer = actorSystem.actorOf(Props.create(Namer.class), "namer");
        ActorRef println = actorSystem.actorOf(Props.create(Println.class), "println");

        // Send message to namer
        Future<Object> ask = ask(namer, "Hello", 500);

        // Pipe returned message to println
        pipe(ask, actorSystem.dispatcher()).pipeTo(println, null);

        Thread.sleep(500);

        actorSystem.shutdown();
    }
}
