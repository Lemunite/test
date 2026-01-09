package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PregnancyTetanusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PregnancyTetanus getPregnancyTetanusSample1() {
        return new PregnancyTetanus().id(1L).pregnancyMonth(1).reaction("reaction1").numberOfDosesReceived(1);
    }

    public static PregnancyTetanus getPregnancyTetanusSample2() {
        return new PregnancyTetanus().id(2L).pregnancyMonth(2).reaction("reaction2").numberOfDosesReceived(2);
    }

    public static PregnancyTetanus getPregnancyTetanusRandomSampleGenerator() {
        return new PregnancyTetanus()
            .id(longCount.incrementAndGet())
            .pregnancyMonth(intCount.incrementAndGet())
            .reaction(UUID.randomUUID().toString())
            .numberOfDosesReceived(intCount.incrementAndGet());
    }
}
