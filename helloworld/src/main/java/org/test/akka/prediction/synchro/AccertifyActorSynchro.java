package org.test.akka.prediction.synchro;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.test.akka.prediction.AccertifyActor;
import org.test.akka.prediction.AccertifyCommunicator;

public class AccertifyActorSynchro extends AbstractActor{
    private ActorRef masterActor = null;

    public AccertifyActorSynchro(ActorRef masterActor) {
        this.masterActor = masterActor;
    }

    static public Props props(ActorRef masterActor) {
        return Props.create(AccertifyActorSynchro.class, () -> new AccertifyActorSynchro(masterActor));
    }

    static public class UpdatePrababilityMessage {
        public final Double probability;
        public UpdatePrababilityMessage(Double probability) {
            this.probability = probability;
        }
    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(AccertifyActor.UpdatePrababilityMessage.class, updateProbabilityMessage -> {
                    log.info("I was called by {} for prability {}", getSender(), updateProbabilityMessage.probability);
                    new AccertifyCommunicator().update();
                    masterActor.tell(new MasterActor.ProbabilityMessage(updateProbabilityMessage.probability, updateProbabilityMessage.resultActor), getSelf());
                    //TODO: update in accertify
                    //getSender().tell(new AccertifyActor.UpdatePrababilityMessage(featureCalculator.calculate(predictMessage.slipId)), getSelf());
//                    context().stop(self());
                })
                .build();
    }
}
