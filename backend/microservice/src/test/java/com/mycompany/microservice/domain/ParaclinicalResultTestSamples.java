package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ParaclinicalResultTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ParaclinicalResult getParaclinicalResultSample1() {
        return new ParaclinicalResult().id(1L).testName("testName1");
    }

    public static ParaclinicalResult getParaclinicalResultSample2() {
        return new ParaclinicalResult().id(2L).testName("testName2");
    }

    public static ParaclinicalResult getParaclinicalResultRandomSampleGenerator() {
        return new ParaclinicalResult().id(longCount.incrementAndGet()).testName(UUID.randomUUID().toString());
    }
}
