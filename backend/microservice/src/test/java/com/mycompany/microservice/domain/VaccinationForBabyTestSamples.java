package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VaccinationForBabyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VaccinationForBaby getVaccinationForBabySample1() {
        return new VaccinationForBaby().id(1L).numberUse(1);
    }

    public static VaccinationForBaby getVaccinationForBabySample2() {
        return new VaccinationForBaby().id(2L).numberUse(2);
    }

    public static VaccinationForBaby getVaccinationForBabyRandomSampleGenerator() {
        return new VaccinationForBaby().id(longCount.incrementAndGet()).numberUse(intCount.incrementAndGet());
    }
}
