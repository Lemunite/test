package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VaccineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vaccine getVaccineSample1() {
        return new Vaccine().id(1L).reaction("reaction1");
    }

    public static Vaccine getVaccineSample2() {
        return new Vaccine().id(2L).reaction("reaction2");
    }

    public static Vaccine getVaccineRandomSampleGenerator() {
        return new Vaccine().id(longCount.incrementAndGet()).reaction(UUID.randomUUID().toString());
    }
}
