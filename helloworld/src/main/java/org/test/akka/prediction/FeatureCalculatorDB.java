package org.test.akka.prediction;

import java.util.HashMap;
import java.util.Map;

public class FeatureCalculatorDB {

    public Map<FeatureType, Double> calculate(Long slipId) {
        //log probability
        //CREATE TABLE AF.MODEL_FEAUTURE(SLIP_ID NUMBER NOT NULL, NAME VARCHAR2() -- add check constraint, RESULT NUMBER NOT NULL) --PK on slip_id and name
        Map<FeatureType, Double> features = new HashMap<>();
        features.put(FeatureType.LAST_SUCCESSFUL_DEPOSIT, 123d);
        features.put(FeatureType.TRANSACTION_AMOUNT, 23d);
        features.put(FeatureType.TXM_DEVICE, 34d);

        return features;
    }
}
