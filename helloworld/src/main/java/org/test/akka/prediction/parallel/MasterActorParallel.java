package org.test.akka.prediction.parallel;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.test.akka.prediction.FeatureCalculatorActor;

public class MasterActorParallel extends AbstractActor {

    static public Props props() {
        return Props.create(MasterActorParallel.class, () -> new MasterActorParallel());
    }

    public static class StartMessage {
        public final ActorRef slowA;
        public final ActorRef slowB;

        public StartMessage(ActorRef slowA, ActorRef slowB) {
            this.slowA = slowA;
            this.slowB = slowB;
        }
    }

    public static class FinalMessage{ Double value;}

    public static class FinalMessageA extends FinalMessage {

        public FinalMessageA(Double value) {
            this.value = value;
        }
    }

    public static class FinalMessageB extends FinalMessage {

        public FinalMessageB(Double value) {
            this.value = value;
        }
    }

//    public static class FinalMessage {
//        public final Double value;
//
//        public FinalMessage(Double value) {
//            this.value = value;
//        }
//    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartMessage.class, startMessage -> {
                    log.info("I was called by {}", getSender());
//                    startMessage.slowA.tell(new SlowA.StartMessageA(getSender()),  getSelf());
//                    startMessage.slowA.tell(new SlowB.StartMessageB(getSender()),  getSelf());
                    //               context().stop(self());
                })
//                .match(FinalMessageA.class, finalMessageA -> {
//                    log.info("I was called by {} fronm loadA {}", getSender(), finalMessageA.value);
//                    //               context().stop(self());
//                     probabilityMessage.resultActor.tell(probabilityMessage, getSelf());
//                })
//                .match(FinalMessageB.class, finalMessageB -> {
//                    log.info("I was called by {} from loadB {}", getSender(), finalMessageB.value);
//                    //               context().stop(self());
//                    probabilityMessage.resultActor.tell(probabilityMessage, getSelf());
//                })
                .build();
    }

}
