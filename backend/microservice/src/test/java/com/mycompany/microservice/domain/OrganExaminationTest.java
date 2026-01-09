package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.MedicalRecordTestSamples.*;
import static com.mycompany.microservice.domain.OrganExaminationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganExaminationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganExamination.class);
        OrganExamination organExamination1 = getOrganExaminationSample1();
        OrganExamination organExamination2 = new OrganExamination();
        assertThat(organExamination1).isNotEqualTo(organExamination2);

        organExamination2.setId(organExamination1.getId());
        assertThat(organExamination1).isEqualTo(organExamination2);

        organExamination2 = getOrganExaminationSample2();
        assertThat(organExamination1).isNotEqualTo(organExamination2);
    }

    @Test
    void medicalRecordTest() {
        OrganExamination organExamination = getOrganExaminationRandomSampleGenerator();
        MedicalRecord medicalRecordBack = getMedicalRecordRandomSampleGenerator();

        organExamination.setMedicalRecord(medicalRecordBack);
        assertThat(organExamination.getMedicalRecord()).isEqualTo(medicalRecordBack);
        assertThat(medicalRecordBack.getOrganExamination()).isEqualTo(organExamination);

        organExamination.medicalRecord(null);
        assertThat(organExamination.getMedicalRecord()).isNull();
        assertThat(medicalRecordBack.getOrganExamination()).isNull();
    }
}
