package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VaccinationTCMRTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VaccinationTCMR getVaccinationTCMRSample1() {
        return new VaccinationTCMR().id(1L).reaction("reaction1");
    }

    public static VaccinationTCMR getVaccinationTCMRSample2() {
        return new VaccinationTCMR().id(2L).reaction("reaction2");
    }

    public static VaccinationTCMR getVaccinationTCMRRandomSampleGenerator() {
        return new VaccinationTCMR().id(longCount.incrementAndGet()).reaction(UUID.randomUUID().toString());
    }
}
