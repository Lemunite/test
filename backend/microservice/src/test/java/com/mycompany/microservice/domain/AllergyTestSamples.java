package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AllergyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Allergy getAllergySample1() {
        return new Allergy().id(1L).description("description1");
    }

    public static Allergy getAllergySample2() {
        return new Allergy().id(2L).description("description2");
    }

    public static Allergy getAllergyRandomSampleGenerator() {
        return new Allergy().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
