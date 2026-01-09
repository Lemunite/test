package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.MedicalRecordTestSamples.*;
import static com.mycompany.microservice.domain.ParaclinicalResultTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParaclinicalResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParaclinicalResult.class);
        ParaclinicalResult paraclinicalResult1 = getParaclinicalResultSample1();
        ParaclinicalResult paraclinicalResult2 = new ParaclinicalResult();
        assertThat(paraclinicalResult1).isNotEqualTo(paraclinicalResult2);

        paraclinicalResult2.setId(paraclinicalResult1.getId());
        assertThat(paraclinicalResult1).isEqualTo(paraclinicalResult2);

        paraclinicalResult2 = getParaclinicalResultSample2();
        assertThat(paraclinicalResult1).isNotEqualTo(paraclinicalResult2);
    }

    @Test
    void medicalRecordTest() {
        ParaclinicalResult paraclinicalResult = getParaclinicalResultRandomSampleGenerator();
        MedicalRecord medicalRecordBack = getMedicalRecordRandomSampleGenerator();

        paraclinicalResult.setMedicalRecord(medicalRecordBack);
        assertThat(paraclinicalResult.getMedicalRecord()).isEqualTo(medicalRecordBack);

        paraclinicalResult.medicalRecord(null);
        assertThat(paraclinicalResult.getMedicalRecord()).isNull();
    }
}
