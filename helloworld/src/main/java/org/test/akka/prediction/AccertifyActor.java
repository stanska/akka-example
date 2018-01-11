package org.test.akka.prediction;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AccertifyActor extends AbstractActor {
    static public Props props() {
        return Props.create(AccertifyActor.class, () -> new AccertifyActor());
    }

    static public class UpdatePrababilityMessage {
        public final Double probability;
        public final ActorRef resultActor;

        public UpdatePrababilityMessage(Double probability, ActorRef resultActor) {
            this.probability = probability;
            this.resultActor = resultActor;
        }
    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UpdatePrababilityMessage.class, updateProbabilityMessage -> {
                    new AccertifyCommunicator().update();
                    log.info("I was called by {} for prability {}", getSender(), updateProbabilityMessage.probability);
                    //TODO: update in accertify
                    //getSender().tell(new AccertifyActor.UpdatePrababilityMessage(featureCalculator.calculate(predictMessage.slipId)), getSelf());
//                    context().stop(self());
                })
                .build();
    }
}
