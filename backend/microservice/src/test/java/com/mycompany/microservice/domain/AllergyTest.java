package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.AllergyTestSamples.*;
import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllergyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Allergy.class);
        Allergy allergy1 = getAllergySample1();
        Allergy allergy2 = new Allergy();
        assertThat(allergy1).isNotEqualTo(allergy2);

        allergy2.setId(allergy1.getId());
        assertThat(allergy1).isEqualTo(allergy2);

        allergy2 = getAllergySample2();
        assertThat(allergy1).isNotEqualTo(allergy2);
    }

    @Test
    void patientTest() {
        Allergy allergy = getAllergyRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        allergy.setPatient(patientBack);
        assertThat(allergy.getPatient()).isEqualTo(patientBack);

        allergy.patient(null);
        assertThat(allergy.getPatient()).isNull();
    }
}
