package org.test.akka.prediction.parallel;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.test.akka.prediction.FeatureCalculatorActor;
import org.test.akka.prediction.PredictorActor;
import org.test.akka.prediction.synchro.AccertifyActorSynchro;
import org.test.akka.prediction.synchro.MasterActor;
import scala.Tuple2;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.concurrent.*;

public class ProbabilityCalculatorParallel {
    private static ActorSystem system = null;
    private static ActorRef masterActor;
    private static ActorRef slowA;
    private static ActorRef slowB;

    public ProbabilityCalculatorParallel() {
        if ( this.system == null ) {
            this.system = ActorSystem.create("fraud_prediction_parallel");
            masterActor = system.actorOf(MasterActorParallel.props(), "masterActorParallel");
            slowA = system.actorOf(SlowA.props(), "slowAActor");
            slowB = system.actorOf(SlowB.props(), "slowBActor");
        }

    }

    public Double combine(Long slipId) {

        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
        final ArrayList<Future<Object>> futures = new ArrayList<>();
        Future<Object> futureB = Patterns.ask(slowB, new SlowB.StartMessageB(), timeout);
        futures.add(futureB);
        Future<Object> futureA= Patterns.ask(slowA, new SlowA.StartMessageA(), timeout);
        futures.add(futureA);

        final Future<Iterable<Object>> aggregate = Futures.sequence(futures, system.dispatcher());
        final Future<Double> transformed = aggregate.map(
                new Mapper<Iterable<Object>, Double>() {
                    public Double apply(Iterable<Object> iterable) {
                        Double result = 0d;
                        for (Object o : iterable) {
                            final MasterActorParallel.FinalMessage message = (MasterActorParallel.FinalMessage)o;
                            result += message.value;
                        }
                        return result;
                    }
                }, system.dispatcher());


        try {
            return  (Double) Await.result(transformed, timeout.duration());
        } catch (Exception e) {
            e.printStackTrace();
            return -1d;
        }
    }
}
