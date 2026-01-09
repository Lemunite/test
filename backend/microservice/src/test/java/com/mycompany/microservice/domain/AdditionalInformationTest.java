package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.AdditionalInformationTestSamples.*;
import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdditionalInformationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdditionalInformation.class);
        AdditionalInformation additionalInformation1 = getAdditionalInformationSample1();
        AdditionalInformation additionalInformation2 = new AdditionalInformation();
        assertThat(additionalInformation1).isNotEqualTo(additionalInformation2);

        additionalInformation2.setId(additionalInformation1.getId());
        assertThat(additionalInformation1).isEqualTo(additionalInformation2);

        additionalInformation2 = getAdditionalInformationSample2();
        assertThat(additionalInformation1).isNotEqualTo(additionalInformation2);
    }

    @Test
    void patientTest() {
        AdditionalInformation additionalInformation = getAdditionalInformationRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        additionalInformation.setPatient(patientBack);
        assertThat(additionalInformation.getPatient()).isEqualTo(patientBack);
        assertThat(patientBack.getAdditionalInfo()).isEqualTo(additionalInformation);

        additionalInformation.patient(null);
        assertThat(additionalInformation.getPatient()).isNull();
        assertThat(patientBack.getAdditionalInfo()).isNull();
    }
}
