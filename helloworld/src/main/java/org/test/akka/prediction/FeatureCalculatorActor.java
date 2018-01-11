package org.test.akka.prediction;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FeatureCalculatorActor extends AbstractActor{

    private ActorRef predictorActor = null;

    public FeatureCalculatorActor(ActorRef predictorActor) {
        this.predictorActor = predictorActor;
    }

    static public Props props(ActorRef predictorActor) {
        return Props.create(FeatureCalculatorActor.class, () -> new FeatureCalculatorActor(predictorActor));
    }

    public static class CalculateFeatureMessage {
        public final Long slipId;
        public final ActorRef resultActor;

        public CalculateFeatureMessage(Long slipId, ActorRef resultActor) {
            this.slipId = slipId;
            this.resultActor = resultActor;
        }
    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CalculateFeatureMessage.class, calculateFeatureMessage -> {
                    log.info("I was called by {} for slip id {}", getSender(), calculateFeatureMessage.slipId);
                    predictorActor.tell(new PredictorActor.PredictMessage(new FeatureCalculatorDB().calculate(calculateFeatureMessage.slipId), calculateFeatureMessage.resultActor), getSelf());
     //               context().stop(self());
                })
                .build();
    }
}
