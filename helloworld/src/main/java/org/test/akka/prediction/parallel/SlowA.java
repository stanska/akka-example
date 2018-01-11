package org.test.akka.prediction.parallel;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SlowA extends AbstractActor {
    static public Props props() {
        return Props.create(SlowA.class, () -> new SlowA());
    }

    public static class StartMessageA {
    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartMessageA.class, startMessageA -> {
                    log.info("I((SlowA) was called by {}", getSender());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getSender().tell(new MasterActorParallel.FinalMessageA(100d), getSelf());
                })
                .build();
    }
}
