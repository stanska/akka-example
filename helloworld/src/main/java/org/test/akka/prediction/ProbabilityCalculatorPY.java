package org.test.akka.prediction;

import java.util.Map;

public class ProbabilityCalculatorPY  {
    public Double calculate(Map<FeatureType, Double> features) {
        //communicate with python
        //CREATE TABLE AF.MODEL_PROBABILITY(SLIP_ID NUMBER PRIMARY KEY, RESULT NUMBER NOT NULL)
        //log probability
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0.3;
    }
}
