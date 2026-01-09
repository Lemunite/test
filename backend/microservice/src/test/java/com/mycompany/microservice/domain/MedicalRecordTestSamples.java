package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MedicalRecord getMedicalRecordSample1() {
        return new MedicalRecord()
            .id(1L)
            .pulse(1)
            .bloodPressure("bloodPressure1")
            .respiratoryRate(1)
            .skinMucosa("skinMucosa1")
            .other("other1")
            .diseaseName("diseaseName1")
            .diseaseCode("diseaseCode1")
            .docterName("docterName1");
    }

    public static MedicalRecord getMedicalRecordSample2() {
        return new MedicalRecord()
            .id(2L)
            .pulse(2)
            .bloodPressure("bloodPressure2")
            .respiratoryRate(2)
            .skinMucosa("skinMucosa2")
            .other("other2")
            .diseaseName("diseaseName2")
            .diseaseCode("diseaseCode2")
            .docterName("docterName2");
    }

    public static MedicalRecord getMedicalRecordRandomSampleGenerator() {
        return new MedicalRecord()
            .id(longCount.incrementAndGet())
            .pulse(intCount.incrementAndGet())
            .bloodPressure(UUID.randomUUID().toString())
            .respiratoryRate(intCount.incrementAndGet())
            .skinMucosa(UUID.randomUUID().toString())
            .other(UUID.randomUUID().toString())
            .diseaseName(UUID.randomUUID().toString())
            .diseaseCode(UUID.randomUUID().toString())
            .docterName(UUID.randomUUID().toString());
    }
}
