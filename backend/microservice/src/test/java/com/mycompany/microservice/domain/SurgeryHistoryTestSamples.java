package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SurgeryHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SurgeryHistory getSurgeryHistorySample1() {
        return new SurgeryHistory().id(1L).bodyPart("bodyPart1").surgeryYear(1).note("note1");
    }

    public static SurgeryHistory getSurgeryHistorySample2() {
        return new SurgeryHistory().id(2L).bodyPart("bodyPart2").surgeryYear(2).note("note2");
    }

    public static SurgeryHistory getSurgeryHistoryRandomSampleGenerator() {
        return new SurgeryHistory()
            .id(longCount.incrementAndGet())
            .bodyPart(UUID.randomUUID().toString())
            .surgeryYear(intCount.incrementAndGet())
            .note(UUID.randomUUID().toString());
    }
}
