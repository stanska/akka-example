package org.test.akka.prediction.parallel;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SlowB extends AbstractActor {
    static public Props props() {
        return Props.create(SlowB.class, () -> new SlowB());
    }

    public static class StartMessageB {
    }
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartMessageB.class, startMessageB -> {
                    log.info("I(SlowB) was called by {}", getSender());
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getSender().tell(new MasterActorParallel.FinalMessageB(200d), getSelf());
                })
                .build();
    }
}
