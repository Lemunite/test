package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.DisabilityTestSamples.*;
import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disability.class);
        Disability disability1 = getDisabilitySample1();
        Disability disability2 = new Disability();
        assertThat(disability1).isNotEqualTo(disability2);

        disability2.setId(disability1.getId());
        assertThat(disability1).isEqualTo(disability2);

        disability2 = getDisabilitySample2();
        assertThat(disability1).isNotEqualTo(disability2);
    }

    @Test
    void patientTest() {
        Disability disability = getDisabilityRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        disability.setPatient(patientBack);
        assertThat(disability.getPatient()).isEqualTo(patientBack);

        disability.patient(null);
        assertThat(disability.getPatient()).isNull();
    }
}
