package org.test.akka.prediction.synchro;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.test.akka.prediction.AccertifyActor;
import org.test.akka.prediction.FeatureCalculatorActor;
import org.test.akka.prediction.PredictorActor;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class ProbabilityCalculatorSyncro {
    private static ActorSystem system = null;
    private static ActorRef masterActor;
    private static ActorRef accertifyActor;
    private static ActorRef predictor;
    private static ActorRef featureCalculatorActor;

    public ProbabilityCalculatorSyncro() {
        if ( this.system == null ) {
            this.system = ActorSystem.create("fraud_prediction_synchro");
            masterActor = system.actorOf(MasterActor.props(), "masterActorSynchro");
            accertifyActor = system.actorOf(AccertifyActorSynchro.props(masterActor), "accertifyActorSynchro");
            predictor = system.actorOf(PredictorActor.props(accertifyActor), "predictorActorSynchro");
            featureCalculatorActor = system.actorOf(FeatureCalculatorActor.props(predictor), "featureCalculatorActorSynchro");
        }

    }

    public Double predict(Long slipId) {


        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
        Future<Object> future = Patterns.ask(masterActor, new MasterActor.StartMessage(slipId, featureCalculatorActor, predictor), timeout);
        try {
            MasterActor.ProbabilityMessage result = (MasterActor.ProbabilityMessage) Await.result(future, timeout.duration());

            return result.probability;
        } catch (Exception e) {
            e.printStackTrace();
            return -1d;
        }
    }
}
