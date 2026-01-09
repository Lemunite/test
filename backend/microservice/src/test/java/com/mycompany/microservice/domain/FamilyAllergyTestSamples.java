package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FamilyAllergyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FamilyAllergy getFamilyAllergySample1() {
        return new FamilyAllergy().id(1L).description("description1").affectedPerson("affectedPerson1");
    }

    public static FamilyAllergy getFamilyAllergySample2() {
        return new FamilyAllergy().id(2L).description("description2").affectedPerson("affectedPerson2");
    }

    public static FamilyAllergy getFamilyAllergyRandomSampleGenerator() {
        return new FamilyAllergy()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .affectedPerson(UUID.randomUUID().toString());
    }
}
