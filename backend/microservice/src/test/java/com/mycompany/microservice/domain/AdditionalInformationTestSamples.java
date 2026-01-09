package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AdditionalInformationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AdditionalInformation getAdditionalInformationSample1() {
        return new AdditionalInformation()
            .id(1L)
            .alcoholGlassesPerDay(1)
            .cancer("cancer1")
            .tuberculosis("tuberculosis1")
            .contraceptiveMethod("contraceptiveMethod1")
            .lastPregnancy(1)
            .numberOfPregnancies(1)
            .numberOfMiscarriages(1)
            .numberOfAbortions(1)
            .numberOfBirths(1)
            .vaginalDelivery(1)
            .cesareanSection(1)
            .difficultDelivery(1)
            .numberOfFullTermBirths(1)
            .numberOfPrematureBirths(1)
            .numberOfChildrenAlive(1)
            .gynecologicalDiseases("gynecologicalDiseases1");
    }

    public static AdditionalInformation getAdditionalInformationSample2() {
        return new AdditionalInformation()
            .id(2L)
            .alcoholGlassesPerDay(2)
            .cancer("cancer2")
            .tuberculosis("tuberculosis2")
            .contraceptiveMethod("contraceptiveMethod2")
            .lastPregnancy(2)
            .numberOfPregnancies(2)
            .numberOfMiscarriages(2)
            .numberOfAbortions(2)
            .numberOfBirths(2)
            .vaginalDelivery(2)
            .cesareanSection(2)
            .difficultDelivery(2)
            .numberOfFullTermBirths(2)
            .numberOfPrematureBirths(2)
            .numberOfChildrenAlive(2)
            .gynecologicalDiseases("gynecologicalDiseases2");
    }

    public static AdditionalInformation getAdditionalInformationRandomSampleGenerator() {
        return new AdditionalInformation()
            .id(longCount.incrementAndGet())
            .alcoholGlassesPerDay(intCount.incrementAndGet())
            .cancer(UUID.randomUUID().toString())
            .tuberculosis(UUID.randomUUID().toString())
            .contraceptiveMethod(UUID.randomUUID().toString())
            .lastPregnancy(intCount.incrementAndGet())
            .numberOfPregnancies(intCount.incrementAndGet())
            .numberOfMiscarriages(intCount.incrementAndGet())
            .numberOfAbortions(intCount.incrementAndGet())
            .numberOfBirths(intCount.incrementAndGet())
            .vaginalDelivery(intCount.incrementAndGet())
            .cesareanSection(intCount.incrementAndGet())
            .difficultDelivery(intCount.incrementAndGet())
            .numberOfFullTermBirths(intCount.incrementAndGet())
            .numberOfPrematureBirths(intCount.incrementAndGet())
            .numberOfChildrenAlive(intCount.incrementAndGet())
            .gynecologicalDiseases(UUID.randomUUID().toString());
    }
}
