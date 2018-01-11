package org.test.akka.prediction.synchro;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.test.akka.prediction.FeatureCalculatorActor;
import org.test.akka.prediction.PredictorActor;

import java.util.HashMap;

public class MasterActor extends AbstractActor {

    static public Props props() {
        return Props.create(MasterActor.class, () -> new MasterActor());
    }

    public static class StartMessage {
        public final Long slipId;
        public final ActorRef featureCalculatorActor;
        public final ActorRef predictorActor;

        public StartMessage(Long slipId, ActorRef featureCalculatorActor, ActorRef predictorActor) {
            this.slipId = slipId;
            this.featureCalculatorActor = featureCalculatorActor;
            this.predictorActor = predictorActor;
        }
    }

    public static class ProbabilityMessage {
        public final Double probability;
        public final ActorRef resultActor;

        public ProbabilityMessage(Double probability, ActorRef resultActor) {
            this.probability = probability;
            this.resultActor = resultActor;
        }
    }


    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartMessage.class, startMessage -> {
                    log.info("I was called by {} for slip id {}", getSender(), startMessage.slipId);
                    startMessage.featureCalculatorActor.tell(new FeatureCalculatorActor.CalculateFeatureMessage(startMessage.slipId, getSender()),  getSelf());
                    //               context().stop(self());
                })
                .match(ProbabilityMessage.class, probabilityMessage -> {
                    log.info("I was called by {} for probability {}", getSender(), probabilityMessage.probability);
                    //               context().stop(self());
                     probabilityMessage.resultActor.tell(probabilityMessage, getSelf());
                })
                .build();
    }

}
