package com.sethkraut.akka.patterns;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.JavaPartialFunction;
import akka.pattern.Patterns;

/**
 * Ask provides the ability to expect a response. Ask allows for sending a message and provides a Future that will
 * receive the response. Under the covers, this creates an Actor to manage the request and response.
 */
public class AskDemo {

    /**
     * Partial functions are functions that don't apply to all inputs. Scala has first class support for them.
     * Akka works around this by calling the function twice. Once with isCheck true. Again to actually calculate a value.
     */
    public static final JavaPartialFunction<Object, Object> PRINTLN = new JavaPartialFunction<Object, Object>() {
        public Object apply(Object x, boolean isCheck) throws Exception {
            if (isCheck) return null;

            System.out.println(x);
            return null;
        }
    };

    private static class Println extends UntypedActor {
        public void onReceive(Object message) throws Throwable {
            sender().tell("I heard '" + message + "'", self());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef println = actorSystem.actorOf(Props.create(Println.class), "println");
        Patterns.ask(println, "Hello", 500).onSuccess(PRINTLN, actorSystem.dispatcher());
        Thread.sleep(500);

        actorSystem.shutdown();
    }
}
