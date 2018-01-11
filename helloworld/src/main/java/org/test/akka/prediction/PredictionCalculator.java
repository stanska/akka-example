package org.test.akka.prediction;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class PredictionCalculator {
    private static ActorSystem system = null;
    private static ActorRef accertifyActor;
    private static ActorRef predictor;
    private static ActorRef featureCalculatorActor;

    public PredictionCalculator() {
        if ( this.system == null ) {
            this.system = ActorSystem.create("fraud_prediction");
            accertifyActor = system.actorOf(AccertifyActor.props(), "accertifyActor");
            predictor = system.actorOf(PredictorActor.props(accertifyActor), "predictorActor");
            featureCalculatorActor = system.actorOf(FeatureCalculatorActor.props(predictor), "featureCalculatorActor");
        }
    }

    public void predict(Long slipId) {
        featureCalculatorActor.tell(new FeatureCalculatorActor.CalculateFeatureMessage(slipId, accertifyActor), predictor);

    }
}
