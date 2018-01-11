package org.test.akka.prediction;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Map;

public class PredictorActor extends AbstractActor {
    private ActorRef accertifyActor = null;

    public PredictorActor(ActorRef accertifyActor) {
        this.accertifyActor = accertifyActor;
    }

    static public Props props(ActorRef accertifyActor) {
        return Props.create(PredictorActor.class, () -> new PredictorActor(accertifyActor));
    }

    static public class PredictMessage {
        public final Map<FeatureType, Double> features;
        public final ActorRef resultActor;

        public PredictMessage(Map<FeatureType, Double> features, ActorRef resultActor) {
            this.features = features;
            this.resultActor = resultActor;
        }
    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PredictMessage.class, predictMessage -> {
                   log.info("I was called by {} for features {}", getSender(), predictMessage.features);
                   Double probability = (new ProbabilityCalculatorPY()).calculate(predictMessage.features);
                   accertifyActor.tell(new AccertifyActor.UpdatePrababilityMessage(probability, predictMessage.resultActor), getSelf());
//                   context().stop(self());
                })
                .build();
    }
}
