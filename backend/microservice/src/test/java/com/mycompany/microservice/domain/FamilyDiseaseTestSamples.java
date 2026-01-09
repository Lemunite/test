package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FamilyDiseaseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FamilyDisease getFamilyDiseaseSample1() {
        return new FamilyDisease().id(1L).affectedPerson("affectedPerson1").relationshipToPatient("relationshipToPatient1");
    }

    public static FamilyDisease getFamilyDiseaseSample2() {
        return new FamilyDisease().id(2L).affectedPerson("affectedPerson2").relationshipToPatient("relationshipToPatient2");
    }

    public static FamilyDisease getFamilyDiseaseRandomSampleGenerator() {
        return new FamilyDisease()
            .id(longCount.incrementAndGet())
            .affectedPerson(UUID.randomUUID().toString())
            .relationshipToPatient(UUID.randomUUID().toString());
    }
}
