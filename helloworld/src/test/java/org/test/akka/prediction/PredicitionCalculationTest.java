package org.test.akka.prediction;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class PredicitionCalculationTest {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void featureCalculatorActorCallsPredictorActor() {
        //GIVEN
        final TestKit accertifyActorMock = new TestKit(system);
        final TestKit predictionActorMock = new TestKit(system);
        final ActorRef featureCalculatorActor = system.actorOf(FeatureCalculatorActor.props(predictionActorMock.getRef()));
        //WHEN
        featureCalculatorActor.tell(new FeatureCalculatorActor.CalculateFeatureMessage(123L, predictionActorMock.getRef()), predictionActorMock.getRef());
        //THEN
        PredictorActor.PredictMessage predictMessage = predictionActorMock.expectMsgClass(PredictorActor.PredictMessage.class);
        assertEquals(new Double(23), predictMessage.features.get(FeatureType.TRANSACTION_AMOUNT));
    }

    @Test
    public void predictorActorCallsAccertifyActor() {
        //GIVEN
        final TestKit accertifyActorMock = new TestKit(system);
        final ActorRef predictorActor = system.actorOf(PredictorActor.props(accertifyActorMock.getRef()));
        Map<FeatureType, Double> features =new HashMap<>();
        //WHEN
        predictorActor.tell(new PredictorActor.PredictMessage( features, accertifyActorMock.getRef()), accertifyActorMock.getRef());
        //THEN
        AccertifyActor.UpdatePrababilityMessage updateMessage = accertifyActorMock.expectMsgClass(AccertifyActor.UpdatePrababilityMessage.class);
        assertEquals(new Double(0.3), updateMessage.probability);
    }
}
