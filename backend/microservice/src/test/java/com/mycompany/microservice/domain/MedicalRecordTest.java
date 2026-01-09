package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.MedicalRecordTestSamples.*;
import static com.mycompany.microservice.domain.OrganExaminationTestSamples.*;
import static com.mycompany.microservice.domain.ParaclinicalResultTestSamples.*;
import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicalRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalRecord.class);
        MedicalRecord medicalRecord1 = getMedicalRecordSample1();
        MedicalRecord medicalRecord2 = new MedicalRecord();
        assertThat(medicalRecord1).isNotEqualTo(medicalRecord2);

        medicalRecord2.setId(medicalRecord1.getId());
        assertThat(medicalRecord1).isEqualTo(medicalRecord2);

        medicalRecord2 = getMedicalRecordSample2();
        assertThat(medicalRecord1).isNotEqualTo(medicalRecord2);
    }

    @Test
    void organExaminationTest() {
        MedicalRecord medicalRecord = getMedicalRecordRandomSampleGenerator();
        OrganExamination organExaminationBack = getOrganExaminationRandomSampleGenerator();

        medicalRecord.setOrganExamination(organExaminationBack);
        assertThat(medicalRecord.getOrganExamination()).isEqualTo(organExaminationBack);

        medicalRecord.organExamination(null);
        assertThat(medicalRecord.getOrganExamination()).isNull();
    }

    @Test
    void paraclinicalResultsTest() {
        MedicalRecord medicalRecord = getMedicalRecordRandomSampleGenerator();
        ParaclinicalResult paraclinicalResultBack = getParaclinicalResultRandomSampleGenerator();

        medicalRecord.addParaclinicalResults(paraclinicalResultBack);
        assertThat(medicalRecord.getParaclinicalResults()).containsOnly(paraclinicalResultBack);
        assertThat(paraclinicalResultBack.getMedicalRecord()).isEqualTo(medicalRecord);

        medicalRecord.removeParaclinicalResults(paraclinicalResultBack);
        assertThat(medicalRecord.getParaclinicalResults()).doesNotContain(paraclinicalResultBack);
        assertThat(paraclinicalResultBack.getMedicalRecord()).isNull();

        medicalRecord.paraclinicalResults(new HashSet<>(Set.of(paraclinicalResultBack)));
        assertThat(medicalRecord.getParaclinicalResults()).containsOnly(paraclinicalResultBack);
        assertThat(paraclinicalResultBack.getMedicalRecord()).isEqualTo(medicalRecord);

        medicalRecord.setParaclinicalResults(new HashSet<>());
        assertThat(medicalRecord.getParaclinicalResults()).doesNotContain(paraclinicalResultBack);
        assertThat(paraclinicalResultBack.getMedicalRecord()).isNull();
    }

    @Test
    void patientTest() {
        MedicalRecord medicalRecord = getMedicalRecordRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        medicalRecord.setPatient(patientBack);
        assertThat(medicalRecord.getPatient()).isEqualTo(patientBack);

        medicalRecord.patient(null);
        assertThat(medicalRecord.getPatient()).isNull();
    }
}
