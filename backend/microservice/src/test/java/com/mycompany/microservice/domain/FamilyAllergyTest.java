package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.FamilyAllergyTestSamples.*;
import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyAllergyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyAllergy.class);
        FamilyAllergy familyAllergy1 = getFamilyAllergySample1();
        FamilyAllergy familyAllergy2 = new FamilyAllergy();
        assertThat(familyAllergy1).isNotEqualTo(familyAllergy2);

        familyAllergy2.setId(familyAllergy1.getId());
        assertThat(familyAllergy1).isEqualTo(familyAllergy2);

        familyAllergy2 = getFamilyAllergySample2();
        assertThat(familyAllergy1).isNotEqualTo(familyAllergy2);
    }

    @Test
    void patientTest() {
        FamilyAllergy familyAllergy = getFamilyAllergyRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        familyAllergy.setPatient(patientBack);
        assertThat(familyAllergy.getPatient()).isEqualTo(patientBack);

        familyAllergy.patient(null);
        assertThat(familyAllergy.getPatient()).isNull();
    }
}
