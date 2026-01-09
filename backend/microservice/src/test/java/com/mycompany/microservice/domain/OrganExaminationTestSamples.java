package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrganExaminationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrganExamination getOrganExaminationSample1() {
        return new OrganExamination()
            .id(1L)
            .cardiovascular("cardiovascular1")
            .respiratory("respiratory1")
            .digestive("digestive1")
            .urinary("urinary1")
            .musculoskeletal("musculoskeletal1")
            .endocrine("endocrine1")
            .neurological("neurological1")
            .psychiatric("psychiatric1")
            .surgery("surgery1")
            .obstetricsAndGynecology("obstetricsAndGynecology1")
            .otolaryngology("otolaryngology1")
            .dentistryAndMaxillofacialSurgery("dentistryAndMaxillofacialSurgery1")
            .eye("eye1")
            .dermatology("dermatology1")
            .nutrition("nutrition1")
            .exercise("exercise1")
            .other("other1")
            .developmentAssessment("developmentAssessment1");
    }

    public static OrganExamination getOrganExaminationSample2() {
        return new OrganExamination()
            .id(2L)
            .cardiovascular("cardiovascular2")
            .respiratory("respiratory2")
            .digestive("digestive2")
            .urinary("urinary2")
            .musculoskeletal("musculoskeletal2")
            .endocrine("endocrine2")
            .neurological("neurological2")
            .psychiatric("psychiatric2")
            .surgery("surgery2")
            .obstetricsAndGynecology("obstetricsAndGynecology2")
            .otolaryngology("otolaryngology2")
            .dentistryAndMaxillofacialSurgery("dentistryAndMaxillofacialSurgery2")
            .eye("eye2")
            .dermatology("dermatology2")
            .nutrition("nutrition2")
            .exercise("exercise2")
            .other("other2")
            .developmentAssessment("developmentAssessment2");
    }

    public static OrganExamination getOrganExaminationRandomSampleGenerator() {
        return new OrganExamination()
            .id(longCount.incrementAndGet())
            .cardiovascular(UUID.randomUUID().toString())
            .respiratory(UUID.randomUUID().toString())
            .digestive(UUID.randomUUID().toString())
            .urinary(UUID.randomUUID().toString())
            .musculoskeletal(UUID.randomUUID().toString())
            .endocrine(UUID.randomUUID().toString())
            .neurological(UUID.randomUUID().toString())
            .psychiatric(UUID.randomUUID().toString())
            .surgery(UUID.randomUUID().toString())
            .obstetricsAndGynecology(UUID.randomUUID().toString())
            .otolaryngology(UUID.randomUUID().toString())
            .dentistryAndMaxillofacialSurgery(UUID.randomUUID().toString())
            .eye(UUID.randomUUID().toString())
            .dermatology(UUID.randomUUID().toString())
            .nutrition(UUID.randomUUID().toString())
            .exercise(UUID.randomUUID().toString())
            .other(UUID.randomUUID().toString())
            .developmentAssessment(UUID.randomUUID().toString());
    }
}
