package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DisabilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Disability getDisabilitySample1() {
        return new Disability().id(1L).description("description1");
    }

    public static Disability getDisabilitySample2() {
        return new Disability().id(2L).description("description2");
    }

    public static Disability getDisabilityRandomSampleGenerator() {
        return new Disability().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
