package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.DiseaseTestSamples.*;
import static com.mycompany.microservice.domain.FamilyDiseaseTestSamples.*;
import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FamilyDiseaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyDisease.class);
        FamilyDisease familyDisease1 = getFamilyDiseaseSample1();
        FamilyDisease familyDisease2 = new FamilyDisease();
        assertThat(familyDisease1).isNotEqualTo(familyDisease2);

        familyDisease2.setId(familyDisease1.getId());
        assertThat(familyDisease1).isEqualTo(familyDisease2);

        familyDisease2 = getFamilyDiseaseSample2();
        assertThat(familyDisease1).isNotEqualTo(familyDisease2);
    }

    @Test
    void diseaseTest() {
        FamilyDisease familyDisease = getFamilyDiseaseRandomSampleGenerator();
        Disease diseaseBack = getDiseaseRandomSampleGenerator();

        familyDisease.addDisease(diseaseBack);
        assertThat(familyDisease.getDiseases()).containsOnly(diseaseBack);
        assertThat(diseaseBack.getFamilyDisease()).isEqualTo(familyDisease);

        familyDisease.removeDisease(diseaseBack);
        assertThat(familyDisease.getDiseases()).doesNotContain(diseaseBack);
        assertThat(diseaseBack.getFamilyDisease()).isNull();

        familyDisease.diseases(new HashSet<>(Set.of(diseaseBack)));
        assertThat(familyDisease.getDiseases()).containsOnly(diseaseBack);
        assertThat(diseaseBack.getFamilyDisease()).isEqualTo(familyDisease);

        familyDisease.setDiseases(new HashSet<>());
        assertThat(familyDisease.getDiseases()).doesNotContain(diseaseBack);
        assertThat(diseaseBack.getFamilyDisease()).isNull();
    }

    @Test
    void patientTest() {
        FamilyDisease familyDisease = getFamilyDiseaseRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        familyDisease.setPatient(patientBack);
        assertThat(familyDisease.getPatient()).isEqualTo(patientBack);

        familyDisease.patient(null);
        assertThat(familyDisease.getPatient()).isNull();
    }
}
