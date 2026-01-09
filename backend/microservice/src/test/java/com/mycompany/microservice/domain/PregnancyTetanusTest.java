package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static com.mycompany.microservice.domain.PregnancyTetanusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PregnancyTetanusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PregnancyTetanus.class);
        PregnancyTetanus pregnancyTetanus1 = getPregnancyTetanusSample1();
        PregnancyTetanus pregnancyTetanus2 = new PregnancyTetanus();
        assertThat(pregnancyTetanus1).isNotEqualTo(pregnancyTetanus2);

        pregnancyTetanus2.setId(pregnancyTetanus1.getId());
        assertThat(pregnancyTetanus1).isEqualTo(pregnancyTetanus2);

        pregnancyTetanus2 = getPregnancyTetanusSample2();
        assertThat(pregnancyTetanus1).isNotEqualTo(pregnancyTetanus2);
    }

    @Test
    void patientTest() {
        PregnancyTetanus pregnancyTetanus = getPregnancyTetanusRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        pregnancyTetanus.setPatient(patientBack);
        assertThat(pregnancyTetanus.getPatient()).isEqualTo(patientBack);

        pregnancyTetanus.patient(null);
        assertThat(pregnancyTetanus.getPatient()).isNull();
    }
}
